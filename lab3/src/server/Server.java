package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */
public class Server extends Thread{
    private ServerSocket serverSocket;
    private Socket server;
    private int gameID = -1;
    private int playerID = -1;
    private int port;
    private ArrayList<Game> games = new ArrayList<Game>();

    /**
     * Creates a serverSocket with a specific port.
     * @param port which port to use
     * @throws IOException throws this Exception if something is wrong
     */
    public Server(int port) throws IOException{
        this.port = port;
        serverSocket = new ServerSocket(port);

    }
    public int getPort(){
        return(port);
    }
    private int generateGameID(){
        gameID++;
        return (gameID);

    }

    private int generatePlayerID(){
        playerID++;
        return(playerID);
    }


    /**
     * Runs the server as well as creates and handles the game for the clients thought a private class
     * that listens to what the clients says(TCP) on the port
     */
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

    /**
     * A class that is created for every client and listens to their specific input.
     * It also handles the input and calls the send function that is outside of this class.
     */
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
                    if(message.equals("CONNECT")){
                        System.out.println("Client connected");
                        int yourID = generatePlayerID();
                        int yourGameID = -1;
                        boolean availableGame = false;
                        for(int i = 0; i<games.size(); i++){
                            if(!(games.get(i).availableSpot())){
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
                        Game game = findGame(disconnectGameID);
                        System.out.println("GET GAMES ID: "+game.getGameID());
                        game.removePlayer(disconnectPlayerID);
                        System.out.println("PLAYER: "+disconnectPlayerID+" DISCONNECTED FROM GAME: " +disconnectGameID);
                        send(disconnectGameID+","+disconnectPlayerID+","+-1+","+-1+","+0, "229.255.255.250",6745);
                        in.close();

                    }else if(message.equals("MOVE")){
                        int gameMovementID = in.read();
                        int playerMovementID = in.read();
                        int x = in.read();
                        int y = in.read();

                        System.out.println("GAME: "+gameMovementID+" PLAYER: "+playerMovementID+" MOVE: "+"X: "+x+" Y: "+y);
                        Game game = findGame(gameMovementID);
                        System.out.println("FOUND"+game.getGameID());
                        if(game.move(x, y, playerMovementID)){
                            System.out.println("MOVE MADE");
                            send(gameMovementID+","+playerMovementID+","+x+","+y+","+game.getGameOver(), "229.255.255.250",6745);
                        }



                    }else if(message.equals("RESTART")){
                        int gameRestartID = in.read();
                        Game game = findGame(gameRestartID);
                        game.restart();
                        send(gameRestartID+","+-1+","+-1+","+-1+","+game.getGameOver(), "229.255.255.250",6745);

                    }
                    else{
                        System.out.println("OTHER");
                    }
                }catch (SocketException e){

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }
    private Game findGame(int gameID){
        for(int i = 0; i < games.size(); i++){
            Game game = games.get(i);
            if(game.getGameID()==gameID) return game;
        }
        return null;

    }

    /**
     * Sends information to the clients
      * @param sendString what information to send
     * @param group to what IP you want to sent your string
     * @param port to what port you want to send your string
     */
    public void send(String sendString, String group, int port){
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

    /**
     * Creates the server and multicast receiver.
     * @param args does nothing in this program
     */
    public static void main(String[] args){
        int port = 6066;
        try {
            Server s = new Server(port);
            s.start();
            Receiver receiver = new Receiver("239.255.255.250", 1900, s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
