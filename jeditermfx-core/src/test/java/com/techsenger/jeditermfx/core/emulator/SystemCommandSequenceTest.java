package com.techsenger.jeditermfx.core.emulator;

import com.techsenger.jeditermfx.core.emulator.SystemCommandSequence;
import static com.techsenger.jeditermfx.core.util.Ascii.BEL_CHAR;
import static com.techsenger.jeditermfx.core.util.Ascii.ESC_CHAR;
import com.techsenger.jeditermfx.core.ArrayTerminalDataStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class SystemCommandSequenceTest {

    private static final String TWO_BYTES_TERMINATOR = ESC_CHAR + "\\";

    @Test
    public void basic() {
        assertArgs("foo" + BEL_CHAR, List.of("foo"));
    }

    @Test
    public void terminatedWithTwoBytes() {
        assertArgs("bar" + TWO_BYTES_TERMINATOR, List.of("bar"));
    }

    @Test
    public void parsedArgs() {
        assertArgs("0;My title" + BEL_CHAR, List.of("0", "My title"));
        assertArgs("0;My title;" + BEL_CHAR, List.of("0", "My title", ""));
        assertArgs(";0;My title" + BEL_CHAR, List.of("", "0", "My title"));
        assertArgs(";0;My title;" + BEL_CHAR, List.of("", "0", "My title", ""));
    }

    @Test
    public void formatUsingSameTerminator() {
        var seq1 = create("2;Test 1" + BEL_CHAR);
        assertEquals(ESC_CHAR + "]foo" + BEL_CHAR, seq1.format(List.of("foo")));
        var seq2 = create("2;Test 1" + TWO_BYTES_TERMINATOR);
        assertEquals(ESC_CHAR + "]bar;baz" + TWO_BYTES_TERMINATOR, seq2.format(List.of("bar", "baz")));
    }

    @SuppressWarnings("unused")
    @Test
    public void perfTest() {
        var args = 1000;
        List<String> list = new ArrayList<>(args);
        for (int i = 0; i < args; i++) {
            list.add("my arg");
        }
        String text = String.join(";", list) + BEL_CHAR;
        // warmup
        for (int i = 0; i < 100000; i++) {
            assertEquals(args, create(text).getArgs().size());
        }
        var startNano = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            assertEquals(args, create(text).getArgs().size());
        }
        Long elapsedTimeNano = System.nanoTime() - startNano;
        System.out.println("Elapsed Time: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNano) + " ms");
    }

    private SystemCommandSequence create(String text) {
        var dataStream = new ArrayTerminalDataStream(text.toCharArray());
        try {
            return new SystemCommandSequence(dataStream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void assertArgs(String text, List<String> expectedArgs) {
        var command = create(text);
        assertEquals(expectedArgs, command.getArgs());
    }
}
