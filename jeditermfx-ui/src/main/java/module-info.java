
module pk.jeditermfx.ui {
    requires pk.jeditermfx.core;
    requires kotlin.stdlib;
    requires pty4j;
    requires purejavacomm;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;

    exports pk.jeditermfx.ui;
    exports pk.jeditermfx.ui.settings;
}
