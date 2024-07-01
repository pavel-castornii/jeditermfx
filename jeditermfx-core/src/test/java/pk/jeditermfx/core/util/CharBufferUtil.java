package pk.jeditermfx.core.util;

import pk.jeditermfx.core.model.CharBuffer;
import org.jetbrains.annotations.TestOnly;

/**
 * @author traff
 */
public class CharBufferUtil {

    @TestOnly
    public static CharBuffer create(String str) {
        char[] buf = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            buf[i] = str.charAt(i);
        }
        return new CharBuffer(buf, 0, buf.length);
    }
}
