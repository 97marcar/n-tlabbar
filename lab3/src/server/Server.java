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
    private String group = "239.146.121.244";
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
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println("nåho");
                ListenThread listenThread = new ListenThread(in);
                listenThread.start();
                System.out.println("jajajajaj");



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
            try {
                System.out.println("HEJSANHOPSANLIELEL");
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                String message = in.readUTF();
                while(!(message.equals("Disconnect"))){

                    System.out.println(message);


                    if(message.equals("CONNECT")){
                        System.out.println("777");
                        int yourID = generatePlayerID();
                        int yourGameID = -1;
                        boolean avaliableGame = false;

                        System.out.println("AAAAA");
                        for(int i = 0; i<games.size(); i++){
                            if(!(games.get(i).isPlayer2())){
                                avaliableGame = true;
                                games.get(i).setPlayer2(yourID);
                                yourGameID = games.get(i).getGameID();
                            }
                        }

                        if(!(avaliableGame)){
                            yourGameID = generateGameID();
                            Game g = new Game(yourGameID, yourID);
                            games.add(g);
                        }
                        out.write(yourGameID);

                    }
                    System.out.println("YES");
                    out.writeUTF("HELLo");
                    in.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class Game{
        private int gameID;
        private int player1;
        private int player2 = 0;

        public Game(int gameID, int player1){
            System.out.println("SPEL SKAPAT");
            this.gameID = gameID;
            this.player1 = player1;
        }

        public void setPlayer2(int player2){
            this.player2 = player2;
        }

        public boolean isPlayer2(){
            if(player2 == 0){
                return false;
            }else{
                return true;
            }
        }
        public int getGameID(){
            return gameID;
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
