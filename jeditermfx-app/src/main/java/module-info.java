
module com.techsenger.jeditermfx.app {
    requires com.techsenger.jeditermfx.core;
    requires com.techsenger.jeditermfx.ui;
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

    exports com.techsenger.jeditermfx.app;
    exports com.techsenger.jeditermfx.app.example;
}
