package model;

import java.io.*;
import java.net.*;
import java.util.Observable;


public class Model extends Observable {
    private MulticastSocket multicastSocket;
    private int port = 6066;
    private String group = "229.255.255.250";
    private Socket client;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;
    private Receiver receiver;
    private int myGameID;
    private int myPlayerID;
    public String[][] grid = new String[3][3];
    private int gameOver;

    public Model(){
        clearGrid();
    }
    public void connect(){
        String serverName = "localhost";
        int port = 6066;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);
            out.writeUTF("CONNECT");
            out.flush();

            //out.writeUTF("Hello from " + client.getLocalSocketAddress());
            inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);

            myGameID = in.read();
            myPlayerID = in.read();
            System.out.println("Your Game ID: " + myGameID);
            System.out.println("Your Player ID:  " + myPlayerID);
            receiver = new Receiver();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private class Receiver{
        Receiver (){
            while(true){
                try{
                    InetAddress groupAdress = InetAddress.getByName(group);
                    multicastSocket = new MulticastSocket(1900);
                    System.out.println("Multicast Reciver running at:" + multicastSocket.getLocalSocketAddress());
                    multicastSocket.joinGroup(groupAdress);

                    DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                    System.out.println("Waiting for a  multicast message...");
                    multicastSocket.receive(packet);
                    String msg = new String(packet.getData(), packet.getOffset(),
                            packet.getLength());
                    System.out.println("[Multicast  Receiver] Received:" + msg);
                    String delims = "[,]";
                    String[] message = msg.split(delims);
                    handleMSG(message);

                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void handleMSG(String[] msg){
        int receivedGameID = Integer.parseInt(msg[0]);
        if(receivedGameID == myGameID){
            int receivedPlayerID = Integer.parseInt(msg[1]);
            int receivedX = Integer.parseInt(msg[2]);
            int receivedY = Integer.parseInt(msg[3]);
            int receivedGameOver = Integer.parseInt(msg[4]);
            if(receivedPlayerID == myPlayerID){
                grid[receivedX][receivedY] = "ME";

            }else{
                grid[receivedX][receivedY] = "OTHER";
            }
            gameOver = receivedGameOver;
            setChanged();
            notifyObservers();
        }

        System.out.println(receivedGameID);
        System.out.println(msg[1]);
        System.out.println(msg[2]);
        System.out.println(msg[3]);

    }

    public void send(){
        String group = "229.255.255.250";
        int port = 1900;

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress groupAdress = InetAddress.getByName(group);
            byte[] msg = "Hello".getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setAddress(groupAdress);
            packet.setPort(port);
            datagramSocket.send(packet);

            System.out.println("Sent a  multicast message.");
            System.out.println("Exiting application");

            datagramSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void restart(){
        try {
            out.writeUTF("RESTART");
            out.flush();
        } catch (SocketException e){
            connect();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect(){
        try {
            out.writeUTF("DISCONNECT");
            out.flush();
            out.write(myGameID);
            out.flush();
            out.write(myPlayerID);
            out.close();
        } catch (SocketException e){
            System.out.println("You are not connected to the Server.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(int x, int y){
        if(gameOver == 0){
            try {
                out.writeUTF("MOVE");
                out.write(myGameID);
                out.write(myPlayerID);
                out.write(x);
                out.write(y);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("THE GAME IS OVER");
        }

        //Send request to make a move at x,y
    }

    public void clearGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int n = 0; n < grid[i].length; n++){
                grid[i][n] = "EMPTY";
            }
        }
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
