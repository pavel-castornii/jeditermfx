package com.techsenger.jeditermfx.ui;

public class FxScrollBarUtils {

    public static double getValueFor(int lineIndex, int totalLines, double scrollBarMin, double scrollBarMax) {
        double result;
        if ((int) scrollBarMin == 0 && totalLines < scrollBarMax) {
            result = lineIndex;
        } else {
            double normalizedValue = (double) lineIndex / (totalLines - 1);
            result = scrollBarMin + normalizedValue * (scrollBarMax - scrollBarMin);
        }
        return result;
    }
}
