package pk.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;

public interface TerminalActionMenuBuilder {

    void addAction(@NotNull TerminalAction action);

    void addSeparator();
}
