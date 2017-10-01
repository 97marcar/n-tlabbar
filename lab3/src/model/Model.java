package model;

import java.io.*;
import java.net.*;
import java.util.Observable;


public class Model extends Observable {
    private MulticastSocket multicastSocket;
    private int port = 6066;
    private String group = "239.146.121.244";

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
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void receiver() throws IOException{
        while (true){
            this.port = port;
            InetAddress groupAdress = InetAddress.getByName(group);
            multicastSocket = new MulticastSocket(port);
            System.out.println("Multicast Reciver running at:" + multicastSocket.getLocalSocketAddress());
            multicastSocket.joinGroup(groupAdress);

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            System.out.println("Waiting for a  multicast message...");
            multicastSocket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            System.out.println("[Multicast  Receiver] Received:" + msg);
        }
    }

    public void send(){
        String group = "239.255.255.250";
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
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e){
            e.printStackTrace();
        } catch (IOException e){
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
