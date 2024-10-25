package com.techsenger.jeditermfx.core;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.TestInfo;

/**
 * @author traff
 */
public class VtEmulatorTest extends EmulatorTestAbstract {

    /**
     * Test of screen features
     */

    public void testTest2_Screen_1(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_2(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_3(TestInfo testInfo) throws IOException {
        doTest(testInfo, 132, 24);
    }

    public void testTest2_Screen_4(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_5(TestInfo testInfo) throws IOException {
        doTest(testInfo, 132, 24);
    }

    public void testTest2_Screen_6(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_7(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_8(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_9(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_10(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_11(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_12(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_13(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_14(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    public void testTest2_Screen_15(TestInfo testInfo) throws IOException {
        doVtTest(testInfo);
    }

    /**
     * Test of characters
     */

    public void testTest3_Characters_1(TestInfo testInfo) throws IOException {
        System.setProperty("jediterm.enable.shift_out.character.support", Boolean.TRUE.toString());
        doVtTest(testInfo);
    }

    private void doVtTest(TestInfo testInfo) throws IOException {
        doTest(testInfo);
    }

    @Override
    protected @NotNull Path getPathToTest(TestInfo testInfo) {
        String name = testInfo.getDisplayName().substring(4);
        int ind = name.lastIndexOf("_");
        return TestPathsManager.getTestDataPath()
                .resolve("vttest/" + name.substring(0, ind) + "/" + name.substring(ind + 1));
    }
}
