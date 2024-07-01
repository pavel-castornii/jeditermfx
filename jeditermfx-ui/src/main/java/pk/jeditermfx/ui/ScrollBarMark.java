package pk.jeditermfx.ui;

import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class ScrollBarMark {

    private final Rectangle rect;

    private final DoubleProperty position = new SimpleDoubleProperty();

    public ScrollBarMark(Color color) {
        rect = new Rectangle(5, 2, color);
        rect.setManaged(false);
    }

    public void attach(ScrollBar scrollBar, StackPane track) {
        rect.widthProperty().bind(track.widthProperty());
        track.getChildren().add(rect);
        rect.layoutYProperty().bind(Bindings.createDoubleBinding(() -> {
                    double height = track.getLayoutBounds().getHeight();
                    double visibleAmout = scrollBar.getVisibleAmount();
                    double max = scrollBar.getMax();
                    double min = scrollBar.getMin();
                    double pos = position.get();
                    double delta = max - min;
                    double relativePosition = (pos - min) / delta;
                    return (relativePosition * height) - (rect.getHeight() / 2);
                },
                position,
                track.layoutBoundsProperty(),
                scrollBar.visibleAmountProperty(),
                scrollBar.minProperty(),
                scrollBar.maxProperty()));
    }

    public final double getPosition() {
        return this.position.get();
    }

    public final void setPosition(double value) {
        this.position.set(value);
    }

    public final DoubleProperty positionProperty() {
        return this.position;
    }

    public void detach() {
        StackPane parent = (StackPane) rect.getParent();
        if (parent != null) {
            parent.getChildren().remove(rect);
            rect.layoutYProperty().unbind();
            rect.widthProperty().unbind();
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(positionAsInt());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScrollBarMark other = (ScrollBarMark) obj;
        return Objects.equals(this.positionAsInt(), other.positionAsInt());
    }

    private int positionAsInt() {
        return (int) Math.round(this.position.get());
    }
}
