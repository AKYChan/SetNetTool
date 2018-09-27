package csc3095.project.setnet.ui.applier;

import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DrawingPaneResizer
{
    private final static double COMPONENT_OVER_MINIMUM_THRESHOLD_SPACE = 50.0;

    public static void autoCalculateParentMinmum(Pane pane)
    {
        double furthestX = pane.getPrefWidth(), furthestY = pane.getPrefHeight();
        for (Node n: pane.getChildren())
        {
            if (n instanceof PlaceSymbol)
               {
                   PlaceSymbol p = (PlaceSymbol) n;
                   furthestX = Math.max(furthestX, p.getPlace().getX() + COMPONENT_OVER_MINIMUM_THRESHOLD_SPACE);
                   furthestY = Math.max(furthestY, p.getPlace().getY() + COMPONENT_OVER_MINIMUM_THRESHOLD_SPACE);
               }
            else if (n instanceof TransitionSymbol)
            {
                TransitionSymbol t = (TransitionSymbol) n;
                furthestX = Math.max(furthestX, t.getTransition().getX() + COMPONENT_OVER_MINIMUM_THRESHOLD_SPACE);
                furthestY = Math.max(furthestY, t.getTransition().getY() + COMPONENT_OVER_MINIMUM_THRESHOLD_SPACE); }
        }
        pane.setMinSize(furthestX, furthestY);
    }
}