
module pk.jeditermfx.core {
    requires org.jetbrains.annotations;
    requires kotlin.stdlib;
    requires org.slf4j;
    requires java.desktop;

    exports pk.jeditermfx.core;
    exports pk.jeditermfx.core.compatibility;
    exports pk.jeditermfx.core.emulator;
    exports pk.jeditermfx.core.emulator.charset;
    exports pk.jeditermfx.core.emulator.mouse;
    exports pk.jeditermfx.core.input;
    exports pk.jeditermfx.core.model;
    exports pk.jeditermfx.core.model.hyperlinks;
    exports pk.jeditermfx.core.typeahead;
    exports pk.jeditermfx.core.util;
}
