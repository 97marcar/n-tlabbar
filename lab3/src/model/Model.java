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
    private int port = 6066;
    private String group = "229.255.255.250";
    private String serverName = "localhost";
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
            receiver = new Receiver(group, 6745, true);

        }catch (IOException e){
            System.out.println("Could not connect to the server.");
        }
    }


    private class Receiver{
        private String group;
        private int port;
        private boolean update;
        Receiver (String g, int p, boolean u){
            group = g;
            port =  p;
            update = u;
            while(true){
                try{
                    InetAddress groupAddress = InetAddress.getByName(group);
                    multicastSocket = new MulticastSocket(port);
                    System.out.println("Multicast Reciver running at:" + multicastSocket.getLocalSocketAddress());
                    multicastSocket.joinGroup(groupAddress);

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
                if(!(receivedX == -1 && receivedY == -1)) grid[receivedX][receivedY] = "ME";

            }else{
                if(receivedX == -1 && receivedY == -1) clearGrid();
                else grid[receivedX][receivedY] = "OTHER";
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

    public void restart(){
        send();
        try {
            connect();
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
        }catch (NullPointerException e){
            System.out.println("You are not connected to the Server.");
        }
        catch (SocketException e){
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

