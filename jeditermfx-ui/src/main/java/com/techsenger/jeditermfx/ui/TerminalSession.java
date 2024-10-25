package com.techsenger.jeditermfx.ui;

import com.techsenger.jeditermfx.core.Terminal;
import com.techsenger.jeditermfx.core.TtyConnector;
import com.techsenger.jeditermfx.core.model.TerminalTextBuffer;

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
