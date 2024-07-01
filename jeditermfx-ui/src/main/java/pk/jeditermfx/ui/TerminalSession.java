package pk.jeditermfx.ui;

import pk.jeditermfx.core.Terminal;
import pk.jeditermfx.core.TtyConnector;
import pk.jeditermfx.core.model.TerminalTextBuffer;

/**
 * @author traff
 */
public interface TerminalSession {

    void start();

    TerminalTextBuffer getTerminalTextBuffer();

    Terminal getTerminal();

    TtyConnector getTtyConnector();

    void close();
}
