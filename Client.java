package sample;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.concurrent.Task;
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
import javafx.animation.*;
import javafx.util.Duration;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;

import java.util.concurrent.TimeUnit;

import java.net.*;
import java.io.*;

public class Client extends Application {
    private Socket socket;
    private DataOutputStream out;
    Runnable Task = new Task<Object>() {
        @Override
        protected Object call() throws Exception {
            return null;
        }
    };

    int square = 75;
    int checkers[][]={
            {0,1,0,1,0,1,0,1},
            {1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {2,0,2,0,2,0,2,0},
            {0,2,0,2,0,2,0,2},
            {2,0,2,0,2,0,2,0}
    };

    public void start(Stage primaryStage) throws InterruptedException {
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

        draw(canvas, root, square, checkers);
        drawPieces(canvas, root, square, checkers);

        root.getChildren().add(canvas);
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        checkers[2][1] = 0;
        checkers[3][2] = 1;
        //updatePieces(canvas, root);
    }

    public void updatePieces(Canvas canvas, Group root) throws InterruptedException {
        drawPieces(canvas, root, square, checkers);
    }


    public void drawPieces(Canvas canvas, Group root, int square, int[][] checkers){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(checkers[col][row] > 0){
                    if(checkers[col][row] == 1){
                        gc.setFill(Color.RED);
                        gc.setStroke(Color.GRAY);
                    }else if (checkers[col][row] == 2){
                        gc.setFill(Color.WHITESMOKE);
                        gc.setStroke(Color.GRAY);
                    }
                    gc.strokeOval(2+square*row,2+square*col, 71,71);
                    gc.fillOval(2+square*row,2+square*col, 71,71);
                }
            }
        }
    }

    public void draw(Canvas canvas, Group root, int square, int[][] checkers){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,600,600);

        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(600,0,200,600);

        gc.setFill(Color.GRAY);

        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(row % 2 == 0 && col % 2 == 0){
                    gc.fillRect(0+square*row,0+square*col, square,square);
                }
            }
        }

        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(row % 2 == 0 && col % 2 == 0){
                    gc.fillRect(square+square*row,square+square*col, square,square);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
