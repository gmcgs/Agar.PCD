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
    private static boolean isRunning;
    public SimpleServer(Server server){
        this.server = server;
        isRunning = true;
    }

    public void end() {
        isRunning = false;
    }

    public class ClientInteractionsIn extends Thread{
        private RemotePlayer p;
        private BufferedReader in;
        public ClientInteractionsIn(Socket soc) throws IOException {
            createConnection(soc);
            Game game = server.boardGui.getGame();
            p = new RemotePlayer(-1, game, game.initialEnergy());
            server.addPlayer(p);
            p.start();
        }
        void createConnection(Socket soc) throws IOException {
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String str = "";
                while (isRunning) {
                    str = in.readLine();
                    if (Objects.equals(str, "nada"))
                        break;
                    p.setLastRecievedDirection(str);
                }
            } catch (IOException e) {
                System.err.println("Perdeu a conexão ao servidor");
            }
        }
    }

    public class ClientInteractionsOut extends Thread{
        private ObjectOutputStream out;

        public ClientInteractionsOut(Socket soc) throws IOException {
            createConnection(soc);
        }
        void createConnection(Socket soc) throws IOException {
            out = new ObjectOutputStream(soc.getOutputStream());
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    out.reset();
                    out.writeUnshared(server.boardGui.getGame());
                    sleep(Game.REFRESH_INTERVAL);
                }
                out.writeObject(null);
            } catch (IOException e) {
                System.err.println("Perdeu a conexão ao servidor");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
                    new ClientInteractionsIn(socket).start();
                    new ClientInteractionsOut(socket).start();
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
