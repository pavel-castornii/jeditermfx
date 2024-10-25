package com.techsenger.jeditermfx.core;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface TerminalCustomCommandListener {

    void process(@NotNull List<String> args);
}
