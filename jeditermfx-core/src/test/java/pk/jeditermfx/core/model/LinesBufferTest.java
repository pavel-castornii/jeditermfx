package pk.jeditermfx.core.model;

import pk.jeditermfx.core.model.LinesBuffer;
import pk.jeditermfx.core.model.CharBuffer;
import pk.jeditermfx.core.TextStyle;
import pk.jeditermfx.core.util.CharBufferUtil;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * @author traff
 */
public class LinesBufferTest {

    public static final String LINE_1 = "Line 1";

    public static final String LINE_2 = "Line 2";

    public static final String LINE_3 = "Line 3";

    public static final String LINE_4 = "Line 4";

    @Test
    public void testMoveTopLines() {
        TextStyle style = new TextStyle();
        LinesBuffer scroll = createLinesBuffer();
        scroll.addNewLine(style, CharBufferUtil.create(LINE_1));
        scroll.addNewLine(style, CharBufferUtil.create(LINE_2));
        LinesBuffer text = createLinesBuffer();
        text.addNewLine(style, CharBufferUtil.create(LINE_3));
        text.addNewLine(style, CharBufferUtil.create(LINE_4));
        text.moveTopLinesTo(1, scroll);
        assertEquals(1, text.getLineCount());
        assertEquals(3, scroll.getLineCount());
        assertEquals("Line 1\n" +
                "Line 2\n" +
                "Line 3", scroll.getLines());
        assertEquals("Line 4", text.getLines());
    }

    @NotNull
    private LinesBuffer createLinesBuffer() {
        return new LinesBuffer(null);
    }

    @Test
    public void testMoveBottomLines() {
        TextStyle style = new TextStyle();
        LinesBuffer scroll = createLinesBuffer();
        scroll.addNewLine(style, CharBufferUtil.create(LINE_1));
        scroll.addNewLine(style, CharBufferUtil.create(LINE_2));
        scroll.addNewLine(style, CharBufferUtil.create(LINE_3));
        LinesBuffer text = createLinesBuffer();
        text.addNewLine(style, CharBufferUtil.create(LINE_4));
        scroll.moveBottomLinesTo(2, text);
        assertEquals(3, text.getLineCount());
        assertEquals(1, scroll.getLineCount());
        assertEquals("Line 1", scroll.getLines());
        assertEquals("Line 2\n" +
                "Line 3\n" +
                "Line 4", text.getLines());
    }

    @Test
    public void testRemoveFirstLines() {
        TextStyle style = new TextStyle();
        LinesBuffer text = createLinesBuffer();
        text.addNewLine(style, CharBufferUtil.create(LINE_1));
        text.addNewLine(style, CharBufferUtil.create(LINE_2));
        text.addNewLine(style, CharBufferUtil.create(LINE_3));
        text.addNewLine(style, CharBufferUtil.create(LINE_4));
        text.removeTopLines(3);
        assertEquals(1, text.getLineCount());
        assertEquals("Line 4", text.getLines());
    }

    @Test
    public void testWriteToLineBuffer() {
        LinesBuffer buf = createLinesBuffer();
        buf.writeString(3, 2, new CharBuffer("Hi!"), TextStyle.EMPTY);
        assertEquals("\n" +
                "\n" +
                "   Hi!", buf.getLines());
        buf.writeString(1, 1, new CharBuffer("*****"), TextStyle.EMPTY);
        assertEquals("\n" +
                " *****\n" +
                "   Hi!", buf.getLines());
        buf.writeString(3, 1, new CharBuffer("+"), TextStyle.EMPTY);
        assertEquals("\n" +
                " **+**\n" +
                "   Hi!", buf.getLines());
        buf.writeString(4, 1, new CharBuffer("***"), TextStyle.EMPTY);
        assertEquals("\n" +
                " **+***\n" +
                "   Hi!", buf.getLines());
        buf.writeString(8, 1, new CharBuffer("="), TextStyle.EMPTY);
        assertEquals("\n" +
                " **+*** =\n" +
                "   Hi!", buf.getLines());
    }
}
