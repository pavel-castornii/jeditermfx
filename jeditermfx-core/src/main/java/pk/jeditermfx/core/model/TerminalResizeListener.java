package pk.jeditermfx.core.model;

import pk.jeditermfx.core.util.TermSize;

public interface TerminalResizeListener {

    void onResize(TermSize oldTermSize, TermSize newTermSize);
}
