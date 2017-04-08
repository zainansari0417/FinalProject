package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.shape.ArcType;

import java.net.*;
import java.io.*;

public class Client extends Application {
    private Socket socket;
    private DataOutputStream out;

    public void start(Stage primaryStage) {
        Group root = new Group();
        Canvas canvas = new Canvas();
        canvas.setWidth(800);
        canvas.setHeight(600);

        //try to connect to server
        try {
            socket = new Socket("localhost", 4444);
            //init output stream
            out = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        draw(canvas, root);

        root.getChildren().add(canvas);
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void draw(Canvas canvas, Group root){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,600,600);

        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(600,0,200,600);

        gc.setFill(Color.GRAY);

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(i % 2 == 0 && j % 2 == 0){
                    gc.fillRect(0+75*i,0+75*j, 75,75);
                }
            }
        }

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(i % 2 == 0 && j % 2 == 0){
                    gc.fillRect(75+75*i,75+75*j, 75,75);
                }
            }
        }

        gc.setFill(Color.RED);
        gc.setStroke(Color.GRAY);

        for(int i=0; i<8; i++){
            for(int j=0; j<4; j++){
                if(i % 2 == 0 && j % 2 == 0){
                    gc.strokeOval(77+75*i,2+75*j, 71,71);
                    gc.fillOval(77+75*i,2+75*j, 71,71);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
