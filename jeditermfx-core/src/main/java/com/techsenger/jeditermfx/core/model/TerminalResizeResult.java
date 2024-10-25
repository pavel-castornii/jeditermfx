package com.techsenger.jeditermfx.core.model;

import com.techsenger.jeditermfx.core.util.CellPosition;

public class TerminalResizeResult {

    private final CellPosition newCursor;

    TerminalResizeResult(CellPosition newCursor) {
        this.newCursor = newCursor;
    }

    public CellPosition getNewCursor() {
        return newCursor;
    }
}
