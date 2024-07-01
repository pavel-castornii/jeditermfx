package pk.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import javafx.scene.input.KeyCombination;

public class TerminalActionPresentation {

    private final String myName;

    private final List<KeyCombination> myKeyCombinations;

    public TerminalActionPresentation(@NotNull String name, @NotNull KeyCombination keyCombination) {
        this(name, Collections.singletonList(keyCombination));
    }

    public TerminalActionPresentation(@NotNull String name, @NotNull List<KeyCombination> keyCombinations) {
        myName = name;
        myKeyCombinations = keyCombinations;
    }

    public @NotNull String getName() {
        return myName;
    }

    public @NotNull List<KeyCombination> getKeyCombinations() {
        return myKeyCombinations;
    }
}
