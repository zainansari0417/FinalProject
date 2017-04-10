package sample;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.*;
import java.io.*;
import java.net.*;

//Client for 2nd player, ie the Hounds
public class Client2 extends Application {
    private Socket socket;
    private DataOutputStream out;

    //variable where fox position would be stored if read from Server
    int foxNewPosX;

    public int square = 75;
    //checkerboard array
    public int checkers[][]={
            {0,1,0,1,0,1,0,1},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,2,0,0,0}
    };

    //original board for use when reset
    public int ogCheckers[][] = checkers;

    //original Hound position
    int currentX = 7;
    int currentY = 0;

    Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();


    public void start(Stage primaryStage) throws InterruptedException {
        Group root = new Group();

        canvas.setWidth(800);
        canvas.setHeight(600);

        //new game button
        Button newGameButton = new Button("New Game");
        newGameButton.setMinSize(200, 100);
        newGameButton.setLayoutX(600);

        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("NEW GAME");
                checkers = ogCheckers;
                ClientThread.drawPieces(gc, square, checkers);
                currentX = 7;
                currentY = 0;

            }
        });

        //game over button
        Button gameOverButton = new Button("Game Over");
        gameOverButton.setMinSize(200, 100);
        gameOverButton.setLayoutX(600);
        gameOverButton.setLayoutY(100);

        //exits game
        gameOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("GAME OVER");
                System.exit(1);
            }
        });

        //try to connect to server
        try {
            socket = new Socket("localhost", 4444);
            //init output stream
            out = new DataOutputStream(socket.getOutputStream());


        } catch(IOException e) {
            e.printStackTrace();
        }


        //On mouse click
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        //if mouse was clicked on a piece, allow it to move
                        if (e.getSceneX() > getHoundY(checkers) && (e.getSceneX() < (getHoundY(checkers)+75))
                                && (e.getSceneY() > getHoundX(checkers) && (e.getSceneY() < getHoundX(checkers)+75))) {

                            canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                    new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            double mY = event.getSceneY();
                                            double mX = event.getSceneX();
                                            if (isLegal(getMoveHoundX(mY), getMoveHoundY(mX), currentX, currentY)) {
                                                moveHound(checkers, getMoveHoundX(mY), getMoveHoundY(mX), currentX, currentY);
                                                currentX = (int) getMoveHoundX(mX);
                                                currentY = (int) getMoveHoundY(mY);
                                            }
                                            System.out.println(foxNewPosX);

                                            ClientThread.drawPieces(gc, square, checkers);
                                        }
                                    });


                        }
                    }
                });

        //draw board
        draw(gc, square, checkers);

        root.getChildren().add(canvas);
        root.getChildren().add(newGameButton);
        root.getChildren().add(gameOverButton);
        primaryStage.setTitle("Fox and Hounds - Hound client");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }

    //draw board
    public void draw(GraphicsContext gc, int square, int[][] checkers){


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


    //move hound postion on array and remove old position
    public void moveHound(int checkers[][], int newX, int newY, int currentX, int currentY){
        checkers[currentY][currentX] = 0;
        checkers[newX][newY] = 1;

        //erase old hound
        gc.setFill(Color.BLACK);
        gc.fillRect(square*currentX,square*currentY, 75,75);
    }

    //checks if move is legal
    //would also check if Fox was in the way if Server information came through
    public boolean isLegal(int newX, int newY, int currentX, int currentY){
        if (newX > 7 || newY > 7){
            return false;
        } else if ((currentX+1 == newY && currentY+1 == newX) || (currentX-1 == newY && currentY+1 == newX)){
            return true;
        } else {
            return false;
        }
    }

    public int getMoveHoundX(double moveX){
        return (int) Math.floor(moveX/75);
    }

    public int getMoveHoundY(double moveY){
        return (int) Math.floor(moveY/75);
    }

    public int getHoundX(int checkers[][]){
        int HoundX = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(checkers[i][j] == 1){
                    HoundX = i*75;
                }
            }
        }
        if (HoundX == 0){
            return 0;
        } else {
            return HoundX;
        }
    }

    public int getHoundY(int checkers[][]){
        int HoundY = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(checkers[i][j] == 1){
                    HoundY = j*75;
                }
            }
        }
        if (HoundY == 0){
            return 0;
        } else {
            return HoundY;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
