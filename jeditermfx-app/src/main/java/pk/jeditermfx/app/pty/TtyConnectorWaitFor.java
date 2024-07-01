package pk.jeditermfx.app.pty;

import pk.jeditermfx.core.TtyConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.function.IntConsumer;

public class TtyConnectorWaitFor {

    private static final Logger logger = LoggerFactory.getLogger(TtyConnectorWaitFor.class);

    public TtyConnectorWaitFor(@NotNull TtyConnector ttyConnector, @NotNull ExecutorService executor,
                               @NotNull IntConsumer terminationCallback) {
        executor.submit(() -> {
            int exitCode = 0;
            try {
                while (true) {
                    try {
                        exitCode = ttyConnector.waitFor();
                        break;
                    } catch (InterruptedException e) {
                        logger.debug("", e);
                    }
                }
            } finally {
                terminationCallback.accept(exitCode);
            }
        });
    }
}
