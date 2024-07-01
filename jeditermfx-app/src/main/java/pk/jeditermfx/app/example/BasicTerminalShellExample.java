package pk.jeditermfx.app.example;

import pk.jeditermfx.app.pty.PtyProcessTtyConnector;
import pk.jeditermfx.core.TtyConnector;
import pk.jeditermfx.ui.JediTermFxWidget;
import pk.jeditermfx.ui.settings.DefaultSettingsProvider;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pk.jeditermfx.ui.DefaultHyperlinkFilter;
import pk.jeditermfx.core.util.Platform;

public class BasicTerminalShellExample extends Application {

    private static final Logger logger = LoggerFactory.getLogger(BasicTerminalShellExample.class);

    private @NotNull JediTermFxWidget createTerminalWidget() {
        JediTermFxWidget widget = new JediTermFxWidget(80, 24, new DefaultSettingsProvider());
        widget.setTtyConnector(createTtyConnector());
        widget.addHyperlinkFilter(new DefaultHyperlinkFilter());
        widget.start();
        return widget;
    }

    private @NotNull TtyConnector createTtyConnector() {
        try {
            Map<String, String> envs = System.getenv();
            String[] command;
            if (Platform.isWindows()) {
                command = new String[]{"cmd.exe"};
            } else {
                command = new String[]{"/bin/bash", "--login"};
                envs = new HashMap<>(System.getenv());
                envs.put("TERM", "xterm-256color");
            }
            PtyProcess process = new PtyProcessBuilder().setCommand(command).setEnvironment(envs).start();
            return new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void start(Stage stage) {
        JediTermFxWidget widget = createTerminalWidget();
        widget.addListener(terminalWidget -> {
            widget.close(); // terminate the current process and dispose all allocated resources
            logger.debug("Closed widget");
        });
        stage.setTitle("Basic Terminal Shell Example");
        stage.setOnCloseRequest(e -> {
            widget.close();
            widget.getTtyConnector().close(); // terminate the current process
            logger.debug("Closed TTY connector");
        });
        Scene scene = new Scene(widget.getPane(), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
