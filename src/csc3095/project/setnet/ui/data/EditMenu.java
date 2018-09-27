package csc3095.project.setnet.ui.data;

import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;

public class EditMenu extends ContextMenu
{
    public EditMenu(Setnet setnet, TextArea console, Node node)
    {
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    PropertyDialog propertyDialog = new PropertyDialog(setnet, console, node);
                    propertyDialog.show();
                }
                catch (Exception e)
                {
                    String nodeType = "";
                    if (node instanceof PlaceSymbol) nodeType = "Place";
                    else if (node instanceof TransitionSymbol) nodeType = "Transition";
                    console.appendText("Unable to edit the selected " + nodeType +". Aborting editing operation.");
                }
            }
        });
        this.getItems().add(edit);
    }
}
