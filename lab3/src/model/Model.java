package model;

import java.io.*;
import java.net.*;
import java.util.Observable;
/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */

public class Model extends Observable {
    private MulticastSocket multicastSocket;
    private String group = "229.255.255.250";
    private String serverName;
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
    private boolean connected = false;

    /**
     * Creates a model and clear the game field.
     */
    public Model(){
        clearGrid();

    }

    /**
     * Connects to a server and creates a multicast receiver.
     */
    public void connect(String ip, int port){
        if(!(connected)){
            connected = true;
            serverName = ip;
            try {
                System.out.println("Connecting to " + serverName + " on port " + port);
                client = new Socket(serverName, port);

                System.out.println("Just connected to " + client.getRemoteSocketAddress());
                outToServer = client.getOutputStream();
                out = new DataOutputStream(outToServer);
                out.writeUTF("CONNECT");
                out.flush();

                inFromServer = client.getInputStream();
                in = new DataInputStream(inFromServer);

                myGameID = in.read();
                myPlayerID = in.read();
                System.out.println("Your Game ID: " + myGameID);
                System.out.println("Your Player ID:  " + myPlayerID);


            }catch (IOException e){
                System.out.println("Could not connect to the server.");
            }
        }

    }


    private class Receiver{
        private String group;
        private int port;
        Receiver (String g, int p){
            group = g;
            port =  p;
            while(true){
                try{
                    InetAddress groupAddress = InetAddress.getByName(group);
                    multicastSocket = new MulticastSocket(port);
                    //System.out.println("Multicast Reciver running at:" + multicastSocket.getLocalSocketAddress());
                    multicastSocket.joinGroup(groupAddress);

                    DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                    System.out.println("Waiting for a  multicast message...");
                    multicastSocket.receive(packet);
                    String msg = new String(packet.getData(), packet.getOffset(),
                            packet.getLength());
                    System.out.println("[Multicast  Receiver] Received:" + msg);
                    String delims = "[,]";
                    String[] message = msg.split(delims);
                    try{
                        handleMSG(message);
                    }catch(NumberFormatException e){

                    }

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
            if(receivedX == -1 && receivedY == -1) clearGrid();
            if(receivedPlayerID == myPlayerID){
                if(!(receivedX == -1 && receivedY == -1)) grid[receivedX][receivedY] = "ME";

            }else{
                if(!(receivedX == -1 && receivedY == -1)) grid[receivedX][receivedY] = "OTHER";
            }
            gameOver = receivedGameOver;
            setChanged();
            notifyObservers();
        }


    }

    /**
     * Sends a request to the multicast group 239.255.255.250 port 1900 to see which servers are on
     */
    public void send(){
        String group = "239.255.255.250";
        int port = 1900;

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress groupAddress = InetAddress.getByName(group);
            byte[] msg = "REQUEST".getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setAddress(groupAddress);
            packet.setPort(port);
            datagramSocket.send(packet);

            System.out.println("Sent a  multicast message.");

            datagramSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Sends a request to restart the game
     */
    public void restart(){
        try {
            out.writeUTF("RESTART");
            out.flush();
            out.write(myGameID);
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startMulticast(){
        receiver = new Receiver(group, 6745);
    }

    /**
     * Sends a request to disconnect
     */
    public void disconnect(){
        try {
            connected = false;
            out.writeUTF("DISCONNECT");
            out.flush();
            out.write(myGameID);
            out.flush();
            out.write(myPlayerID);
            out.close();
        }catch (NullPointerException e){
            System.out.println("You are not connected to the Server.");
        }
        catch (SocketException e){
            System.out.println("You are not connected to the Server.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a move request at x and y
     * @param x position x-axis
     * @param y position y-axis
     */
    public void move(int x, int y){
        if(gameOver == 0){
            try {
                out.writeUTF("MOVE");
                out.write(myGameID);
                out.write(myPlayerID);
                out.write(x);
                out.write(y);

            }catch (NullPointerException e){
                System.out.println("You are not connected to the Server.");
            }
            catch (IOException e) {
                System.out.println("You are not connected to the Server.");
            }
        }else {
            System.out.println("THE GAME IS OVER");
        }

    }


    private void clearGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int n = 0; n < grid[i].length; n++){
                grid[i][n] = "EMPTY";
            }
        }
    }




}

