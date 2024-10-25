package com.techsenger.jeditermfx.app.debug;


public final class ControlSequenceSettings {

    private final boolean showChunkId;

    private final boolean useTeseq;

    private final boolean showInvisibleCharacters;

    private final boolean wrapLines;

    public ControlSequenceSettings(boolean showChunkId, boolean useTeseq,
                                   boolean showInvisibleCharacters, boolean wrapLines) {
        this.showChunkId = showChunkId;
        this.useTeseq = useTeseq;
        this.showInvisibleCharacters = showInvisibleCharacters;
        this.wrapLines = wrapLines;
    }

    public final boolean isShowChunkId() {
        return this.showChunkId;
    }

    public final boolean isUseTeseq() {
        return this.useTeseq;
    }

    public final boolean isShowInvisibleCharacters() {
        return this.showInvisibleCharacters;
    }

    public final boolean isWrapLines() {
        return this.wrapLines;
    }
}