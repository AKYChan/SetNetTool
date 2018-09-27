package csc3095.project.setnet.ui.firing;

import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.main.SetnetStateSaver;
import csc3095.project.setnet.main.steps.StepList;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class FiringHandler
{
    private final Setnet setnet;
    private final Pane pane;
    private final TextArea stepConsole;
    private final SetnetStateSaver setnetStateSaver;
    private final Button backButton;
    private final Button forwardButton;
    private final EventHandler<MouseEvent> pressedSeqHandler;
    private final EventHandler<MouseEvent> pressedMaxHandler;
    private final EventHandler<MouseEvent> pressedBackHandler;
    private final EventHandler<MouseEvent> pressedForwardHandler;
    private final StepList<String> firedTransitionsList = new StepList<>();

    public FiringHandler(Setnet setnet, Pane pane, TextArea stepConsole, SetnetStateSaver setnetStateSaver, Button backButton, Button forwardButton)
    {
        this.setnet = setnet;
        this.pane = pane;
        this.stepConsole = stepConsole;
        this.setnetStateSaver = setnetStateSaver;
        this.backButton = backButton;
        this.forwardButton = forwardButton;
        pressedSeqHandler = e ->
        {
            onSeqMousePressed(e);
            e.consume();
        };
        pressedMaxHandler = e ->
        {
            onMaxMousePressed(e);
            e.consume();
        };
        pressedBackHandler = e ->
        {
            setnetStateSaver.goBackStep(pane, backButton, forwardButton);
            firedTransitionsList.navBack();
            updateStepsConsole();
            e.consume();
        };
        pressedForwardHandler = e ->
        {
            setnetStateSaver.goForwardStep(pane, backButton, forwardButton);
            firedTransitionsList.navForward();
            updateStepsConsole();
            e.consume();
        };
        resetFiredTransitionsList(); //Place holder to track how many transitions are fired, preventing a null pointer exception from occurring
    }

    public EventHandler<MouseEvent> getPressedSeqHandler() { return pressedSeqHandler; }

    public EventHandler<MouseEvent> getPressedMaxHandler() { return pressedMaxHandler; }

    public EventHandler<MouseEvent> getPressedBackHandler() { return pressedBackHandler; }

    public EventHandler<MouseEvent> getPressedForwardHandler() { return pressedForwardHandler; }

    private void onSeqMousePressed(MouseEvent e) { mousePressed(e, FiringState.SEQUENTIAL_FIRING); }

    private void onMaxMousePressed(MouseEvent e) { mousePressed(e, FiringState.MAXIMAL_FIRING); }

    private void mousePressed(MouseEvent e, FiringState firingState)
    {
        Node target = (Node) e.getTarget();
        if (target.getParent() instanceof TransitionSymbol)
        {
            TransitionSymbol t = (TransitionSymbol) target.getParent();
            if (t.getTransition().isFireable() && firingState.canFire(setnet))
            {
                String firedTransitions = "";
                for (Node n: pane.getChildren())
                {
                    if (n instanceof TransitionSymbol)
                    {
                        TransitionSymbol ts = (TransitionSymbol) n;
                        if (ts.getTransition().isFireable())
                        {
                            firedTransitions += ts.getTransition().getName() + " ";
                        }
                    }
                }
                firedTransitions += "\n";
                firedTransitionsList.clearFromPosition();
                firedTransitionsList.saveStep(firedTransitions);
                updateStepsConsole();

                firingState.run(setnet);
                StateChanger.updatePlacesAndTransitions(pane, firingState);
                setnetStateSaver.saveStep(pane, backButton, forwardButton);
            }
        }
    }

    //Single function for the purpose of updating the steps console as the user goes back, forward or saves over the current step
    private void updateStepsConsole()
    {
        stepConsole.setText("");
        for (String s: firedTransitionsList.getListUntilCurrent())
        {
            if (!firedTransitionsList.getListUntilCurrent().isEmpty()) stepConsole.setText(stepConsole.getText() + s);
        }
    }

    public void resetFiredTransitionsList()
    {
        firedTransitionsList.clear();
        firedTransitionsList.saveStep(new String(""));
    }
}