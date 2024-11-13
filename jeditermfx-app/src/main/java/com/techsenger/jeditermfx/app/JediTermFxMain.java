package com.techsenger.jeditermfx.app;

import javafx.application.Application;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class JediTermFxMain {

    public static final void main(@NotNull String[] arg) {
        Intrinsics.checkNotNullParameter(arg, "arg");
        Application.launch(JediTermFx.class, arg);
    }
}
