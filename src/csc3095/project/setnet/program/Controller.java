package csc3095.project.setnet.program;

import csc3095.project.setnet.main.Setnet;
import csc3095.project.setnet.main.SetnetStateSaver;
import csc3095.project.setnet.save.SetnetFileSystem;
import csc3095.project.setnet.ui.firing.FiringHandler;
import csc3095.project.setnet.ui.drawing.DrawHandler;
import csc3095.project.setnet.ui.firing.FiringState;
import csc3095.project.setnet.ui.firing.StateChanger;
import csc3095.project.setnet.ui.select.SelectionHandler;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Controller
{
    //The components of the tool's UI
    public Pane drawingPane;
    public ScrollPane drawingPaneParent;
    public ToggleButton autoRunButton, autoMaxRunButton, maxRunButton, runButton, selectButton, arcButton, placeButton, transitionButton;
    public Button clearConsoleButton, backStepButton, forwardStepButton;
    public MenuItem newMenuItem, openMenuItem, saveMenuItem, verifyMenuItem, helpMenuItem, aboutMenuItem;
    public TextArea console, stepConsole;

    //The necessary 'mathematical' model of the SET-net and the handlers used to make the tool run
    private Setnet setnet = new Setnet();
    private DrawHandler drawHandler;
    private SelectionHandler selectionHandler;
    private FiringHandler firingHandler;
    private SetnetFileSystem setnetFileSystem;
    private SetnetStateSaver setnetStateSaver;

    //Values used for the determining the tool's save path and automatic firing times. Both user based and default based included.
    private final static Properties USER_PROPERTIES = new Properties();
    private final static String DEFAULT_PATH = ".\\SET-net files";
    private final static int DEFAULT_AUTO_RUN_TIME = 3000;

    //The final values used by the tool itself
    private static String ENV_USER_PATH;
    private static int ENV_USER_AUTO_RUN_TIME;

    //New menu item function for creating new SET-net diagrams
    public void onNew()
    {
        Alert toCreate = new Alert(Alert.AlertType.CONFIRMATION);
        toCreate.setTitle("SET-net creation confirmation");
        toCreate.setHeaderText("You are now about to create a new SET-net diagram.");
        toCreate.setContentText("Are you sure you want to continue? Any unsaved work will be lost.");

        Optional<ButtonType> confirmation = toCreate.showAndWait();
        if (confirmation.get() == ButtonType.OK)
        {
            drawingPane.getChildren().clear();
            setnet = new Setnet();
            drawHandler = new DrawHandler(setnet, drawingPane, console);
            selectionHandler = new SelectionHandler(setnet, console, drawingPane);
            firingHandler = new FiringHandler(setnet, drawingPane, stepConsole, setnetStateSaver, backStepButton, forwardStepButton);
            setnetFileSystem = new SetnetFileSystem(setnet, drawingPane, console, ENV_USER_PATH);
            StateChanger.loadSetnet(setnet);
            console.setText("A new file has been created.\nSET-net tool re-initiated.\n");
            stepConsole.setText("");

            resetButtons();
            resetState();
        }
        toCreate.close();
    }

    //Open menu item function for opening up an existing SET-net file stored elsewhere
    public void onOpen()
    {
        Setnet tempSetnet = setnetFileSystem.openDrawing(setnet);
        if (tempSetnet != setnet)
        {
            setnet = tempSetnet;
            drawHandler = new DrawHandler(setnet, drawingPane, console);
            selectionHandler = new SelectionHandler(setnet, console, drawingPane);
            firingHandler = new FiringHandler(setnet, drawingPane, stepConsole, setnetStateSaver, backStepButton, forwardStepButton);
            setnetFileSystem = new SetnetFileSystem(setnet, drawingPane, console, ENV_USER_PATH);
            StateChanger.loadSetnet(setnet);
            console.setText("The file has been successfully opened.\nSET-net tool re-initiated.\n");
            stepConsole.setText("");
        }
    }

    public void onSave() { setnetFileSystem.saveDrawing(); }

    //Verify menu item function for determining if the SET-net contains any deadlocks
    public void onVerify()
    {
        String message = "";
        if (setnet.verifyDeadlock()) message = "This SET-net has a deadlock.";
        else message = "This SET-net is deadlock-free.";

        console.setText(console.getText() + message + "\n");
        Alert verification = new Alert(Alert.AlertType.INFORMATION);
        verification.setTitle("Deadlock-freeness verification");
        verification.setHeaderText("Deadlock-freeness check for current SET-net");
        verification.setContentText(message);
        verification.showAndWait();
    }

    //Help menu item function for displaying some tool instructions
    public void onHelp()
    {
        Alert toInform = new Alert(Alert.AlertType.INFORMATION);
        toInform.setTitle("Help");
        toInform.setHeaderText("Instructions for the SET-net tool");
        toInform.setContentText("The tool has the following features, which the user can use within this tool. These include:\n\n" +
                                "Creating new diagrams, saving diagrams, opening diagrams, verifying diagrams, drawing out diagrams with the use of the arc, transition and place buttons " +
                                "and lastly, simulating the diagram by firing it sequentially and maximally (with both manual and automatic options available to the user) where the console on the right " +
                                "tells the user what transition they have selected to fire and even go back a few steps (and forwards) from there, when necessary.\n\n" +
                                "To add tokens, you can right-click on to the places and update adding a token to it (as well as changing its name). The name of the transition can be changed in the same way.\n\n" +
                                "If there are any errors, you can also select the 'mis-placed' component and press the 'DEL' button to remove it from the diagram.\n\n" +
                                "For the user's reference, a console has been provided at the bottom of the tool which will give the user some insight on what is happening currently in the tool and to provide minor " +
                                "error feedback like connecting two places or transitions together.\n\n" +
                                "When simulating the diagram, you will not be able to create a new diagram, save the diagram, open a diagram or verify the diagram. To re-gain these features, click on another tool button " +
                                "to get off the firing modes.\n\n" +
                                "Note: There are features, which are not yet incorporated such as using 'CTRL+S' for saving files due to the original time constraints the author had. For this purpose, this project " +
                                "has been left in a state where one can develop more for the tool's UI and potentially its future features like maximal locality checks. More information on this can be found with " +
                                "the written dissertation that comes with this tool.\n\n");
        toInform.showAndWait();
    }

    //About menu item function for displaying information about the tool, including project name, supervisor name and (original) author name
    public void onAbout()
    {
        Alert toInform = new Alert(Alert.AlertType.INFORMATION);
        toInform.setTitle("About SET-net tool");
        toInform.setHeaderText("Information about this tool");
        toInform.setContentText("SET-net modelling tool\n\nVersion 1.0.0\n\nDissertation project 2017-18 academic year\n" +
                                "Supervisor: Prof. Maciej Koutny\nOriginal Author: Alex Kai Yin Chan\nStudent number: 140345458\n" +
                                "Dissertation project title: A modelling language tool that investigates and demonstrates the properties of a Set-net\n\n" +
                                "Project development year: 2018");
        toInform.showAndWait();
    }

    //Auto-run button function for automatically simulating the sequential firing sequence of the SET-net
    public void onAutoRun() throws InterruptedException
    {
        disableDiagramMenuItems();
        Thread.sleep(ENV_USER_AUTO_RUN_TIME);
        setnet.seqFire();
        StateChanger.updatePlacesAndTransitions(drawingPane, FiringState.SEQUENTIAL_FIRING);
    }

    //Auto-max-run button function for automatically simulating the maximal firing sequence of the SET-net
    public void onAutoMaxRun() throws InterruptedException
    {
        disableDiagramMenuItems();
        Thread.sleep(ENV_USER_AUTO_RUN_TIME);
        setnet.maxFire();
        StateChanger.updatePlacesAndTransitions(drawingPane, FiringState.MAXIMAL_FIRING);
    }

    //Run button function for simulating the sequential firing sequence of the SET-net
    public void onSeqFire()
    {
        resetState();
        if (runButton.isSelected())
        {
            setnetStateSaver.changeFiringState(FiringState.SEQUENTIAL_FIRING);
            setnetStateSaver.saveState(drawingPane);
            firingHandler.resetFiredTransitionsList();
            StateChanger.setEnabledTransitions(drawingPane,true);
            drawingPane.setOnMousePressed(firingHandler.getPressedSeqHandler());
            backStepButton.setOnMousePressed(firingHandler.getPressedBackHandler());
            forwardStepButton.setOnMousePressed(firingHandler.getPressedForwardHandler());
            disableDiagramMenuItems();
        }
    }

    //Max run button function for simulating the maximal firing sequence of the SET-net
    public void onMaxFire()
    {
        resetState();
        if (maxRunButton.isSelected())
        {
            setnetStateSaver.changeFiringState(FiringState.MAXIMAL_FIRING);
            setnetStateSaver.saveState(drawingPane);
            firingHandler.resetFiredTransitionsList();
            StateChanger.setEnabledTransitions(drawingPane,true);
            drawingPane.setOnMousePressed(firingHandler.getPressedMaxHandler());
            backStepButton.setOnMousePressed(firingHandler.getPressedBackHandler());
            forwardStepButton.setOnMousePressed(firingHandler.getPressedForwardHandler());
            disableDiagramMenuItems();
        }
    }

    //Select button function for providing the user the functionality to select the drawn out components, editing them and deleting them as necessary
    public void onSelect()
    {
        resetState();
        if (selectButton.isSelected())
        {
            drawingPane.requestFocus();
            drawingPane.setOnMousePressed(selectionHandler.getPressedHandler());
            drawingPane.setOnMouseDragged(selectionHandler.getDraggedHandler());
            drawingPane.setOnKeyPressed(selectionHandler.getDeleteHandler());
        }
    }

    //Arc button function for providing the user the functionality to connect places and transitions with arcs
    public void onArc()
    {
        resetState();
        if (arcButton.isSelected())
        {
            drawingPane.setCursor(Cursor.CROSSHAIR);
            drawingPane.setOnMouseClicked(drawHandler.getOnClickDrawArc());
        }
    }

    //Arc button function for providing the user the functionality to create places in the diagram
    public void onPlace()
    {
        resetState();
        if (placeButton.isSelected())
        {
            drawingPane.setCursor(Cursor.CROSSHAIR);
            drawingPane.setOnMouseClicked(drawHandler.getOnClickDrawPlace());
        }
    }


    //Arc button function for providing the user the functionality to create transitions in the diagram
    public void onTransition()
    {
        resetState();
        if (transitionButton.isSelected())
        {
            drawingPane.setCursor(Cursor.CROSSHAIR);
            drawingPane.setOnMouseClicked(drawHandler.getOnClickDrawTransition());
        }
    }

    //Clear console button for clearing the console output
    public void onClearConsole() { console.setText(""); }

    //Function for resetting all the buttons to an 'idle' state - i.e. all of them set to not be enabled
    private void resetButtons()
    {
        maxRunButton.setSelected(false);
        runButton.setSelected(false);
        selectButton.setSelected(false);
        arcButton.setSelected(false);
        placeButton.setSelected(false);
        transitionButton.setSelected(false);
    }

    //Function for resetting the state of the program, which includes refreshing the drawing functionalities applied when pressing the place button for example
    private void resetState()
    {
        selectionHandler.clearSelected();
        drawingPane.setOnMousePressed(e ->{});
        drawingPane.setOnMouseDragged(e ->{});
        drawingPane.setOnMouseClicked(e -> {});
        drawingPane.setOnMouseDragged(e -> {});
        drawingPane.setOnKeyPressed(e -> {});
        drawingPane.setCursor(Cursor.DEFAULT);
        backStepButton.setOnMousePressed(e -> {});
        forwardStepButton.setOnMousePressed(e -> {});
        drawHandler.clearState();

        newMenuItem.setDisable(false);
        openMenuItem.setDisable(false);
        saveMenuItem.setDisable(false);
        verifyMenuItem.setDisable(false);

        setnetStateSaver.resetState(drawingPane, stepConsole, backStepButton, forwardStepButton);
        StateChanger.setEnabledTransitions(drawingPane, false);
    }

    //Function to disable the new, open, save and verify menu items. Mainly used when the diagram is being simulated
    private void disableDiagramMenuItems()
    {
        newMenuItem.setDisable(true);
        openMenuItem.setDisable(true);
        saveMenuItem.setDisable(true);
        verifyMenuItem.setDisable(true);
    }

    //Function to load in the user preferences that is read in as a configuration file
    private void loadUserSettings()
    {
        try
        {
            FileInputStream in = new FileInputStream("config.ini");
            USER_PROPERTIES.load(in);
            ENV_USER_PATH = USER_PROPERTIES.getProperty("SAVE_DIRECTORY");
            ENV_USER_AUTO_RUN_TIME = Integer.parseInt(USER_PROPERTIES.getProperty("AUTO_FIRE_TIME"));
        }
        catch (FileNotFoundException fnfe)
        {
            console.appendText("Configuration file was not found. Defaulting program settings.\n");
            ENV_USER_PATH = DEFAULT_PATH;
            ENV_USER_AUTO_RUN_TIME = DEFAULT_AUTO_RUN_TIME;
        }
        catch (IOException io)
        {
            console.appendText("There was an error with the configuration file. Defaulting program settings.\n");
            ENV_USER_PATH = DEFAULT_PATH;
            ENV_USER_AUTO_RUN_TIME = DEFAULT_AUTO_RUN_TIME;
        }
    }

    //Function for initialising the program
    @FXML
    public void initialize()
    {
        loadUserSettings();
        setnetStateSaver = new SetnetStateSaver();
        drawHandler = new DrawHandler(setnet, drawingPane, console);
        selectionHandler = new SelectionHandler(setnet, console, drawingPane);
        setnetFileSystem = new SetnetFileSystem(setnet, drawingPane, console, ENV_USER_PATH);
        firingHandler = new FiringHandler(setnet, drawingPane, stepConsole, setnetStateSaver, backStepButton, forwardStepButton);
        StateChanger.loadSetnet(setnet);
        console.setText("SET-net tool initiated.\n");

        //The following interface template was referenced (and later used) from a StackOverflow post: https://stackoverflow.com/questions/26388527/how-do-i-make-my-system-wait-5-seconds-before-continuing
        //This function features the 'handler' for the automatic sequential firing button
        Service<Void> toggleableAutoSeq = new Service<Void>()
        {
            @Override
            protected Task<Void> createTask()
            {
                return new Task<Void>()
                {
                    @Override
                    protected Void call() throws Exception
                    {
                        while (!isCancelled()) onAutoRun();
                        return null;
                    }
                };
            }
        };
        autoRunButton.selectedProperty().addListener((obs, oVal, nVal) ->
        {
            if (nVal)
            {
                resetState();
                setnetStateSaver.saveState(drawingPane);
                StateChanger.setEnabledTransitions(drawingPane, true);
                toggleableAutoSeq.reset();
                toggleableAutoSeq.start();
            }
            else
            {
                toggleableAutoSeq.cancel();
                resetState();
            }
        });

        //Likewise, this function features the 'handler' for the automatic maximal firing button
        Service<Void> toggleableAutoMax = new Service<Void>()
        {
            @Override
            protected Task<Void> createTask()
            {
                return new Task<Void>()
                {
                    @Override
                    protected Void call() throws Exception
                    {
                        while (!isCancelled()) onAutoMaxRun();
                        return null;
                    }
                };
            }
        };
        autoMaxRunButton.selectedProperty().addListener((obs, oVal, nVal) ->
        {
            if (nVal)
            {
                resetState();
                setnetStateSaver.saveState(drawingPane);
                StateChanger.setEnabledTransitions(drawingPane, true);
                toggleableAutoMax.reset();
                toggleableAutoMax.start();
            }
            else
            {
                toggleableAutoMax.cancel();
                resetState();
            }
        });
    }
}
