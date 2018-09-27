package csc3095.project.setnet.ui.symbol;

import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.ui.applier.GlowApplier;
import csc3095.project.setnet.ui.data.SelectableNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TransitionSymbol extends Group implements SelectableNode
{
    private final static double DEFAULT_WIDTH = 25.0;
    private final static double DEFAULT_HEIGHT = 25.0;
    private final double CENTRE_THRESHOLD;

    private final Rectangle body;
    private final Text name;
    private final Transition transition;
    private final DoubleProperty xProperty;
    private final DoubleProperty yProperty;
    private final DoubleProperty namePropertyX;
    private final DoubleProperty namePropertyY;

    public TransitionSymbol(Transition transition) { this(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT), new Text(transition.getName()), transition); }

    protected TransitionSymbol(Rectangle body, Text name, Transition transition)
    {
        super(body, name);
        this.body = body;
        this.name = name;
        this.transition = transition;

        CENTRE_THRESHOLD = body.getWidth() / 2;

        body.setStroke(Color.BLACK);
        body.setFill(Color.WHITE);

        body.setX(transition.getX() - CENTRE_THRESHOLD);
        body.setY(transition.getY() - CENTRE_THRESHOLD);
        xProperty = new SimpleDoubleProperty(transition.getX());
        yProperty = new SimpleDoubleProperty(transition.getY());

        namePropertyX = new SimpleDoubleProperty(transition.getX() - (DEFAULT_WIDTH / 4.0));
        namePropertyY = new SimpleDoubleProperty(transition.getY() + DEFAULT_HEIGHT);
        name.xProperty().bind(namePropertyX);
        name.yProperty().bind(namePropertyY);
        name.setTextAlignment(TextAlignment.CENTER);

        update(false);
    }

    public DoubleProperty xProperty() { return xProperty; }

    public DoubleProperty yProperty() { return yProperty; }

    public Transition getTransition() { return transition; }

    public void update(boolean systemRunning)
    {
        if (transition.isFireable() && systemRunning)    changeLineColour(Color.ORANGE);
        else                                             changeLineColour(Color.BLACK);
        name.setText(transition.getName());
    }

    public void updatePosition()
    {
        body.setX(transition.getX() - CENTRE_THRESHOLD);
        body.setY(transition.getY() - CENTRE_THRESHOLD);
        xProperty.set(transition.getX());
        yProperty.set(transition.getY());
        namePropertyX.set(transition.getX() - (DEFAULT_WIDTH / 4.0));
        namePropertyY.set(transition.getY() + DEFAULT_HEIGHT);
    }

    @Override
    public void notifySelection(boolean selected)
    {
        if (selected)   body.setEffect(GlowApplier.highlightSelected());
        else            body.setEffect(GlowApplier.unhighlightSelected());
    }

    private void changeLineColour(Paint value)
    {
        body.setStroke(value);
    }
}
