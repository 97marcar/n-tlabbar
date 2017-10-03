package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private Socket server;
    private MulticastSocket multicastSocket;
    private int gameID = -1;
    private int playerID = -1;
    private int port;
    private String group = "229.146.121.244";
    private ArrayList<Game> games = new ArrayList<Game>();

    public Server(int port) throws IOException{
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    private int generateGameID(){
        gameID++;
        return (gameID);

    }

    private int generatePlayerID(){
        playerID++;
        return(playerID);
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
                server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                System.out.println(server.getLocalSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                ListenThread listenThread = new ListenThread(in);
                listenThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ListenThread extends Thread{
        private DataInputStream in;
        public ListenThread(DataInputStream in){
            this.in = in;
        }
        public void run(){
            while(true){
                try {
                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
                    String message = in.readUTF();
                    System.out.println(message);
                    if(message.equals("CONNECT")){
                        System.out.println("Connected");
                        int yourID = generatePlayerID();
                        int yourGameID = -1;
                        boolean availableGame = false;
                        for(int i = 0; i<games.size(); i++){
                            if(!(games.get(i).avaliableSpot())){
                                availableGame = true;
                                games.get(i).setPlayer(yourID);
                                yourGameID = games.get(i).getGameID();
                                break;
                            }
                        }

                        if(!(availableGame)){
                            yourGameID = generateGameID();
                            Game g = new Game(yourGameID, yourID);
                            games.add(g);
                        }
                        out.write(yourGameID);
                        out.write(yourID);

                    }else if(message.equals("DISCONNECT")){
                        int disconnectGameID = in.read();
                        int disconnectPlayerID = in.read();
                        for(int i = 0; i<games.size(); i++){
                            if((games.get(i).getGameID()) == disconnectGameID){
                                System.out.println("GET GAMES ID: "+games.get(i).getGameID());
                                games.get(i).removePlayer(disconnectPlayerID);
                                System.out.println("PLAYER: "+disconnectPlayerID+" DISCONNECTED FROM GAME: " +disconnectGameID);
                                break;
                            }
                        }

                        in.close();
                        break;
                    }else if(message.equals("MOVE")){
                        int gameMovementID = in.read();
                        int playerMovementID = in.read();
                        int x = in.read();
                        int y = in.read();

                        System.out.println("GAME: "+gameMovementID+" PLAYER: "+playerMovementID+" MOVE: "+"X: "+x+" Y: "+y);
                        for (int i = 0; i<games.size(); i++){
                            Game game = games.get(i);
                            if(game.getGameID()==gameMovementID){
                                if(game.move(x, y, playerMovementID)){
                                    send(gameMovementID+","+playerMovementID+","+x+","+y+","+game.getGameOver());
                                }
                            }
                        }

                    }else{
                        System.out.println("OTHER");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
    public void send(String sendString){
        String group = "229.255.255.250";
        int port = 1900;

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress groupAddress = InetAddress.getByName(group);
            byte[] msg = sendString.getBytes();
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
