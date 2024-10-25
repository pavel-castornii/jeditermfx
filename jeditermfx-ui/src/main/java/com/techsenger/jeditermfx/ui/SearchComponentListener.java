package com.techsenger.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;

public interface SearchComponentListener {

    void searchSettingsChanged(@NotNull String textToFind, boolean ignoreCase);

    void hideSearchComponent();

    void selectNextFindResult();

    void selectPrevFindResult();
}
