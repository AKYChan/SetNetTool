package csc3095.project.setnet.ui.drawing;

import csc3095.project.setnet.components.Arc;
import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.applier.DrawingPaneResizer;
import csc3095.project.setnet.ui.symbol.ArcSymbol;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DrawHandler
{
    private final Setnet setnet;
    private final Pane pane;
    private final TextArea console;
    private final EventHandler<MouseEvent> onClickDrawPlace;
    private final EventHandler<MouseEvent> onClickDrawTransition;
    private final EventHandler<MouseEvent> onClickDrawArc;

    private ArcConnector connector = ArcConnector.NONE_SELECTED;
    private PlaceSymbol placeSymbol = null;
    private TransitionSymbol transitionSymbol = null;

    public DrawHandler(Setnet setnet, Pane pane, TextArea console)
    {
        this.setnet = setnet;
        this.pane = pane;
        this.console = console;
        onClickDrawPlace = e ->
        {
            drawPlace(e);
            e.consume();
        };
        onClickDrawTransition = e ->
        {
            drawTransition(e);
            e.consume();
        };
        onClickDrawArc = e ->
        {
            drawArc(e);
            e.consume();
        };
    }

    public EventHandler<MouseEvent> getOnClickDrawPlace() { return onClickDrawPlace; }

    public EventHandler<MouseEvent> getOnClickDrawTransition() { return onClickDrawTransition; }

    public EventHandler<MouseEvent> getOnClickDrawArc() { return onClickDrawArc; }

    public void clearState() { connector = ArcConnector.NONE_SELECTED; }

    private void drawPlace(MouseEvent e)
    {
        if (e.getButton() == MouseButton.PRIMARY)
        {
            Place place = setnet.createPlace();
            place.setX(e.getX());
            place.setY(e.getY());

            PlaceSymbol placeSymbol = new PlaceSymbol(place);
            placeSymbol.update();

            pane.getChildren().add(placeSymbol);
            DrawingPaneResizer.autoCalculateParentMinmum(pane);
        }
    }

    private void drawTransition(MouseEvent e)
    {
        if (e.getButton() == MouseButton.PRIMARY)
        {
            Transition transition = setnet.createTransition();
            transition.setX(e.getX());
            transition.setY(e.getY());

            TransitionSymbol transitionSymbol = new TransitionSymbol(transition);
            pane.getChildren().add(transitionSymbol);
            DrawingPaneResizer.autoCalculateParentMinmum(pane);
        }
    }

    private void drawArc(MouseEvent e)
    {
        if (e.getButton() == MouseButton.PRIMARY)
        {
            Node target = (Node) e.getTarget();
            if (target.getParent() instanceof PlaceSymbol)
            {
                switch (connector)
                {
                    case NONE_SELECTED:
                        placeSymbol = (PlaceSymbol) target.getParent();
                        outputToConsole(placeSymbol.getPlace().getName() + " selected.");
                        connector = ArcConnector.PLACE_SELECTED;
                        break;

                    case TRANSITION_SELECTED:
                        placeSymbol = (PlaceSymbol) target.getParent();
                        outputToConsole("Transition " + transitionSymbol.getTransition().getName() + " has connected with place " + placeSymbol.getPlace().getName());
                        createArcConnection(pane);
                        break;

                    case PLACE_SELECTED:
                        outputToConsole("Can't connect an arc between two places. Please try connecting it to a transition.");
                        break;
                }
            }
            else if (target.getParent() instanceof TransitionSymbol)
            {
                switch (connector)
                {
                    case NONE_SELECTED:
                        transitionSymbol = (TransitionSymbol) target.getParent();
                        outputToConsole(transitionSymbol.getTransition().getName() + " selected.");
                        connector = ArcConnector.TRANSITION_SELECTED;
                        break;

                    case PLACE_SELECTED:
                        transitionSymbol = (TransitionSymbol) target.getParent();
                        outputToConsole("Place " + placeSymbol.getPlace().getName()+ " has connected with transition " + transitionSymbol.getTransition().getName());
                        createArcConnection(pane);
                        break;

                    case TRANSITION_SELECTED:
                        outputToConsole("Can't connect an arc between two transitions. Please try connecting it to a place.");
                        break;
                }
            }
        }
    }

    private void createArcConnection(Pane pane)
    {
        Arc a = connector.connect(setnet, placeSymbol.getPlace(), transitionSymbol.getTransition());
        if (a != null)
        {
            ArcSymbol arcSymbol = new ArcSymbol(a, placeSymbol, transitionSymbol);

            pane.getChildren().add(arcSymbol);
            arcSymbol.toBack();

            placeSymbol = null;
            transitionSymbol = null;

            connector = ArcConnector.NONE_SELECTED;
        }
        else
        {
            if      (connector == ArcConnector.PLACE_SELECTED) outputToConsole("Arc between " + placeSymbol.getPlace().getName() + "->" + transitionSymbol.getTransition().getName() + " already exists");
            else if (connector == ArcConnector.TRANSITION_SELECTED) outputToConsole("Arc between " + transitionSymbol.getTransition().getName() + "->" + placeSymbol.getPlace().getName() + " already exists");
        }
    }

    private void outputToConsole(String line) { console.appendText(line + "\n"); }
}
