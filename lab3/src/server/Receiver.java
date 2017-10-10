package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;

/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */
public class Receiver {
    private String group;
    private int port;
    private Server server;
    private MulticastSocket multicastSocket;
    private ServerSocket serverSocket;

    /**
     * Listens to messages sent on a specific IP and port. If the message is "REQUEST" it sends back information
     * to another IP("229.255.255.250") and port (6745)
     * @param g IP of the group it listens to
     * @param p port of the group it listens to
     * @param s server so it can access the information it needs and sent it back.
     */
    public Receiver(String g, int p, Server s){
        System.out.println("RECEIVER CREATED");
        group = g;
        port =  p;
        server = s;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                if (msg.equals("REQUEST")){
                    System.out.println(serverSocket.getInetAddress());
                    System.out.println(serverSocket.getLocalPort());
                    String answer = " SERVICE QUERY JavaGameServer ThreeInARow "+serverSocket.getInetAddress()+" "+server.getPort();
                    server.send(answer, "229.255.255.250", 6745);
                }


            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}

