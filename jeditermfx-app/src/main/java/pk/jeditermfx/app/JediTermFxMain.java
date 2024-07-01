package pk.jeditermfx.app;

import java.io.ByteArrayInputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public final class JediTermFxMain {

    public static final void main(@NotNull String[] arg) {
        Intrinsics.checkNotNullParameter(arg, "arg");
        configureJavaUtilLogging();
        Application.launch(JediTermFx.class, arg);
    }

    private static void configureJavaUtilLogging() {
        try {
            String format = "[%1$tF %1$tT] [%4\\$-7s] %5$s %n";
            var string = "java.util.logging.SimpleFormatter.format=" + format;
            byte[] bytes = string.getBytes(Charsets.UTF_8);
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(bytes));
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(handler);
            rootLogger.setLevel(Level.INFO);
        } catch (Exception ex) {
            LoggerFactory.getLogger(JediTermFxMain.class).error("Error configuring java util logging", ex);
        }
    }
}
