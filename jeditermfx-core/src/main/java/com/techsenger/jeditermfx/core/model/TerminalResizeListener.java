package com.techsenger.jeditermfx.core.model;

import com.techsenger.jeditermfx.core.util.TermSize;

public interface TerminalResizeListener {

    void onResize(TermSize oldTermSize, TermSize newTermSize);
}
