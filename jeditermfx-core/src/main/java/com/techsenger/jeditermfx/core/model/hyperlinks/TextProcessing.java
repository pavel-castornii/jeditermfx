package com.techsenger.jeditermfx.core.model.hyperlinks;

import com.techsenger.jeditermfx.core.HyperlinkStyle;
import com.techsenger.jeditermfx.core.TextStyle;
import com.techsenger.jeditermfx.core.model.CharBuffer;
import com.techsenger.jeditermfx.core.model.LinesBuffer;
import com.techsenger.jeditermfx.core.model.TerminalLine;
import com.techsenger.jeditermfx.core.model.TerminalTextBuffer;
import com.techsenger.jeditermfx.core.util.CharUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author traff
 */
public class TextProcessing {

    private static final Logger logger = LoggerFactory.getLogger(TextProcessing.class);

    private final List<HyperlinkFilter> myHyperlinkFilter;

    private final TextStyle myHyperlinkColor;

    private final HyperlinkStyle.HighlightMode myHighlightMode;

    private TerminalTextBuffer myTerminalTextBuffer;

    public TextProcessing(@NotNull TextStyle hyperlinkColor, @NotNull HyperlinkStyle.HighlightMode highlightMode) {
        myHyperlinkColor = hyperlinkColor;
        myHighlightMode = highlightMode;
        myHyperlinkFilter = new ArrayList<>();
    }

    public void setTerminalTextBuffer(@NotNull TerminalTextBuffer terminalTextBuffer) {
        myTerminalTextBuffer = terminalTextBuffer;
    }

    public void processHyperlinks(@NotNull LinesBuffer buffer, @NotNull TerminalLine updatedLine) {
        if (myHyperlinkFilter.isEmpty()) return;
        doProcessHyperlinks(buffer, updatedLine);
    }

    private void doProcessHyperlinks(@NotNull LinesBuffer buffer, @NotNull TerminalLine updatedLine) {
        myTerminalTextBuffer.lock();
        try {
            int updatedLineInd = findLineInd(buffer, updatedLine);
            if (updatedLineInd == -1) {
                // When lines arrive fast enough, the line might be pushed to the history buffer already.
                updatedLineInd = findHistoryLineInd(myTerminalTextBuffer.getHistoryBuffer(), updatedLine);
                if (updatedLineInd == -1) {
                    logger.debug("Cannot find line for links processing");
                    return;
                }
                buffer = myTerminalTextBuffer.getHistoryBuffer();
            }
            int startLineInd = updatedLineInd;
            while (startLineInd > 0 && buffer.getLine(startLineInd - 1).isWrapped()) {
                startLineInd--;
            }
            String lineStr = joinLines(buffer, startLineInd, updatedLineInd);
            for (HyperlinkFilter filter : myHyperlinkFilter) {
                LinkResult result = filter.apply(lineStr);
                if (result != null) {
                    for (LinkResultItem item : result.getItems()) {
                        TextStyle style = new HyperlinkStyle(myHyperlinkColor.getForeground(),
                                myHyperlinkColor.getBackground(), item.getLinkInfo(), myHighlightMode, null, null);
                        if (item.getStartOffset() < 0 || item.getEndOffset() > lineStr.length()) continue;
                        int prevLinesLength = 0;
                        for (int lineInd = startLineInd; lineInd <= updatedLineInd; lineInd++) {
                            int startLineOffset = Math.max(prevLinesLength, item.getStartOffset());
                            int endLineOffset = Math.min(prevLinesLength + myTerminalTextBuffer.getWidth(), item.getEndOffset());
                            if (startLineOffset < endLineOffset) {
                                var x = startLineOffset - prevLinesLength;
                                var finalStr = lineStr.substring(startLineOffset, endLineOffset);
                                var line = buffer.getLine(lineInd);
                                if (myHighlightMode.isOriginalColorUsed()) {
                                    var stylesByPosition = getStylesByPostion(line, x, finalStr.length());
                                    var entries = stylesByPosition.entrySet().stream().collect(Collectors.toList());
                                    for (var i = 0; i < entries.size(); i++) {
                                        var currentEntry = entries.get(i);
                                        Map.Entry<Integer, TextStyle> nextEntry = null;
                                        if (i + 1 < entries.size()) {
                                            nextEntry = entries.get(i + 1);
                                        }
                                        String str = null;
                                        if (nextEntry == null) {
                                            str = finalStr.substring(currentEntry.getKey() - x);
                                        } else {
                                            str = finalStr.substring(currentEntry.getKey() - x, nextEntry.getKey() - x);
                                        }
                                        var originalStyle = currentEntry.getValue();
                                        //is there any sense to create HyperLinkStyle from HyperLinkStyle?
                                        //note, that this method will be called twice
                                        if (!(originalStyle instanceof HyperlinkStyle)) {
                                            originalStyle = new HyperlinkStyle(myHyperlinkColor.getForeground(),
                                                    myHyperlinkColor.getBackground(), item.getLinkInfo(),
                                                    myHighlightMode, null, originalStyle);
                                        }
                                        var charBuf = new CharBuffer(str);
                                        line.writeString(currentEntry.getKey(), charBuf, originalStyle);
                                    }
                                } else {
                                    var charBuf = new CharBuffer(finalStr);
                                    line.writeString(x, charBuf, style);
                                }
                            }
                            prevLinesLength += myTerminalTextBuffer.getWidth();
                        }
                    }
                }
            }
        } finally {
            myTerminalTextBuffer.unlock();
        }
    }

