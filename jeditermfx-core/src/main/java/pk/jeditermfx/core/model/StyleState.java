package pk.jeditermfx.core.model;

import pk.jeditermfx.core.TerminalColor;
import pk.jeditermfx.core.TextStyle;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class StyleState {

    private volatile TextStyle myCurrentStyle = TextStyle.EMPTY;

    private volatile TextStyle myDefaultStyle = TextStyle.EMPTY;

    public StyleState() {
    }

    public @NotNull TextStyle getCurrent() {
        return myCurrentStyle;
    }

    public void reset() {
        myCurrentStyle = myDefaultStyle;
    }

    public void setDefaultStyle(@NotNull TextStyle defaultStyle) {
        myDefaultStyle = defaultStyle;
    }

    public @NotNull TerminalColor getDefaultBackground() {
        return Objects.requireNonNull(myDefaultStyle.getBackground());
    }

    public @NotNull TerminalColor getDefaultForeground() {
        return Objects.requireNonNull(myDefaultStyle.getForeground());
    }

    public void setCurrent(@NotNull TextStyle current) {
        myCurrentStyle = current;
    }
}
