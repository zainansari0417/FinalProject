package sample;

import java.net.*;
import java.io.*;
import java.lang.Thread;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

//client thread
public class ClientThread extends Thread {
    private Socket clientSocket;
    private DataInputStream in;
    Client client = new Client();
    //Fox position to send to server
    public int FoxPosX;
    public int FoxPosY;

    public ClientThread(Socket clientSocket, int FoxPosX, int FoxPosY) {
        this.FoxPosX = FoxPosX;
        this.FoxPosY = FoxPosY;
        this.clientSocket = clientSocket;
        try {
            in = new DataInputStream(this.clientSocket.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Goes through array to see which spots have pieces, then draws them
    public static void drawPieces(GraphicsContext gc, int square, int[][] checkers){

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

    public void run(){
        /*
        while(true) {
            try {
                System.out.println("Working.");
                //Takes the X and Y positions of Fox from client 1 to send to the server
                int posX = in.readInt();
                int posY = in.readInt();
                FoxPosX = posX;
                FoxPosY = posY;
            } catch(EOFException eof) {
                //client has disconnected
                try {
                    in.close();
                    clientSocket.close();
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
                break;
            } catch(IOException e) {
                e.printStackTrace();
            }
        } */
    }

}


