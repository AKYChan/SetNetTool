package csc3095.project.setnet.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProgramInitiator extends Application
{
    private final static double WINDOW_WIDTH = 800.0, WINDOW_HEIGHT = 550.0;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("setnet_main.fxml"));
        primaryStage.setTitle("Set-net Modelling Language tool");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setMinWidth(WINDOW_WIDTH);
        primaryStage.setMinHeight(WINDOW_HEIGHT);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}