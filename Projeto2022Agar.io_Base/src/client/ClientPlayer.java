package client;


import game.Game;
import server.SimpleServer;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientPlayer extends Thread {

    private ObjectInputStream in;
    private PrintWriter out;
    private Socket socket;
    private final Client client;

    public ClientPlayer(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            connectToTheServer();
            client.update ((Game)in.readObject());
            sendMessages();
        } catch (IOException | ClassNotFoundException | InterruptedException exception) {
            System.err.println("error");
        } finally {
            try {
                socket.close();
            } catch (IOException exception) {
                System.err.println("error");
            }
        }
    }

    void connectToTheServer() throws IOException {
        InetAddress ip = InetAddress.getByName(null);
        socket = new Socket(ip, SimpleServer.PORT);
        in = new ObjectInputStream ( socket.getInputStream ());
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    void sendMessages() throws IOException, ClassNotFoundException, InterruptedException {
        boolean isRunning = true;
        while (isRunning) {
            Game game = (Game)in.readUnshared();
            client.update (game);
            sleep(Game.REFRESH_INTERVAL);
        }
    }
}
