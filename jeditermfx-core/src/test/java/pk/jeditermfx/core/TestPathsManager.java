package pk.jeditermfx.core;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

/**
 * @author traff
 */
public class TestPathsManager {

    public static @NotNull Path getTestDataPath() {
        return Path.of("src/test/resources/testData").toAbsolutePath().normalize();
    }
}
