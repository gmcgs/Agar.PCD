package server;

import game.Game;
import game.RemotePlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class SimpleServer extends Thread{
    public static final int PORT = 8080;
    private Server server;

    private boolean isRunning;
    public SimpleServer(Server server){
        this.server = server;
        isRunning = true;
    }

    public class ClientInteractions extends Thread{
        private RemotePlayer p;
        private BufferedReader in;
        private ObjectOutputStream out;
        public ClientInteractions(Socket soc) throws IOException {
            createConnection(soc);
            Game game = server.boardGui.getGame();
            p = new RemotePlayer(-1, game, game.getInitialEnergy());
            server.addPlayer(p);
        }
        void createConnection(Socket soc) throws IOException {
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            out = new ObjectOutputStream ( soc.getOutputStream ());
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    String str = in.readLine();
                    if (Objects.equals(str, "nada"))
                        break;
                    p.setLastRecievedDirection(str);
                    out.reset();
                    out.writeUnshared(server.boardGui.getGame());
                }
            } catch (IOException e) {
                System.err.println("Perdeu a conexão ao servidor");
            }
        }
    }
    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(PORT);
            try {
                while (isRunning) {
                    Socket socket = ss.accept();
                    new ClientInteractions(socket).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                ss.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
