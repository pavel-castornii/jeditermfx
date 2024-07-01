package pk.jeditermfx.core.model;

import pk.jeditermfx.core.util.CellPosition;

public class TerminalResizeResult {

    private final CellPosition newCursor;

    TerminalResizeResult(CellPosition newCursor) {
        this.newCursor = newCursor;
    }

    public CellPosition getNewCursor() {
        return newCursor;
    }
}
