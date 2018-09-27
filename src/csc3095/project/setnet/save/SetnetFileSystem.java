package csc3095.project.setnet.save;

import csc3095.project.setnet.components.Arc;
import csc3095.project.setnet.components.Place;
import csc3095.project.setnet.components.Transition;
import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.ui.symbol.ArcSymbol;
import csc3095.project.setnet.ui.symbol.PlaceSymbol;
import csc3095.project.setnet.ui.symbol.TransitionSymbol;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class SetnetFileSystem
{
    private final static String openTitle = "Open up an existing SET-net file";
    private final static String saveTitle = "Saving current SET-net drawing...";
    private final static FileChooser.ExtensionFilter setnetFilter = new FileChooser.ExtensionFilter("SET-net files (*.snet)", "*.snet");
    private final String path;

    private final Pane currentDrawPane;
    private final TextArea console;
    private Setnet currentSetnet;
    private FileChooser chooser;

    public SetnetFileSystem(Setnet currentSetnet, Pane currentDrawPane, TextArea console, String saveDirectory)
    {
        this.currentSetnet = currentSetnet;
        this.currentDrawPane = currentDrawPane;
        this.console = console;
        this.path = ".\\" + saveDirectory;
    }

    public Setnet openDrawing(Setnet setnet)
    {
        initialiseChooser(openTitle);
        File file = chooser.showOpenDialog(null);
        if (file != null && file.getPath().endsWith(".snet")) //Continue with opening the file, if the user has chosen a non-null file
        {
            Alert toOpen = new Alert(Alert.AlertType.CONFIRMATION);
            toOpen.setTitle("Attempted opening of a SET-net file");
            toOpen.setHeaderText("You are now about to open up another SET-net file, which causes your current SET-net drawing to be cleared");
            toOpen.setContentText("Are you sure you want to continue? Any unsaved work will be lost.");

            Optional<ButtonType> confirmation = toOpen.showAndWait();
            if (confirmation.get() == ButtonType.OK)
            {
                currentDrawPane.getChildren().clear();
                currentSetnet = XMLHandler.loadSetnetDataFromFile(file);

                for (Place place: currentSetnet.getPlaces())
                {
                    PlaceSymbol p = new PlaceSymbol(place);
                    currentDrawPane.getChildren().add(p);
                }

                for (Transition transition: currentSetnet.getTransitions())
                {
                    TransitionSymbol t = new TransitionSymbol(transition);
                    currentDrawPane.getChildren().add(t);
                }

                for (Arc a: currentSetnet.getArcs())
                {
                    PlaceSymbol placeSymbol = null;
                    TransitionSymbol transitionSymbol = null;

                    for (Node n: currentDrawPane.getChildren())
                    {
                        if (n instanceof PlaceSymbol)
                        {
                            PlaceSymbol p = (PlaceSymbol) n;
                            if (a.getPlace() == p.getPlace())
                            {
                                placeSymbol = p;
                                break;
                            }
                        }
                    }

                    for (Node n: currentDrawPane.getChildren())
                    {
                        if (n instanceof TransitionSymbol)
                        {
                            TransitionSymbol t = (TransitionSymbol) n;
                            if (a.getTransition() == t.getTransition())
                            {
                                transitionSymbol = t;
                                break;
                            }
                        }
                    }

                    if (placeSymbol != null && transitionSymbol != null)
                    {
                        ArcSymbol arcSymbol = new ArcSymbol(a, placeSymbol, transitionSymbol);
                        currentDrawPane.getChildren().add(arcSymbol);
                        arcSymbol.toBack();
                    }
                }
                return currentSetnet;
            }
        }
        else if (file != null && !file.getPath().endsWith(".snet"))
        {
            console.appendText("A non-.snet file has been detected and the operation has been aborted. Please make sure to open up a .snet file.");
        }
        else
        {
            console.appendText("The opening file operation has been aborted.\n");
        }
        return setnet; //Return itself as the tool did not open another file
    }

    public void saveDrawing()
    {
        initialiseChooser(saveTitle);

        File file = chooser.showSaveDialog(null);

        if (file != null)
        {
            if (!file.getPath().endsWith(".snet")) file = new File(file.getPath() + ".snet");
            XMLHandler.saveSetnetDataToFile(file, currentSetnet);
            console.appendText("The file has been successfully saved.\n");
        }
        else
        {
            console.appendText("The saving file operation has been aborted\n");
        }
    }

    private void initialiseChooser(String title)
    {
        chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(path));
        chooser.getExtensionFilters().add(setnetFilter);
    }
}