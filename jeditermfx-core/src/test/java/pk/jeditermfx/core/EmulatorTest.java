package pk.jeditermfx.core;

import pk.jeditermfx.core.emulator.ColorPalette;
import pk.jeditermfx.core.model.TerminalTextBuffer;
import pk.jeditermfx.core.util.TestSession;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @author traff
 */
public class EmulatorTest extends EmulatorTestAbstract {

    @Test
    public void testSetCursorPosition(TestInfo testInfo) throws IOException {
        doTest(testInfo, 3, 4, "X00\n" +  //X wins
                "0X \n" +
                "X X\n" +
                "   \n");
    }

    //public void testTooLargeScrollRegion() throws IOException { TODO: this test fails on Travis somehow
    //  doTest(80, 24);
    //}

    @Test
    public void testMidnightCommanderOnVT100(TestInfo testInfo) throws IOException {
        doTest(testInfo);
    }

    @Test
    public void testMidnightCommanderOnXTerm(TestInfo testInfo) throws IOException {
        TerminalTextBuffer terminalTextBuffer = doTest(testInfo);
        assertColor(terminalTextBuffer.getStyleAt(8, 2), ColorPalette.getIndexedTerminalColor(3),
                ColorPalette.getIndexedTerminalColor(4));
        assertColor(terminalTextBuffer.getStyleAt(23, 4), ColorPalette.getIndexedTerminalColor(7),
                ColorPalette.getIndexedTerminalColor(4));
        assertColor(terminalTextBuffer.getStyleAt(2, 0), ColorPalette.getIndexedTerminalColor(0),
                ColorPalette.getIndexedTerminalColor(6));
    }

    @Test
    public void testEraseBeyondTerminalWidth(TestInfo testInfo) throws IOException {
        doTest(testInfo);
    }

    @Test
    public void testSystemCommands(TestInfo testInfo) throws IOException {
        doTest(testInfo, 30, 3);
    }

    @Test
    public void testOscSetTitle() throws IOException {
        TestSession session = new TestSession(30, 3);
        session.process("\u001B]0;Title A\u001B\\Done1 ");
        assertEquals("Title A", session.getDisplay().getWindowTitle());
        session.process("\u001B]1;Title B\u001B\\Done2 ");
        assertEquals("Title B", session.getDisplay().getWindowTitle());
        session.process("\u001B]2;Title C\u001B\\Done3");
        assertEquals("Title C", session.getDisplay().getWindowTitle());
        assertEquals("Done1 Done2 Done3", session.getTerminal().getTextBuffer().getScreenLines().trim());
    }

    @Test
    public void testOsc10Query() throws IOException {
        TestSession session = new TestSession(10, 10);
        session.getDisplay().setWindowForeground(new Color(16, 15, 14));
        session.process("\u001B]10;?\7");
        assertEquals("\033]10;rgb:1010/0f0f/0e0e\7", session.getTerminal().getOutputAndClear());
        session.process("\u001B]10;?\u001B\\");
        assertEquals("\033]10;rgb:1010/0f0f/0e0e\u001B\\", session.getTerminal().getOutputAndClear());
    }

    @Test
    public void testOsc11Query() throws IOException {
        TestSession session = new TestSession(10, 10);
        session.getDisplay().setWindowBackground(new Color(16, 15, 14));
        session.process("\u001B]11;?\7");
        assertEquals("\033]11;rgb:1010/0f0f/0e0e\7", session.getTerminal().getOutputAndClear());
        session.process("\u001B]11;?\u001B\\");
        assertEquals("\033]11;rgb:1010/0f0f/0e0e\u001B\\", session.getTerminal().getOutputAndClear());
    }

    @Test
    public void testResetToInitialState() throws IOException {
        TestSession session = new TestSession(20, 4);
        for (int i = 1; i <= 9; i++) {
            if (i > 1) {
                session.process("\r\n");
            }
            session.process("foo " + i);
        }
        assertScreenLines(session, List.of("foo 6", "foo 7", "foo 8", "foo 9"));
        assertHistoryLines(session, List.of("foo 1", "foo 2", "foo 3", "foo 4", "foo 5"));
        session.process(esc("c"));
        assertScreenLines(session, List.of(""));
        assertHistoryLines(session, List.of());
    }

    @Test
    public void testSoftReset() throws IOException {
        TestSession session = new TestSession(20, 4);
        for (int i = 1; i <= 9; i++) {
            if (i > 1) {
                session.process("\r\n");
            }
            session.process("foo " + i);
        }
        assertScreenLines(session, List.of("foo 6", "foo 7", "foo 8", "foo 9"));
        assertHistoryLines(session, List.of("foo 1", "foo 2", "foo 3", "foo 4", "foo 5"));
        session.process(csi("!p"));
        assertScreenLines(session, List.of(""));
        assertHistoryLines(session, List.of("foo 1", "foo 2", "foo 3", "foo 4", "foo 5"));
    }

    @Test
    public void testEraseInDisplay3() throws IOException {
        TestSession session = new TestSession(20, 2);
        for (int i = 1; i <= 5; i++) {
            if (i > 1) {
                session.process("\r\n");
            }
            session.process("foo " + i);
        }
        assertScreenLines(session, List.of("foo 4", "foo 5"));
        assertHistoryLines(session, List.of("foo 1", "foo 2", "foo 3"));
        session.process(csi("3J"));
        assertScreenLines(session, List.of("", ""));
        assertHistoryLines(session, List.of());
    }

    private void assertScreenLines(@NotNull TestSession session, @NotNull List<String> expectedScreenLines) {
        assertEquals(expectedScreenLines, session.getTerminalTextBuffer().getScreenBuffer().getLineTexts());
    }

    private void assertHistoryLines(@NotNull TestSession session, @NotNull List<String> expectedHistoryLines) {
        assertEquals(expectedHistoryLines, session.getTerminalTextBuffer().getHistoryBuffer().getLineTexts());
    }

    @SuppressWarnings("SameParameterValue")
    private static @NotNull String esc(@NotNull String string) {
        return "\u001B" + string;
    }

    @SuppressWarnings("SameParameterValue")
    private static @NotNull String csi(@NotNull String string) {
        return "\u001B[" + string;
    }

    @Override
    protected @NotNull Path getPathToTest(TestInfo testInfo) {
        return TestPathsManager.getTestDataPath().resolve(testInfo.getTestMethod().get().getName());
    }
}