    private Map<Integer, TextStyle> getStylesByPostion(TerminalLine line, int x, int length) {
        var treeMap = new TreeMap<Integer, TextStyle>();
        var entriesLength = 0;
        for (var e : line.getEntries()) {
            treeMap.put(entriesLength, e.getStyle());
            entriesLength += e.getLength();
        }
        Map<Integer, TextStyle> result = new LinkedHashMap<>();
        int end = x + length - 1;
        Map.Entry<Integer, TextStyle> entry = treeMap.floorEntry(x);
        if (entry == null) {
            return result;
        }
        result.put(x, entry.getValue());
        while (entry != null && entry.getKey() <= end) {
            if (!result.containsValue(entry.getValue())) {
                result.put(entry.getKey(), entry.getValue());
            }
            entry = treeMap.higherEntry(entry.getKey());
        }
        return result;
    }

    private int findHistoryLineInd(@NotNull LinesBuffer historyBuffer, @NotNull TerminalLine line) {
        int lastLineInd = Math.max(0, historyBuffer.getLineCount() - 200); // check only last lines in history buffer
        for (int i = historyBuffer.getLineCount() - 1; i >= lastLineInd; i--) {
            if (historyBuffer.getLine(i) == line) {
                return i;
            }
        }
        return -1;
    }

    private static int findLineInd(@NotNull LinesBuffer buffer, @NotNull TerminalLine line) {
        for (int i = 0; i < buffer.getLineCount(); i++) {
            TerminalLine l = buffer.getLine(i);
            if (l == line) {
                return i;
            }
        }
        return -1;
    }

    @NotNull
    private String joinLines(@NotNull LinesBuffer buffer, int startLineInd, int updatedLineInd) {
        StringBuilder result = new StringBuilder();
        for (int i = startLineInd; i <= updatedLineInd; i++) {
            String text = buffer.getLine(i).getText();
            if (i < updatedLineInd && text.length() < myTerminalTextBuffer.getWidth()) {
                text = text + new CharBuffer(CharUtils.NUL_CHAR, myTerminalTextBuffer.getWidth() - text.length());
            }
            result.append(text);
        }
        return result.toString();
    }

    public void addHyperlinkFilter(@NotNull HyperlinkFilter filter) {
        myHyperlinkFilter.add(filter);
    }

    public @NotNull List<LinkResultItem> applyFilter(@NotNull String line) {
        List<LinkResultItem> links = new ArrayList<>();
        for (HyperlinkFilter filter : myHyperlinkFilter) {
            LinkResult result = filter.apply(line);
            if (result != null) {
                links.addAll(result.getItems());
            }
        }
        return links;
    }
}
