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
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.shape.ArcType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.concurrent.*;
import java.lang.Thread;
import java.io.*;
import java.net.*;

//Client for the first player, ie the Fox
public class Client extends Application {
    private Socket socket;
    private DataOutputStream out;

    //the size of each square on the board
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

    //saving the original board for reset later
    public int ogCheckers[][] = checkers;

    //initial Fox position
    int currentX = 4;
    int currentY = 7;

    Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();

    public void start(Stage primaryStage) throws InterruptedException, IOException {
        Group root = new Group();

        canvas.setWidth(800);
        canvas.setHeight(600);

        //new game button
        Button newGameButton = new Button("New Game");
        newGameButton.setMinSize(200, 100);
        newGameButton.setLayoutX(600);

        //resets checkerboard as well as Fox and Hound positions
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("NEW GAME");
                currentX = 4;
                currentY = 7;
                checkers = ogCheckers;
                ClientThread.drawPieces(gc, square, checkers);

            }
        });

        //game over button
        Button gameOverButton = new Button("Game Over");
        gameOverButton.setMinSize(200, 100);
        gameOverButton.setLayoutX(600);
        gameOverButton.setLayoutY(100);

        //exits the game
        gameOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("GAME OVER");
                System.exit(1);

            }
        });

        //shows winner at the end of the game
        Label winner = new Label("");
        winner.setMinSize(100, 100);
        winner.setLayoutY(300);
        winner.setLayoutX(620);

        //try to connect to server
        try {
            socket = new Socket("localhost", 4444);
            //init output stream
            out = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        //Sends fox position to server, server will then send it to client 2
        //out.writeInt(getFoxX(checkers));
        //out.writeInt(getFoxY(checkers));

        DrawService drawService = new DrawService();


        //Checks if fox has won
        if (foxWins(checkers) != true) {

            //checks for mouse click
            canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {

                            //if mouse is clicked on a piece, allow it to be moved
                            if (e.getSceneX() > getFoxY(checkers) && (e.getSceneX() < (getFoxY(checkers) + 75))
                                    && (e.getSceneY() > getFoxX(checkers) && (e.getSceneY() < getFoxX(checkers) + 75))) {

                                canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                        new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {

                                                double mY = event.getSceneY();
                                                double mX = event.getSceneX();
                                                //check if move is legal, then move Fox
                                                if (isLegal(getMoveFoxX(mY), getMoveFoxY(mX), currentX, currentY)) {
                                                    moveFox(checkers, getMoveFoxX(mY), getMoveFoxY(mX), currentX, currentY);
                                                    currentX = (int) getMoveFoxX(mX);
                                                    currentY = (int) getMoveFoxY(mY);
                                                }
                                                //System.out.println("Working");

                                                //Prints all checkerboard values for testing
                                                for (int i = 0; i < 8; i++) {
                                                    for (int j = 0; j < 8; j++) {
                                                        System.out.println("" + i + j + "= " + checkers[i][j]);
                                                    }
                                                }
                                                //draws the moved piece
                                                ClientThread.drawPieces(gc, square, checkers);

                                                if(foxWins(checkers) == true){
                                                    winner.setText("FOX WINS");
                                                }
                                            }
                                        });


                            }
                        }
                    });
        } else {
            winner.setText("FOX WINS!");
        }

        //draws the board
        draw(gc, square, checkers);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                //drawService.start();
            }
        }.start();

        root.getChildren().add(canvas);
        root.getChildren().add(newGameButton);
        root.getChildren().add(gameOverButton);
        root.getChildren().add(winner);
        primaryStage.setTitle("Fox and Hounds - Fox Client");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }

    class DrawService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                public Void call() {
                    while (true) {
                        ClientThread.drawPieces(gc, square, checkers);
                    }
                }
            };
        }
        @Override
        protected void succeeded() {
            reset();
        }
    }

    //draws the board
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

    //changes array values to move fox and remove it from its previous postion
    public void moveFox(int checkers[][], int newX, int newY, int currentX, int currentY){
        checkers[currentY][currentX] = 0;
        checkers[newX][newY] = 2;

        //erases old piece
        gc.setFill(Color.BLACK);
        gc.fillRect(square*currentX,square*currentY, 75,75);
    }

    //checks if move is legal
    public boolean isLegal(int newX, int newY, int currentX, int currentY){
        if (newX > 7 || newY > 7){
            return false;
        } else if ((currentX-1 == newY && currentY-1 == newX) || (currentX-1 == newY && currentY+1 == newX)
                || (currentX+1 == newY && currentY-1 == newX) || (currentX+1 == newY && currentY+1 == newX)){
            return true;
        } else {
            return false;
        }
        //Would also check if a Hound is taking up a legal spot for Fox
    }

    //If Fox reaches end of the board, Fox wins
    public boolean foxWins(int checkers[][]){
        if(getFoxX(checkers) == 0){
            return true;
        } else {
            return false;
        }
    }

    //Fox X positon for new move
    public int getMoveFoxX(double moveX){
        return (int) Math.floor(moveX/75);
    }

    //Fox Y position for new move
    public int getMoveFoxY(double moveY){
        return (int) Math.floor(moveY/75);
    }

    //Fox X position
    public int getFoxX(int checkers[][]){
        int foxX = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(checkers[i][j] == 2){
                    foxX = i*75;
                }
            }
        }
        if (foxX == 0){
            return 0;
        } else {
            return foxX;
        }
    }

    //Fox Y position
    public int getFoxY(int checkers[][]){
        int foxY = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(checkers[i][j] == 2){
                    foxY = j*75;
                }
            }
        }
        if (foxY == 0){
            return 0;
        } else {
            return foxY;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
