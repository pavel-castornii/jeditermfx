package pk.jeditermfx.app;

import pk.jeditermfx.app.pty.TtyConnectorWaitFor;
import pk.jeditermfx.core.compatibility.Point;
import pk.jeditermfx.core.Terminal;
import pk.jeditermfx.core.TtyConnector;
import pk.jeditermfx.core.model.SelectionUtil;
import pk.jeditermfx.core.model.TerminalSelection;
import pk.jeditermfx.ui.JediTermFxWidget;
import pk.jeditermfx.ui.TerminalPanel;
import pk.jeditermfx.ui.TerminalWidget;
import pk.jeditermfx.ui.settings.DefaultSettingsProvider;
import pk.jeditermfx.ui.settings.SettingsProvider;
import pk.jeditermfx.app.debug.TerminalDebugView;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AbstractTerminalApplication extends Application {

    public static final Logger logger = LoggerFactory.getLogger(AbstractTerminalApplication.class);

    private Stage myBufferStage;

    private JediTermFxWidget myWidget;

    private final MenuItem myShowBuffersAction = new MenuItem("Show buffers");

    private final MenuItem myDumpDimension = new MenuItem("Dump terminal dimension");

    private final MenuItem myDumpSelection = new MenuItem("Dump selection");

    private final MenuItem myDumpCursorPosition = new MenuItem("Dump cursor position");

    private final MenuItem myCursor0x0 = new MenuItem("1x1");

    private final MenuItem myCursor10x10 = new MenuItem("10x10");

    private final MenuItem myCursor80x24 = new MenuItem("80x24");

    private MenuBar getMenuBar() {
        final MenuBar mb = new MenuBar();
        final Menu dm = new Menu("Debug");
        Menu logLevel = new Menu("Set log level ...");
        Level[] levels = new Level[]{Level.ALL, Level.FINE, Level.INFO, Level.WARNING, Level.SEVERE, Level.OFF};
        for (final Level l : levels) {
            var item = new MenuItem(l.toString());
            item.setOnAction(e -> java.util.logging.Logger.getLogger("").setLevel(l));
            logLevel.getItems().add(item);
        }
        Menu cursorPosition = new Menu("Set cursor position ...");
        cursorPosition.getItems().addAll(
                myCursor0x0,
                myCursor10x10,
                myCursor80x24);
        dm.getItems().addAll(
                logLevel,
                new SeparatorMenuItem(),
                myShowBuffersAction,
                new SeparatorMenuItem(),
                myDumpDimension,
                myDumpSelection,
                myDumpCursorPosition,
                cursorPosition
        );
        mb.getMenus().add(dm);
        return mb;
    }

    protected void openSession(TerminalWidget terminal) {
        if (terminal.canOpenSession()) {
            openSession(terminal, createTtyConnector());
        }
    }

    public void openSession(TerminalWidget terminal, TtyConnector ttyConnector) {
        JediTermFxWidget session = terminal.createTerminalSession(ttyConnector);
        if (ttyConnector instanceof JediTermFx.LoggingPtyProcessTtyConnector) {
            ((JediTermFx.LoggingPtyProcessTtyConnector) ttyConnector).setWidget(session);
        }
        session.start();
    }

    public abstract TtyConnector createTtyConnector();

    @Override
    public void start(Stage stage) throws Exception {
        myWidget = createTerminalWidget(new DefaultSettingsProvider());
        stage.setTitle("JediTermFX");
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        final MenuBar mb = getMenuBar();
        initMenuItems();
        VBox.setVgrow(myWidget.getPane(), Priority.ALWAYS);
        var root = new VBox(mb, myWidget.getPane());
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(true);
        myWidget.getTerminal().addApplicationTitleListener(e -> Platform.runLater(() -> stage.setTitle(e)));
        openSession(myWidget);
        onTermination(myWidget, exitCode -> {
            myWidget.close();
            System.exit(exitCode); // unneeded, but speeds up the JVM termination
        });
        stage.show();
    }

    protected AbstractTerminalApplication() {

    }

    private void initMenuItems() {
        myShowBuffersAction.setOnAction(e -> {
            showBuffers();
        });
        myDumpDimension.setOnAction(e -> {
            Terminal terminal = myWidget.getTerminal();
            logger.info(terminal.getTerminalWidth() + "x" + terminal.getTerminalHeight());
        });
        myDumpSelection.setOnAction(e -> {
            JediTermFxWidget widget = myWidget;
            TerminalPanel terminalPanel = widget.getTerminalPanel();
            TerminalSelection selection = terminalPanel.getSelection();
            if (selection != null) {
                Pair<Point, Point> points = selection.pointsForRun(widget.getTerminal().getTerminalWidth());
                logger.info(selection + " : '"
                        + SelectionUtil.getSelectionText(points.getFirst(), points.getSecond(), terminalPanel.getTerminalTextBuffer()) + "'");
            } else {
                logger.info("No selection");
            }
        });
        myDumpCursorPosition.setOnAction(e -> {
            logger.info(myWidget.getTerminal().getCursorX() +
                    "x" + myWidget.getTerminal().getCursorY());
        });
        myCursor0x0.setOnAction(e -> {
            myWidget.getTerminal().cursorPosition(1, 1);
        });
        myCursor10x10.setOnAction(e -> {
            myWidget.getTerminal().cursorPosition(10, 10);
        });
        myCursor80x24.setOnAction(e -> {
            myWidget.getTerminal().cursorPosition(80, 24);
        });
    }

    private static void onTermination(@NotNull JediTermFxWidget widget, @NotNull IntConsumer terminationCallback) {
        new TtyConnectorWaitFor(widget.getTtyConnector(),
                widget.getExecutorServiceManager().getUnboundedExecutorService(),
                terminationCallback);
    }

    protected JediTermFxWidget createTerminalWidget(@NotNull SettingsProvider settingsProvider) {
        return new JediTermFxWidget(settingsProvider);
    }

//TODO?
//  private void sizeFrameForTerm(final JFrame frame) {
//    SwingUtilities.invokeLater(() -> {
//      Dimension d = myWidget.getPreferredSize();
//      d.width += frame.getWidth() - frame.getContentPane().getWidth();
//      d.height += frame.getHeight() - frame.getContentPane().getHeight();
//      frame.setSize(d);
//    });
//  }

    private void showBuffers() {
        if (myBufferStage != null) {
            myBufferStage.requestFocus();
            return;
        }
        myBufferStage = new Stage();
        myBufferStage.setTitle("Buffers");
        TerminalDebugView debugView = new TerminalDebugView(myWidget);
        var scene = new Scene(debugView.getPane(), 1600, 800);
        myBufferStage.setScene(scene);
        myBufferStage.centerOnScreen();
        myBufferStage.setOnCloseRequest(e -> {
            myBufferStage = null;
            debugView.stop();
            logger.info("Buffer stage closed");
        });
        myBufferStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                myBufferStage.close();
            }
        });
        myBufferStage.show();
    }
}
