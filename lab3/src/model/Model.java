package model;



import java.io.*;
import java.net.Socket;
import java.util.Observable;


public class Model extends Observable {
    public Model(){

    }

    public void connect(){
        String serverName = "localhost";
        int port = 6066;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println("Server says " + in.read());

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void move(int x, int y){
        //Send request to make a move at x,y
    }



    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }

}

/**SKit som ska till Server
 * public final int EMPTY = 0;
 public final int ME = 1;
 public final int OTHER = 2;
 private int inrow = 0;
 int[][] grid;
 private final int NOT_STARTED = 0;
 private final int MY_TURN = 1;
 private final int OTHER_TURN = 2;
 private final int FINISHED = 3;

 public Model(){
 grid = new int[3][3];
 clearGrid();
 }

 //Move to Server
 public void clearGrid(){
 for(int i = 0; i < grid.length; i++){
 for(int n = 0; n < grid[i].length; n++){
 grid[i][n] = EMPTY;
 }
 }
 }
 */
