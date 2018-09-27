package csc3095.project.setnet.ui.firing;

import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class StateChanger
{
    private static Setnet setnet;

    public final static void loadSetnet(Setnet referredSetnet) { setnet = referredSetnet; }

    public final static void setEnabledTransitions(Pane pane, boolean systemRunning)
    {
        for (Node n: pane.getChildren())
        {
            if (n instanceof TransitionSymbol)
            {
                TransitionSymbol t = (TransitionSymbol) n;
                t.update(systemRunning);
            }
        }
    }

    public final static void updatePlacesAndTransitions(Pane pane, FiringState firingState)
    {
        for (Node n: pane.getChildren())
        {
            if (n instanceof TransitionSymbol)
            {
                TransitionSymbol t = (TransitionSymbol) n;
                t.update(t.getTransition().isFireable() && firingState.canFire(setnet));
            }
            else if (n instanceof PlaceSymbol)
            {
                PlaceSymbol p = (PlaceSymbol) n;
                p.update();
            }
        }
    }
}
