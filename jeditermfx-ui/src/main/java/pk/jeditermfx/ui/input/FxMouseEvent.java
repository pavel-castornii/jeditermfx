package pk.jeditermfx.ui.input;

import javafx.scene.input.MouseButton;
import pk.jeditermfx.core.input.MouseEvent;
import pk.jeditermfx.core.emulator.mouse.MouseButtonCodes;
import pk.jeditermfx.core.emulator.mouse.MouseButtonModifierFlags;
import org.jetbrains.annotations.NotNull;

public final class FxMouseEvent extends MouseEvent {

    private static int createButtonCode(@NotNull javafx.scene.input.MouseEvent fxMouseEvent) {
        // for mouse dragged, button is stored in modifiers
        if (fxMouseEvent.getButton() == MouseButton.PRIMARY) {
            return MouseButtonCodes.LEFT;
        } else if (fxMouseEvent.getButton() == MouseButton.MIDDLE) {
            return MouseButtonCodes.MIDDLE;
        } else if (fxMouseEvent.getButton() == MouseButton.SECONDARY) {
            return MouseButtonCodes.NONE; //we don't handle right mouse button as it used for the context menu invocation
        }
        return MouseButtonCodes.NONE;
    }

    private static int getModifierKeys(@NotNull javafx.scene.input.MouseEvent fxMouseEvent) {
        int modifier = 0;
        if (fxMouseEvent.isControlDown()) {
            modifier |= MouseButtonModifierFlags.MOUSE_BUTTON_CTRL_FLAG;
        }
        if (fxMouseEvent.isShiftDown()) {
            modifier |= MouseButtonModifierFlags.MOUSE_BUTTON_SHIFT_FLAG;
        }
        if (fxMouseEvent.isMetaDown()) {
            modifier |= MouseButtonModifierFlags.MOUSE_BUTTON_META_FLAG;
        }
        return modifier;
    }

    private final javafx.scene.input.MouseEvent myFxMouseEvent;

    public FxMouseEvent(@NotNull javafx.scene.input.MouseEvent fxMouseEvent) {
        super(createButtonCode(fxMouseEvent), getModifierKeys(fxMouseEvent));
        myFxMouseEvent = fxMouseEvent;
    }

    @Override
    public String toString() {
        return myFxMouseEvent.toString();
    }
}
