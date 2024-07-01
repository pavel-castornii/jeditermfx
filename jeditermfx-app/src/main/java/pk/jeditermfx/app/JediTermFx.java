package pk.jeditermfx.app;

import pk.jeditermfx.ui.DefaultHyperlinkFilter;
import pk.jeditermfx.app.pty.LoggingTtyConnector;
import pk.jeditermfx.app.pty.PtyProcessTtyConnector;
import pk.jeditermfx.core.TtyConnector;
import pk.jeditermfx.core.model.TerminalTextBuffer;
import pk.jeditermfx.ui.JediTermFxWidget;
import pk.jeditermfx.ui.settings.SettingsProvider;
import pk.jeditermfx.app.debug.TerminalDebugUtil;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import pk.jeditermfx.core.util.Platform;

public final class JediTermFx extends AbstractTerminalApplication {

    private static final Logger logger = LoggerFactory.getLogger(JediTermFx.class);

    @NotNull
    @Override
    public TtyConnector createTtyConnector() {
        try {
            var envs = configureEnvironmentVariables();
            String[] command;
            if (Platform.isWindows()) {
                command = new String[]{"powershell.exe"};
            } else {
                String shell = (String) envs.get("SHELL");
                if (shell == null) {
                    shell = "/bin/bash";
                }
                if (Platform.isMacOS()) {
                    command = new String[]{shell, "--login"};
                } else {
                    command = new String[]{shell};
                }
            }
            var workingDirectory = Path.of(".").toAbsolutePath().normalize().toString();
            logger.info("Starting {} in {}", String.join(" ", command), workingDirectory);
            var process = new PtyProcessBuilder()
                    .setDirectory(workingDirectory)
                    .setInitialColumns(120)
                    .setInitialRows(20)
                    .setCommand(command)
                    .setEnvironment(envs)
                    .setConsole(false)
                    .setUseWinConPty(true)
                    .start();
            return new LoggingPtyProcessTtyConnector(process, StandardCharsets.UTF_8, Arrays.asList(command));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final Map<String, String> configureEnvironmentVariables() {
        HashMap envs = new HashMap<String, String>(System.getenv());
        if (Platform.isMacOS()) {
            envs.put("LC_CTYPE", Charsets.UTF_8.name());
        }
        if (!Platform.isWindows()) {
            envs.put("TERM", "xterm-256color");
        }
        return envs;
    }

    @NotNull
    protected JediTermFxWidget createTerminalWidget(@NotNull SettingsProvider settingsProvider) {
        Intrinsics.checkNotNullParameter(settingsProvider, "settingsProvider");
        JediTermFxWidget widget = new JediTermFxWidget(settingsProvider);
        widget.addHyperlinkFilter(new DefaultHyperlinkFilter());
        return widget;
    }

    public static final class LoggingPtyProcessTtyConnector extends PtyProcessTtyConnector
            implements LoggingTtyConnector {

        private final int MAX_LOG_SIZE = 200;

        @NotNull
        private final LinkedList<char[]> myDataChunks = new LinkedList<>();

        @NotNull
        private final LinkedList<TerminalState> myStates = new LinkedList<>();

        @Nullable
        private JediTermFxWidget myWidget;

        private int logStart;

        public LoggingPtyProcessTtyConnector(@NotNull PtyProcess process, @NotNull Charset charset, @NotNull List command) {
            super(process, charset, command);
            Intrinsics.checkNotNullParameter(process, "process");
            Intrinsics.checkNotNullParameter(charset, "charset");
            Intrinsics.checkNotNullParameter(command, "command");
        }

        @Override
        public int read(@NotNull char[] buf, int offset, int length) throws IOException {
            Intrinsics.checkNotNullParameter(buf, "buf");
            int len = super.read(buf, offset, length);
            if (len > 0) {
                char[] arr = ArraysKt.copyOfRange(buf, offset, len);
                this.myDataChunks.add(arr);
                Intrinsics.checkNotNull(this.myWidget);
                TerminalTextBuffer terminalTextBuffer = this.myWidget.getTerminalTextBuffer();
                String lines = terminalTextBuffer.getScreenLines();
                Intrinsics.checkNotNull(terminalTextBuffer);
                LoggingTtyConnector.TerminalState terminalState =
                        new LoggingTtyConnector.TerminalState(lines, TerminalDebugUtil.getStyleLines(terminalTextBuffer),
                                terminalTextBuffer.getHistoryBuffer().getLines());
                this.myStates.add(terminalState);
                if (this.myDataChunks.size() > this.MAX_LOG_SIZE) {
                    this.myDataChunks.removeFirst();
                    this.myStates.removeFirst();
                    this.logStart++;
                }
            }
            return len;
        }

        @NotNull
        @Override
        public List<char[]> getChunks() {
            return new ArrayList(this.myDataChunks);
        }

        @NotNull
        @Override
        public List<TerminalState> getStates() {
            return new ArrayList(this.myStates);
        }

        @Override
        public int getLogStart() {
            return this.logStart;
        }

        @Override
        public void write(@NotNull String string) throws IOException {
            Intrinsics.checkNotNullParameter(string, "string");
            logger.debug("Writing in OutputStream : " + string);
            super.write(string);
        }

        @Override
        public void write(@NotNull byte[] bytes) throws IOException {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            logger.debug("Writing in OutputStream : " + Arrays.toString(bytes) + " " + new String(bytes, Charsets.UTF_8));
            super.write(bytes);
        }

        public final void setWidget(@NotNull JediTermFxWidget widget) {
            Intrinsics.checkNotNullParameter(widget, "widget");
            this.myWidget = widget;
        }
    }
}
