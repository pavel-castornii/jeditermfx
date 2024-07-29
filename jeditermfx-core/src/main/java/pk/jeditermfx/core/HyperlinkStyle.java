package pk.jeditermfx.core;

import pk.jeditermfx.core.model.hyperlinks.LinkInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author traff
 */
public class HyperlinkStyle extends TextStyle {

    @NotNull
    private final LinkInfo myLinkInfo;

    @NotNull
    private final TextStyle myCustomStyle;

    @Nullable
    private final TextStyle myPrevTextStyle;

    @NotNull
    private final HighlightMode myHighlightMode;

    @Nullable
    private final TextStyle myOriginalStyle;

    public HyperlinkStyle(@NotNull TextStyle prevTextStyle, @NotNull LinkInfo hyperlinkInfo) {
        this(prevTextStyle.getForeground(), prevTextStyle.getBackground(), hyperlinkInfo,
                HighlightMode.HOVER_WITH_CUSTOM_COLOR, prevTextStyle, null);
    }

    public HyperlinkStyle(@Nullable TerminalColor foreground, @Nullable TerminalColor background,
                          @NotNull LinkInfo hyperlinkInfo, @NotNull HighlightMode mode,
                          @Nullable TextStyle prevTextStyle, @Nullable TextStyle originalStyle) {
        this(false, foreground, background, hyperlinkInfo, mode, prevTextStyle, originalStyle);
    }

    private HyperlinkStyle(boolean keepColors, @Nullable TerminalColor foreground,
                           @Nullable TerminalColor background, @NotNull LinkInfo hyperlinkInfo,
                           @NotNull HighlightMode mode, @Nullable TextStyle prevTextStyle,
                           @Nullable TextStyle originalStyle) {
        super(keepColors ? foreground : null, keepColors ? background : null);
        myCustomStyle = new TextStyle.Builder()
                .setBackground(background)
                .setForeground(foreground)
                .setOption(Option.UNDERLINED, true)
                .build();
        myLinkInfo = hyperlinkInfo;
        myHighlightMode = mode;
        myPrevTextStyle = prevTextStyle;
        myOriginalStyle = originalStyle;
    }

    @Nullable
    public TextStyle getPrevTextStyle() {
        return myPrevTextStyle;
    }

    @Nullable
    public TextStyle getOriginalStyle() {
        return myOriginalStyle;
    }

    @NotNull
    public TextStyle getCustomStyle() {
        return myCustomStyle;
    }

    @NotNull
    public LinkInfo getLinkInfo() {
        return myLinkInfo;
    }

    @NotNull
    public HighlightMode getHighlightMode() {
        return myHighlightMode;
    }

    @NotNull
    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public enum HighlightMode {

        ALWAYS_WITH_ORIGINAL_COLOR(true),

        ALWAYS_WITH_CUSTOM_COLOR(false),

        NEVER_WITH_ORIGINAL_COLOR(true),

        /**
         * It requires original to resolve if it is required to underline.
         */
        NEVER_WITH_CUSTOM_COLOR(true),

        HOVER_WITH_ORIGINAL_COLOR(true),

        HOVER_WITH_CUSTOM_COLOR(false),

        HOVER_WITH_BOTH_COLORS(true);

        private final boolean originalColorUsed;

        private HighlightMode(boolean originalColorUsed) {
            this.originalColorUsed = originalColorUsed;
        }

        public boolean isOriginalColorUsed() {
            return originalColorUsed;
        }
    }

    public static class Builder extends TextStyle.Builder {

        @NotNull
        private LinkInfo myLinkInfo;

        @NotNull
        private TextStyle myHighlightStyle;

        @Nullable
        private TextStyle myPrevTextStyle;

        @NotNull
        private HighlightMode myHighlightMode;

        private Builder(@NotNull HyperlinkStyle style) {
            myLinkInfo = style.myLinkInfo;
            myHighlightStyle = style.myCustomStyle;
            myPrevTextStyle = style.myPrevTextStyle;
            myHighlightMode = style.myHighlightMode;
        }

        @NotNull
        public HyperlinkStyle build() {
            return build(false);
        }

        @NotNull
        public HyperlinkStyle build(boolean keepColors) {
            TerminalColor foreground = myHighlightStyle.getForeground();
            TerminalColor background = myHighlightStyle.getBackground();
            if (keepColors) {
                TextStyle style = super.build();
                foreground = style.getForeground() != null ? style.getForeground() : myHighlightStyle.getForeground();
                background = style.getBackground() != null ? style.getBackground() : myHighlightStyle.getBackground();
            }
            return new HyperlinkStyle(keepColors, foreground, background, myLinkInfo, myHighlightMode,
                    myPrevTextStyle, null);
        }
    }
}
