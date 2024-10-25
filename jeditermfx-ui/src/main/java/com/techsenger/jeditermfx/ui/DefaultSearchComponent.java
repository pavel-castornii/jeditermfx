package com.techsenger.jeditermfx.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

final class DefaultSearchComponent implements SearchComponent {

    private final HBox pane = new HBox();

    private final TextField myTextField = new TextField();

    private final Label label = new Label();

    private final CheckBox ignoreCaseCheckBox = new CheckBox("Ignore Case");

    private final List<SearchComponentListener> myListeners = new CopyOnWriteArrayList<>();

    private final SearchComponentListener myMulticaster = createMulticaster();

    public DefaultSearchComponent(JediTermFxWidget jediTermWidget) {
        this.ignoreCaseCheckBox.setSelected(true);
        Button next = createNextButton();
        next.setOnAction(e -> myMulticaster.selectNextFindResult());
        Button prev = createPrevButton();
        prev.setOnAction(e -> myMulticaster.selectPrevFindResult());
        var charSize = jediTermWidget.myTerminalPanel.myCharSize;
        HBox.setHgrow(myTextField, Priority.ALWAYS);
        pane.setMaxSize(
                charSize.getWidth() * 60,
                charSize.getHeight() + 14);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setPadding(new Insets(0, charSize.getWidth() / 2, 0, charSize.getWidth() / 2));
        pane.setStyle("-fx-background-color: -fx-background");
        myTextField.setEditable(true);
        updateLabel(null);
        this.pane.getChildren().add(myTextField);
        listenForChanges();
        this.pane.getChildren().add(ignoreCaseCheckBox);
        HBox.setMargin(ignoreCaseCheckBox, new Insets(0, charSize.getWidth(), 0, charSize.getWidth()));
        this.pane.getChildren().add(label);
        this.pane.getChildren().add(next);
        this.pane.getChildren().add(prev);
        this.pane.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                this.myTextField.requestFocus();
            }
        });
    }

    private void listenForChanges() {
        Runnable settingsChanged = () -> {
            myMulticaster.searchSettingsChanged(myTextField.getText(), ignoreCaseCheckBox.isSelected());
        };
        myTextField.textProperty().addListener((ov, oldV, newV) -> settingsChanged.run());
        ignoreCaseCheckBox.selectedProperty().addListener((ov, oldV, newV) -> settingsChanged.run());
    }

    private Button createNextButton() {
        return new Button("\u25BC");
    }

    private Button createPrevButton() {
        return new Button("\u25B2");
    }

    private void updateLabel(@Nullable SubstringFinder.FindResult result) {
        if (result == null) {
            label.setText("");
        } else if (!result.getItems().isEmpty()) {
            SubstringFinder.FindResult.FindItem selectedItem = result.selectedItem();
            label.setText(selectedItem.getIndex() + " of " + result.getItems().size());
        }
    }

    @Override
    public void onResultUpdated(SubstringFinder.@Nullable FindResult results) {
        updateLabel(results);
    }

    @Override
    public @NotNull Pane getPane() {
        return this.pane;
    }

    @Override
    public void addListener(@NotNull SearchComponentListener listener) {
        myListeners.add(listener);
    }

    public void requestFocus() {
        myTextField.requestFocus();
    }

    @Override
    public void addKeyPressedListener(@NotNull EventHandler<KeyEvent> listener) {
        myTextField.setOnKeyPressed(listener);
    }

    private @NotNull SearchComponentListener createMulticaster() {
        final Class<SearchComponentListener> listenerClass = SearchComponentListener.class;
        return (SearchComponentListener) Proxy.newProxyInstance(listenerClass.getClassLoader(), new Class[]{listenerClass}, (object, method, params) -> {
            for (SearchComponentListener listener : myListeners) {
                method.invoke(listener, params);
            }
            //noinspection SuspiciousInvocationHandlerImplementation
            return null;
        });
    }
}
