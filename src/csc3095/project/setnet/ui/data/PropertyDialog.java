package csc3095.project.setnet.ui.data;

import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;
import java.net.URL;

public class PropertyDialog extends Dialog
{
    private final URL path = getClass().getResource("property_main.fxml");
    private final Label nodeNameLabel;
    private final TextField nodeNameValue;
    private final Label tokenLabel;
    private final ComboBox tokenOption;
    private final Button updateButton;

    private enum TokenSetter
    {
        TOKEN
                {
                    @Override
                    void setToken(Place p) { p.addToken(); }

                    @Override
                    public String toString() { return "Yes"; }
                },
        NO_TOKEN
                {
                    @Override
                    void setToken(Place p) { p.removeToken(); }

                    @Override
                    public String toString() { return "No"; }
                };

        abstract void setToken(Place p);
    }

    public PropertyDialog(Setnet setnet, TextArea console, Node node) throws Exception
    {
        FXMLLoader dialogLoader = new FXMLLoader(path);

        setTitle("Property");
        setDialogPane(dialogLoader.load());

        nodeNameLabel = (Label) findComponent("#nodeNameLabel");
        nodeNameValue = (TextField) findComponent("#nodeNameValue");
        tokenLabel = (Label) findComponent("#tokenLabel");
        tokenOption = (ComboBox) findComponent("#tokenOption");
        updateButton = (Button) findComponent("#updateButton");

        tokenOption.getItems().addAll(TokenSetter.TOKEN, TokenSetter.NO_TOKEN);

        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(
                e ->
                {
                    window.hide();
                    e.consume();
                });

        if (node instanceof PlaceSymbol)
        {
            PlaceSymbol p = (PlaceSymbol) node;
            nodeNameValue.setText(p.getPlace().getName());

            String originalName = p.getPlace().getName();
            TokenSetter originalSetter;
            if (p.getPlace().hasToken())
            {
                tokenOption.setValue(TokenSetter.TOKEN);
                originalSetter = TokenSetter.TOKEN;
            }
            else
            {
                tokenOption.setValue(TokenSetter.NO_TOKEN);
                originalSetter = TokenSetter.NO_TOKEN;
            }

            updateButton.setOnMousePressed(e ->
            {
                String newName = nodeNameValue.getText();

                boolean updateSuccessful = true;
                for (Place op: setnet.getPlaces()) if (p.getPlace() != op) updateSuccessful = updateSuccessful & !(op.getName().equals(newName));
                for (Transition ot: setnet.getTransitions()) updateSuccessful = updateSuccessful & !(ot.getName().equals(newName));

                if (updateSuccessful)
                {
                    TokenSetter setter = (TokenSetter) tokenOption.getValue();
                    if (!newName.equalsIgnoreCase(originalName))
                    {
                        p.getPlace().setName(newName);
                        console.appendText("Place " + originalName + " has been changed to Place " + newName + "\n");
                    }
                    if (setter != originalSetter)
                    {
                        setter.setToken(p.getPlace());
                        if (setter == TokenSetter.TOKEN) console.appendText("Place " + p.getPlace().getName() + " now has a token added.\n");
                        else if (setter == TokenSetter.NO_TOKEN) console.appendText("Place " + p.getPlace().getName() + " now has a token removed.\n");
                    }
                    p.update();
                    window.hide();
                }
                else displayExistingNameMessage();
            });

            tokenLabel.setVisible(true);
            tokenOption.setVisible(true);
        }
        else if (node instanceof TransitionSymbol)
        {
            TransitionSymbol t = (TransitionSymbol) node;
            nodeNameValue.setText(t.getTransition().getName());

            String originalName = t.getTransition().getName();
            updateButton.setOnMousePressed(e ->
            {
                String newName = nodeNameValue.getText();

                boolean updateSuccessful = true;
                for (Place op: setnet.getPlaces()) updateSuccessful = updateSuccessful & !(op.getName().equals(newName));
                for (Transition ot: setnet.getTransitions()) if (t.getTransition() != ot) updateSuccessful = updateSuccessful & !(ot.getName().equals(newName));

                if (updateSuccessful)
                {
                    if (!newName.equalsIgnoreCase(originalName))
                    {
                        t.getTransition().setName(newName);
                        console.appendText("Transition " + originalName + " has been changed to Transition " + newName + "\n");
                    }
                    t.update(false);
                    window.hide();
                }
                else displayExistingNameMessage();
            });

            tokenLabel.setVisible(false);
            tokenOption.setVisible(false);
        }
    }

    private Node findComponent(String id) { return getDialogPane().getContent().lookup(id); }

    private void displayExistingNameMessage()
    {
        Alert alreadyExists = new Alert(Alert.AlertType.WARNING);
        alreadyExists.setTitle("Error detected");
        alreadyExists.setHeaderText("The given name already exists");
        alreadyExists.setContentText("The name you have provided has already been taken. Please try a different name.");
        alreadyExists.showAndWait();
    }
}