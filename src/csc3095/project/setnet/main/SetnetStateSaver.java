package csc3095.project.setnet.main;

import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.main.steps.StepList;
import csc3095.project.setnet.ui.firing.FiringState;
import csc3095.project.setnet.ui.firing.StateChanger;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetnetStateSaver
{
    private final Map<Place, Boolean> state = new HashMap<>();
    private final StepList<Map<Place, Boolean>> steps = new StepList<>();
    private FiringState firingState;

    public SetnetStateSaver() { this.firingState = FiringState.INACTIVE; }

    public void changeFiringState(FiringState firingState) { this.firingState =  firingState; }

    public void saveStep(Pane pane, Button backButton, Button forwardButton)
    {
        Map<Place, Boolean> step = new HashMap<>();
        addPlaceStatesToMap(step, pane);
        if (!steps.isCurrentAtTail()) steps.clearFromPosition();
        steps.saveStep(step);
        updateButtonStates(backButton, forwardButton);
    }

    public void saveState(Pane pane)
    {
        state.clear();
        Map<Place, Boolean> initialStep = new HashMap<>();
        addPlaceStatesToMap(initialStep, pane);
        state.putAll(initialStep);
        steps.saveStep(initialStep);
    }

    public void resetState(Pane pane, TextArea stepConsole, Button backButton, Button forwardButton)
    {
        firingState = FiringState.INACTIVE;
        if (!state.isEmpty())
        {
            for (Node n: pane.getChildren())
            {
                if (n instanceof PlaceSymbol)
                {
                    PlaceSymbol p = (PlaceSymbol) n;
                    Place place = p.getPlace();

                    if (state.get(place))   place.addToken();
                    else                    place.removeToken();

                    p.update();
                }
            }
            state.clear();
        }
        if (!steps.isEmpty()) steps.clear();
        updateButtonStates(backButton, forwardButton);
        stepConsole.setText("");
    }

    public void goBackStep(Pane pane, Button backStepButton, Button forwardStepButton)
    {
        Map<Place, Boolean> stepData = steps.navBack();
        changeToStepState(stepData, pane);
        updateButtonStates(backStepButton, forwardStepButton);
    }

    public void goForwardStep(Pane pane, Button backStepButton, Button forwardStepButton)
    {
        Map<Place, Boolean> stepData = steps.navForward();
        changeToStepState(stepData, pane);
        updateButtonStates(backStepButton, forwardStepButton);
    }

    private void addPlaceStatesToMap(Map<Place, Boolean> map, Pane pane)
    {
        for (Node n: pane.getChildren())
        {
            if (n instanceof PlaceSymbol)
            {
                PlaceSymbol p = (PlaceSymbol) n;
                Place place = p.getPlace();
                map.put(place, place.hasToken());
            }
        }
    }

    private void updateButtonStates(Button backStepButton, Button forwardStepButton)
    {
        if (steps.isEmpty())
        {
            backStepButton.setDisable(true);
            forwardStepButton.setDisable(true);
        }
        else
        {
            if (steps.isCurrentAtHead())    backStepButton.setDisable(true);
            else                            backStepButton.setDisable(false);

            if (steps.isCurrentAtTail())    forwardStepButton.setDisable(true);
            else                            forwardStepButton.setDisable(false);
        }
    }

    private void changeToStepState(Map<Place, Boolean> stepData, Pane pane)
    {
        for (Node n: pane.getChildren())
        {
            if (n instanceof PlaceSymbol)
            {
                PlaceSymbol p = (PlaceSymbol) n;
                Place place = p.getPlace();

                if (stepData.get(place))   place.addToken();
                else                       place.removeToken();

                StateChanger.updatePlacesAndTransitions(pane, firingState);
            }
        }
    }
}
