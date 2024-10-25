package com.techsenger.jeditermfx.core;

import com.techsenger.jeditermfx.core.TextStyle;
import com.techsenger.jeditermfx.core.TerminalColor;
import com.techsenger.jeditermfx.core.ArrayTerminalDataStream;
import com.techsenger.jeditermfx.core.emulator.Emulator;
import com.techsenger.jeditermfx.core.emulator.JediEmulator;
import com.techsenger.jeditermfx.core.model.JediTerminal;
import com.techsenger.jeditermfx.core.model.StyleState;
import com.techsenger.jeditermfx.core.model.TerminalTextBuffer;
import com.techsenger.jeditermfx.core.util.BackBufferDisplay;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author traff
 */
public class StyledTextTest {

    private static final String CSI = "" + ((char) 27) + "[";

    private static final TextStyle GREEN = new TextStyle(TerminalColor.index(2), null);

    private static final TextStyle BLACK = new TextStyle(TerminalColor.BLACK, null);

    @Test
    public void test24BitForegroundColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "38;2;0;128;0mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 128, 0), style.getForeground());
    }

    @Test
    public void test24BitBackgroundColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "48;2;0;128;0mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 128, 0), style.getBackground());
    }

    @Test
    public void test24BitCombinedColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "0;38;2;0;128;0;48;2;0;255;0;1mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 128, 0), style.getForeground());
        assertEquals(new TerminalColor(0, 255, 0), style.getBackground());
        assertTrue(style.hasOption(TextStyle.Option.BOLD));
    }

    @Test
    public void testIndexedForegroundColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "38;5;46mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 255, 0), style.getForeground());
    }

    @Test
    public void testIndexedBackgroundColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "48;5;46mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 255, 0), style.getBackground());
    }

    @Test
    public void testIndexedCombinedColourParsing() throws IOException {
        TerminalTextBuffer terminalTextBuffer = getBufferFor(12, 1, CSI + "0;38;5;46;48;5;196;1mHello");
        TextStyle style = terminalTextBuffer.getStyleAt(0, 0);
        assertEquals(new TerminalColor(0, 255, 0), style.getForeground());
        assertEquals(new TerminalColor(255, 0, 0), style.getBackground());
        assertTrue(style.hasOption(TextStyle.Option.BOLD));
    }

    private @NotNull TerminalTextBuffer getBufferFor(int width, int height, String content) throws IOException {
        StyleState state = new StyleState();
        TerminalTextBuffer terminalTextBuffer = new TerminalTextBuffer(width, height, state);
        JediTerminal terminal = new JediTerminal(new BackBufferDisplay(terminalTextBuffer), terminalTextBuffer, state);
        Emulator emulator = new JediEmulator(new ArrayTerminalDataStream(content.toCharArray()), terminal);
        while (emulator.hasNext()) {
            emulator.next();
        }
        return terminalTextBuffer;
    }
}
