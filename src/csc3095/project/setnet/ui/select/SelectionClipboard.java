package csc3095.project.setnet.ui.select;

import csc3095.project.setnet.ui.data.SelectableNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectionClipboard
{
    private ObservableList<SelectableNode> selectedItems = FXCollections.observableArrayList();

    public ObservableList<SelectableNode> getItems() { return selectedItems; }

    //Function that sets the provided node to be 'selected'
    public void select(SelectableNode n, boolean selected)
    {
        unselectAll();
        if (!selectedItems.contains(n) && selected)
        {
            selectedItems.add(n);
            n.notifySelection(true);
        }
        else
        {
            selectedItems.remove(n);
            n.notifySelection(false);
        }
    }

    //Function that deselects all the nodes that were previously selected
    public void unselectAll()
    {
        for (SelectableNode n: selectedItems) n.notifySelection(false);
        selectedItems.removeAll(selectedItems);
    }
}
