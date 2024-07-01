
module pk.jeditermfx.app {
    requires pk.jeditermfx.core;
    requires pk.jeditermfx.ui;
    requires kotlin.stdlib;
    requires pty4j;
    requires purejavacomm;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires java.desktop;
    requires java.logging;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;

    exports pk.jeditermfx.app;
    exports pk.jeditermfx.app.example;
}
