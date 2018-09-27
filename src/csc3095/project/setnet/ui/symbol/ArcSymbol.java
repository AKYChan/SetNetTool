package csc3095.project.setnet.ui.symbol;

import csc3095.project.setnet.components.Arc;
import csc3095.project.setnet.components.Flow;
import csc3095.project.setnet.ui.data.SelectableNode;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ArcSymbol extends Group implements SelectableNode
{
    private final static double LENGTH_FACTOR = 10.0;
    private final static double DISTANCE_FACTOR = 5.0;

    private final Line body;
    private final Line leftHead;
    private final Line rightHead;
    private final Arc arc;
    private final PlaceSymbol placeSymbol;
    private final TransitionSymbol transitionSymbol;

    public ArcSymbol(Arc arc, PlaceSymbol placeSymbol, TransitionSymbol transitionSymbol) { this(new Line(), new Line(), new Line(), arc, placeSymbol, transitionSymbol); }

    protected ArcSymbol(Line body, Line leftHead, Line rightHead, Arc arc, PlaceSymbol placeSymbol, TransitionSymbol transitionSymbol)
    {
        super(body, leftHead, rightHead);

        this.body = body;
        this.leftHead = leftHead;
        this.rightHead = rightHead;
        this.arc = arc;
        this.placeSymbol = placeSymbol;
        this.transitionSymbol = transitionSymbol;

        //add a listener to the properties, to ensure arrow heads are updated accordingly
        InvalidationListener listener = setupArrowListener();
        startXProperty().addListener(listener);
        startYProperty().addListener(listener);
        endXProperty().addListener(listener);
        endYProperty().addListener(listener);

        bindSymbols();
    }

    public final DoubleProperty startXProperty() { return body.startXProperty(); }

    public final DoubleProperty startYProperty() { return body.startYProperty(); }

    public final DoubleProperty endXProperty() { return body.endXProperty(); }

    public final DoubleProperty endYProperty() { return body.endYProperty(); }

    public final Arc getArc() { return arc; }

    public final PlaceSymbol getPlaceSymbol() { return placeSymbol; }

    public final TransitionSymbol getTransitionSymbol() { return transitionSymbol; }

    private void bindSymbols()
    {
        if (arc.getFlow() == Flow.PLACE_TO_TRANSITION)
        {
            body.startXProperty().bind(placeSymbol.xProperty());
            body.startYProperty().bind(placeSymbol.yProperty());
            body.endXProperty().bind(transitionSymbol.xProperty());
            body.endYProperty().bind(transitionSymbol.yProperty());
        }
        else if (arc.getFlow() == Flow.TRANSITION_TO_PLACE)
        {
            body.startXProperty().bind(transitionSymbol.xProperty());
            body.startYProperty().bind(transitionSymbol.yProperty());
            body.endXProperty().bind(placeSymbol.xProperty());
            body.endYProperty().bind(placeSymbol.yProperty());
        }
    }

    @Override
    public void notifySelection(boolean selected)
    {
        if (selected)
        {
            body.setStroke(Color.BLUE);
            leftHead.setStroke(Color.BLUE);
            rightHead.setStroke(Color.BLUE);
        }
        else
        {
            body.setStroke(Color.BLACK);
            leftHead.setStroke(Color.BLACK);
            rightHead.setStroke(Color.BLACK);
        }
    }

    private final InvalidationListener setupArrowListener()
    {
        InvalidationListener updater = o ->
        {
            double bex = body.getEndX();
            double bey = body.getEndY();
            double bsx = body.getStartX();
            double bsy = body.getStartY();

            double lengthX = bsx - bex;
            double lengthY = bsy - bey;

            double bmx = (bex + bsx) / 2.0 - lengthX / 4.0;
            double bmy = (bey + bsy) / 2.0 - lengthY / 4.0;

            //Set the ends of the left and right heads to connect to the end of the body
            leftHead.setEndX(bmx);
            leftHead.setEndY(bmy);
            rightHead.setEndX(bmx);
            rightHead.setEndY(bmy);

            //Set up the line to create two separate lines that split off to form the arrow head
            double dFactor = LENGTH_FACTOR / Math.hypot(lengthX, lengthY);
            double oFactor = DISTANCE_FACTOR / Math.hypot(lengthX, lengthY);

            // part in direction of main arcBody
            double adx = (bsx - bex) * dFactor;
            double ady = (bsy - bey) * dFactor;

            // part ortogonal to main arcBody
            double aox = (bsx - bex) * oFactor;
            double aoy = (bsy - bey) * oFactor;

            leftHead.setStartX(bmx + adx - aoy);
            leftHead.setStartY(bmy + ady + aox);
            rightHead.setStartX(bmx + adx + aoy);
            rightHead.setStartY(bmy + ady - aox);
        };
        updater.invalidated(null);
        return updater;
    }
}
