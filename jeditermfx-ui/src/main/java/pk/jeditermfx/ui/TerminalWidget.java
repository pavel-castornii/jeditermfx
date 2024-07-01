package pk.jeditermfx.ui;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import pk.jeditermfx.core.TerminalDisplay;
import pk.jeditermfx.core.TtyConnector;

/**
 * @author traff
 */
public interface TerminalWidget {

    JediTermFxWidget createTerminalSession(TtyConnector ttyConnector);

    Pane getPane();

    Node getPreferredFocusableNode();

    boolean canOpenSession();

    Dimension2D getPreferredSize();

    TerminalDisplay getTerminalDisplay();

    void addListener(TerminalWidgetListener listener);

    void removeListener(TerminalWidgetListener listener);
}
