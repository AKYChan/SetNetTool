package csc3095.project.setnet.ui.symbol;

import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.ui.applier.GlowApplier;
import csc3095.project.setnet.ui.data.SelectableNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PlaceSymbol extends Group implements SelectableNode
{
    private final static double DEFAULT_BODY_RADIUS = 12.5;
    private final static double DEFAULT_TOKEN_RADIUS = 5.0;

    private final Circle body;
    private final Circle token;
    private final Text name;
    private final Place place;
    private final DoubleProperty namePropertyX;
    private final DoubleProperty namePropertyY;

    public PlaceSymbol(Place place) { this(new Circle(DEFAULT_BODY_RADIUS), new Circle(DEFAULT_TOKEN_RADIUS), new Text(place.getName()), place); }

    protected PlaceSymbol(Circle body, Circle token, Text name, Place place)
    {
        super(body, token, name);

        this.body = body;
        this.token = token;
        this.name = name;
        this.place = place;

        body.setCenterX(place.getX());
        body.setCenterY(place.getY());

        body.setStroke(Color.BLACK);
        body.setFill(Color.WHITE);

        token.centerXProperty().bind(body.centerXProperty());
        token.centerYProperty().bind(body.centerYProperty());

        namePropertyX = new SimpleDoubleProperty(place.getX() - (DEFAULT_BODY_RADIUS / 2.0));
        namePropertyY = new SimpleDoubleProperty(place.getY() + (DEFAULT_BODY_RADIUS * 2.0));
        name.xProperty().bind(namePropertyX);
        name.yProperty().bind(namePropertyY);
        name.setTextAlignment(TextAlignment.CENTER);

        update();
    }

    public DoubleProperty xProperty() { return body.centerXProperty(); }

    public DoubleProperty yProperty() { return body.centerYProperty(); }

    public Place getPlace() { return place; }

    public void update()
    {
        if (place.hasToken())   changeTokenColour(Color.BLACK);
        else                    changeTokenColour(Color.WHITE);
        name.setText(place.getName());
    }

    public void updatePosition()
    {
        body.setCenterX(place.getX());
        body.setCenterY(place.getY());
        xProperty().set(place.getX());
        yProperty().set(place.getY());
        namePropertyX.set(place.getX() - (DEFAULT_BODY_RADIUS / 2.0));
        namePropertyY.set(place.getY() + (DEFAULT_BODY_RADIUS * 2.0));
    }

    @Override
    public void notifySelection(boolean selected)
    {
        if (selected)   body.setEffect(GlowApplier.highlightSelected());
        else            body.setEffect(GlowApplier.unhighlightSelected());
    }

    private void changeTokenColour(Paint value)
    {
        token.setStroke(value);
        token.setFill(value);
    }
}
