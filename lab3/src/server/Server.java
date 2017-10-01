package server;

import java.io.*;
import java.net.*;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private int id = 0;
    private int port;
    private String group = "239.146.121.244";

    public Server(int port) throws IOException{
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public void receiver() throws IOException{
        while (true){

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


    public void run(){
        while(true){
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.write(generateID());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private int generateID(){
        id++;
        String temp = Integer.toString(id);
        if(id % 2 != 0){
            temp = temp +"1";
        }else{
            temp = temp +"2";
        }

        return (Integer.parseInt(temp));

    }

    public static void main(String[] args){
        int port = 6066;
        try {
            Thread t = new Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
