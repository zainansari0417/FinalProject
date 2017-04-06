package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import java.net.*;
import java.io.*;

public class Client extends Application {
    private Socket socket;
    private DataOutputStream out;

    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        //try to connect to server
        try {
            socket = new Socket("localhost", 4444);
            //init output stream
            out = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
