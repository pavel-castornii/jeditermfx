package pk.jeditermfx.ui;

import pk.jeditermfx.core.Color;
import pk.jeditermfx.core.TerminalColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class FxTransformers {

    @Contract(value = "null -> null; !null -> new", pure = true)
    public static @Nullable javafx.scene.paint.Color toFxColor(@Nullable Color color) {
        return color == null ? null : javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(),
                color.getAlpha() / 255.0);
    }

    @Contract("null -> null; !null -> new")
    public static @Nullable Color fromFxColor(@Nullable javafx.scene.paint.Color color) {
        return color == null ? null : new Color(
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255),
                (int) Math.round(color.getOpacity() * 255));
    }

    @Contract("null -> null; !null -> new")
    public static @Nullable TerminalColor fromFxToTerminalColor(@Nullable javafx.scene.paint.Color color) {
        return color == null ? null : TerminalColor.fromColor(fromFxColor(color));
    }
}
