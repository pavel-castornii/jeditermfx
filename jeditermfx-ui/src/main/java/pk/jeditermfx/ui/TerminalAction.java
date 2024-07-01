package pk.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author traff
 */
public class TerminalAction {

    private final TerminalActionPresentation myPresentation;

    private final Predicate<KeyEvent> myRunnable;

    private Supplier<Boolean> myEnabledSupplier = () -> true;

    private KeyCode myMnemonicKeyCode = null;

    private boolean mySeparatorBefore = false;

    private boolean myHidden = false;

    public TerminalAction(@NotNull TerminalActionPresentation presentation, @NotNull Predicate<KeyEvent> runnable) {
        myPresentation = presentation;
        myRunnable = runnable;
    }

    public TerminalAction(@NotNull TerminalActionPresentation presentation) {
        this(presentation, keyEvent -> true);
    }

    public @NotNull TerminalActionPresentation getPresentation() {
        return myPresentation;
    }

    public @Nullable KeyCode getMnemonicKeyCode() {
        return myMnemonicKeyCode;
    }

    public boolean matches(KeyEvent e) {
        for (KeyCombination kc : myPresentation.getKeyCombinations()) {
            if (kc.match(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnabled(@Nullable KeyEvent e) {
        return myEnabledSupplier.get();
    }

    public boolean actionPerformed(@Nullable KeyEvent e) {
        return myRunnable.test(e);
    }

    public static boolean processEvent(@NotNull TerminalActionProvider actionProvider, @NotNull KeyEvent e) {
        for (TerminalAction a : actionProvider.getActions()) {
            if (a.matches(e)) {
                return a.isEnabled(e) && a.actionPerformed(e);
            }
        }
        if (actionProvider.getNextProvider() != null) {
            return processEvent(actionProvider.getNextProvider(), e);
        }
        return false;
    }

    public @NotNull String getName() {
        return myPresentation.getName();
    }

    public TerminalAction withMnemonicKey(KeyCode keyCode) {
        myMnemonicKeyCode = keyCode;
        return this;
    }

    public TerminalAction withEnabledSupplier(@NotNull Supplier<Boolean> enabledSupplier) {
        myEnabledSupplier = enabledSupplier;
        return this;
    }

    public TerminalAction separatorBefore(boolean enabled) {
        mySeparatorBefore = enabled;
        return this;
    }

    private @NotNull MenuItem toMenuItem() {
        var itemText = myPresentation.getName();
        if (myMnemonicKeyCode != null) {
            var key = myMnemonicKeyCode.getName();
            itemText = itemText.replace(key, "_" + key);
        }
        MenuItem menuItem = new MenuItem(itemText);
        if (!myPresentation.getKeyCombinations().isEmpty()) {
            menuItem.setAccelerator(myPresentation.getKeyCombinations().get(0));
        }
        menuItem.setOnAction(actionEvent -> actionPerformed(null));
        menuItem.setDisable(!isEnabled(null));
        return menuItem;
    }

    public boolean isSeparated() {
        return mySeparatorBefore;
    }

    public boolean isHidden() {
        return myHidden;
    }

    public TerminalAction withHidden(boolean hidden) {
        myHidden = hidden;
        return this;
    }

    @Override
    public String toString() {
        return "'" + myPresentation.getName() + "'";
    }

    public static void fillMenu(@NotNull ContextMenu menu, @NotNull TerminalActionProvider actionProvider) {
        buildMenu(actionProvider, new TerminalActionMenuBuilder() {

            @Override
            public void addAction(@NotNull TerminalAction action) {
                menu.getItems().add(action.toMenuItem());
            }

            @Override
            public void addSeparator() {
                menu.getItems().add(new SeparatorMenuItem());
            }
        });
    }

    public static void buildMenu(@NotNull TerminalActionProvider provider, @NotNull TerminalActionMenuBuilder builder) {
        List<TerminalActionProvider> actionProviders = listActionProviders(provider);
        boolean emptyGroup = true;
        for (TerminalActionProvider actionProvider : actionProviders) {
            boolean addSeparator = !emptyGroup;
            emptyGroup = true;
            for (TerminalAction action : actionProvider.getActions()) {
                if (action.isHidden()) continue;
                if (addSeparator || action.isSeparated()) {
                    builder.addSeparator();
                    addSeparator = false;
                }
                builder.addAction(action);
                emptyGroup = false;
            }
        }
    }

    private static @NotNull List<TerminalActionProvider> listActionProviders(@NotNull TerminalActionProvider provider) {
        var providers = new ArrayList<TerminalActionProvider>();
        for (var p = provider; p != null; p = p.getNextProvider()) {
            providers.add(0, p);
        }
        return providers;
    }
}
