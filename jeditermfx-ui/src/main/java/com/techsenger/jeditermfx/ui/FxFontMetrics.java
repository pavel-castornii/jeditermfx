package com.techsenger.jeditermfx.ui;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FxFontMetrics {

    private static final Logger logger = LoggerFactory.getLogger(FxFontMetrics.class);

    public static FxFontMetrics create(Font font, String str) {
        var text = new Text(str);
        text.setFont(font);
        text.applyCss();//TODO???
        var width = text.getLayoutBounds().getWidth();
        var height = text.getLayoutBounds().getHeight();
        var descent = text.getLayoutBounds().getHeight() - text.getBaselineOffset();
        var metrics = new FxFontMetrics(width, height, descent);
        logger.trace("Created metrics: {} for {}", metrics, font);
        return metrics;
    }

    private final double descent;

    private final double width;

    private final double height;

    private FxFontMetrics(double width, double height, double descent) {
        this.descent = descent;
        this.width = width;
        this.height = height;
    }

    public double getDescent() {
        return descent;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "{" + "descent=" + descent + ", width=" + width + ", height=" + height + '}';
    }
}
