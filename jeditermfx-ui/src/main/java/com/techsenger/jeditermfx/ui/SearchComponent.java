package com.techsenger.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public interface SearchComponent {

    @NotNull
    Pane getPane();

    void addListener(@NotNull SearchComponentListener listener);

    void addKeyPressedListener(@NotNull EventHandler<KeyEvent> listener);

    void onResultUpdated(@Nullable SubstringFinder.FindResult results);
}
