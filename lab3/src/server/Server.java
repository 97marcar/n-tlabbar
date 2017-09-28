package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private int id = 0;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
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
        try{
            Thread t = new Server(port);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
