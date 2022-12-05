package server;

import game.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer extends Thread{
    public static final int PORT = 8080;
    private Server handler;

    public SimpleServer(Server handler){
        this.handler = handler;
    }

    @Override
    public void run(){
        try {
            startServing();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORT);
        try {
            while(true){
                Socket socket = ss.accept();
                new ClientDealer(socket).start();
            }
        } finally {
            ss.close();
        }
    }

    public class ClientDealer extends Thread{


        public ClientDealer(Socket socket) {
        }
    }
}
