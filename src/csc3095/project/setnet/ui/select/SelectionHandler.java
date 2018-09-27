package csc3095.project.setnet.ui.select;

import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.applier.DrawingPaneResizer;
import csc3095.project.setnet.ui.data.EditMenu;
import csc3095.project.setnet.ui.data.SelectableNode;
import csc3095.project.setnet.ui.symbol.ArcSymbol;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class SelectionHandler
{
    private final SelectionClipboard clipboard;
    private final Pane pane;
    private final Setnet setnet;
    private final TextArea console;
    private final EventHandler<MouseEvent> onPressed;
    private final EventHandler<MouseEvent> onDragged;
    private final EventHandler<KeyEvent> onDeleteKey;
    private EditMenu editMenu = null;

    public SelectionHandler(final Setnet sn, final TextArea c, final Pane p)
    {
        clipboard = new SelectionClipboard();
        setnet = sn;
        console = c;
        pane = p;
        onPressed = e ->
        {
            this.performOnMousePressed(e);
            e.consume();
        };
        onDragged = e ->
        {
            this.performOnMouseDragged(e);
            e.consume();
        };
        onDeleteKey = e->
        {
            this.performOnDeleteKeyPressed(e);
            e.consume();
        };
    }

    public EventHandler<MouseEvent> getPressedHandler() { return onPressed; }

    public EventHandler<MouseEvent> getDraggedHandler() { return onDragged; }

    public EventHandler<KeyEvent> getDeleteHandler() { return  onDeleteKey; }

    public void clearSelected() { clipboard.unselectAll(); }

    private void performOnMousePressed(MouseEvent e)
    {
        Node target = (Node) e.getTarget();
        if (target.getParent() instanceof SelectableNode)
        {
            SelectableNode selectableTarget = (SelectableNode) target.getParent();

            if (e.getButton() == MouseButton.PRIMARY) clipboard.select(selectableTarget, true);
            else if (e.getButton() == MouseButton.SECONDARY)
            {
                if (selectableTarget instanceof PlaceSymbol || selectableTarget instanceof TransitionSymbol)
                {
                    if (editMenu != null) editMenu.getScene().getWindow().hide();
                    editMenu = new EditMenu(setnet, console, target.getParent());
                    editMenu.show(pane, e.getScreenX(), e.getScreenY());
                }
            }
        }
        else clipboard.unselectAll();
    }

    private void performOnMouseDragged(MouseEvent e)
    {
        double newPosX = e.getX(), newPosY = e.getY();
        Node target = (Node) e.getTarget(); //Retrieve the child of a selectable PlaceNode and/or TransitionNode
        if (target.getParent() instanceof SelectableNode)
        {
            SelectableNode selectableTarget = (SelectableNode) target.getParent(); //Change the selected target to be the main PlaceNode or TransitionNode object
            if (selectableTarget instanceof PlaceSymbol)
            {
                PlaceSymbol placeSymbol = (PlaceSymbol) selectableTarget;
                if (withinBounds(placeSymbol.getParent(), newPosX, newPosY))
                {
                    placeSymbol.getPlace().setX(newPosX);
                    placeSymbol.getPlace().setY(newPosY);
                    placeSymbol.updatePosition();
                    DrawingPaneResizer.autoCalculateParentMinmum(pane);
                }
            }
            else if (selectableTarget instanceof TransitionSymbol)
            {
                TransitionSymbol transitionSymbol = (TransitionSymbol) selectableTarget;
                if (withinBounds(transitionSymbol.getParent(), newPosX, newPosY))
                {
                    transitionSymbol.getTransition().setX(newPosX);
                    transitionSymbol.getTransition().setY(newPosY);
                    transitionSymbol.updatePosition();
                    DrawingPaneResizer.autoCalculateParentMinmum(pane);
                }
            }
        }
    }

    private void performOnDeleteKeyPressed(KeyEvent e)
    {
        List<SelectableNode> selected = clipboard.getItems();
        if (e.getCode() == KeyCode.DELETE)
        {
            for (SelectableNode n : selected)
            {
                if (n instanceof PlaceSymbol)
                {
                    PlaceSymbol p = (PlaceSymbol) n;
                    List<ArcSymbol> arcs = removeConnectingArcs(p);

                    setnet.removePlace(p.getPlace());
                    pane.getChildren().remove(p);
                    pane.getChildren().removeAll(arcs);
                }
                else if (n instanceof TransitionSymbol)
                {
                    TransitionSymbol t = (TransitionSymbol) n;
                    List<ArcSymbol> arcs = removeConnectingArcs(t);

                    setnet.removeTransition(t.getTransition());
                    pane.getChildren().remove(t);
                    pane.getChildren().removeAll(arcs);
                }
                else if (n instanceof ArcSymbol)
                {
                    ArcSymbol a = (ArcSymbol) n;

                    setnet.removeArc(a.getArc());
                    pane.getChildren().remove(n);
                }
            }
            clipboard.unselectAll();
        }
    }

    private boolean withinBounds(Parent root, double newX, double newY)
    {
        Bounds bounds = root.getLayoutBounds();
        return bounds.contains(newX, newY);
    }

    private List<ArcSymbol> removeConnectingArcs(SelectableNode node)
    {
        List<ArcSymbol> arcs = new ArrayList<>();
        if (node instanceof PlaceSymbol || node instanceof TransitionSymbol)
        {
            for (Node n: pane.getChildren())
            {
                if (n instanceof ArcSymbol)
                {
                    ArcSymbol a = (ArcSymbol) n;
                    if (a.getPlaceSymbol() == node || a.getTransitionSymbol() == node)
                    {
                        arcs.add(a);
                        setnet.removeArc(a.getArc());
                    }
                }
            }
        }
        return arcs;
    }
}
