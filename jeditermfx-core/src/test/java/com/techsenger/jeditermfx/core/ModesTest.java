package com.techsenger.jeditermfx.core;

import com.techsenger.jeditermfx.core.TerminalMode;
import com.techsenger.jeditermfx.core.model.JediTerminal;
import com.techsenger.jeditermfx.core.model.StyleState;
import com.techsenger.jeditermfx.core.model.TerminalTextBuffer;
import com.techsenger.jeditermfx.core.util.BackBufferDisplay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ModesTest {

    @Test
    public void testAutoWrap() {
        StyleState state = new StyleState();
        TerminalTextBuffer terminalTextBuffer = new TerminalTextBuffer(10, 3, state);
        JediTerminal terminal = new JediTerminal(new BackBufferDisplay(terminalTextBuffer), terminalTextBuffer, state);
        terminal.setModeEnabled(TerminalMode.AutoWrap, false);
        //                             1234567890123456789
        terminal.writeUnwrappedString("this is a long line");
        assertEquals("long line \n" +
                "          \n" +
                "          \n", terminalTextBuffer.getScreenLines());
        assertEquals(10, terminal.getCursorX());
        assertEquals(1, terminal.getCursorY());
        terminal.cursorPosition(1, 1);
        terminal.setModeEnabled(TerminalMode.AutoWrap, true);
        //                             1234567890123456789
        terminal.writeUnwrappedString("this is a long line");
        assertEquals("this is a \n" +
                "long line \n" +
                "          \n", terminalTextBuffer.getScreenLines());
        assertEquals(10, terminal.getCursorX());
        assertEquals(2, terminal.getCursorY());
    }

}
