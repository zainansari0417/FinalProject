package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.*;
import java.io.*;

public class Server extends Application {

    private ServerSocket server;
    private final TextArea bulletin = new TextArea();

    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();

        //start server
        try {
            server = new ServerSocket(4444);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
