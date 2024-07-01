package pk.jeditermfx.core.util;

public enum Platform {

    Windows,

    macOS,

    Linux,

    Other;

    private static Platform CURRENT = detectCurrent();

    public static Platform current() {
        return CURRENT;
    }

    public static boolean isWindows() {
        return CURRENT == Windows;
    }

    public static boolean isMacOS() {
        return CURRENT == macOS;
    }

    private static Platform detectCurrent() {
        var osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) return Windows;
        if (osName.startsWith("mac")) return macOS;
        if (osName.startsWith("linux")) return Linux;
        return Other;
    }
}
