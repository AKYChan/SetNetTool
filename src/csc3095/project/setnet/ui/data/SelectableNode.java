package csc3095.project.setnet.ui.data;

public interface SelectableNode
{
    //Function for the object to request the user's selection
    default boolean requestSelection() { return true; }

    //Function for the object to change its state based on the selection
    void notifySelection(boolean selected);
}
