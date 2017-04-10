package sample;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.*;
import java.io.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.*;

public class Server extends Application {

    private ServerSocket server;
    //private Socket socket;
    //private ObjectOutputStream outToClient2;
    //Client client = new Client();

    //Takes fox position from client 1
    public int foxPosX;
    public int foxPosY;

    //Takes hound postion from client 2
    private int houndPosX;
    private int houndPosY;

    public void start(Stage primaryStage) throws InterruptedException {
        GridPane root = new GridPane();

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 300, 100));
        primaryStage.show();

        //start server
        try {
            server = new ServerSocket(4444);
            //outToClient2 = new ObjectOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        //client thread task
        Runnable task = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                while(true) {
                    try {
                        Socket client = server.accept();
                        new Thread(new ClientThread(client, foxPosX, foxPosY)).start();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(task).start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
