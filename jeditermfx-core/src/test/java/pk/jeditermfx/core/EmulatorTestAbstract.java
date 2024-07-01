package pk.jeditermfx.core;

import pk.jeditermfx.core.model.TerminalTextBuffer;
import pk.jeditermfx.core.util.TestSession;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.TestInfo;

/**
 * @author traff
 */
public abstract class EmulatorTestAbstract {

    protected static void assertColor(TextStyle style, TerminalColor foreground, TerminalColor background) {
        assertEquals(foreground, style.getForeground());
        assertEquals(background, style.getBackground());
    }

    protected TerminalTextBuffer doTest(TestInfo testInfo) throws IOException {
        return doTest(testInfo, 80, 24);
    }

    protected TerminalTextBuffer doTest(TestInfo testInfo, int width, int height) throws IOException {
        String content = Files.readString(Path.of(getPathToTest(testInfo) + ".after.txt"), StandardCharsets.UTF_8);
        // CRLF in test data files should be handled as LF
        return doTest(testInfo, width, height, content.replaceAll("\r\n", "\n").replaceAll("\r", "\n"));
    }

    protected TerminalTextBuffer doTest(TestInfo testInfo, int width, int height, String expected) throws IOException {
        TestSession testSession = new TestSession(width, height);
        String text = Files.readString(Path.of(getPathToTest(testInfo) + ".txt"), StandardCharsets.UTF_8);
        // LF in test data files should be handled as CRLF to emulate a tty stream
        String crlfText = text.replaceAll("\r?\n", "\r\n");
        testSession.process(crlfText);
        TerminalTextBuffer terminalTextBuffer = testSession.getTerminal().getTextBuffer();
        assertEquals(expected, terminalTextBuffer.getScreenLines());
        return terminalTextBuffer;
    }

    protected abstract @NotNull Path getPathToTest(TestInfo testInfo);
}
