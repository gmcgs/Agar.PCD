package game;

import environment.Direction;
import gui.BoardJComponent;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread implements Observer{
    public static final int PORT = 8080;
    private static Game game;
    public static Map<Integer, PhoneyHumanPlayer> players = Collections.synchronizedMap(new HashMap<>());
    private static int id = -1; // Starting ID
    public static boolean isRunning = false;
    public static BoardJComponent boardGui;
    private JFrame frame = new JFrame("Server");
    private static final Object lock = new Object();

    public Server(){
        game = new Game();
        game.addObserver(this);
        isRunning = true;
        buildGui();
        start();
    }

    private void buildGui() {
        boardGui = new BoardJComponent(game);
        frame.add(boardGui);
        frame.setSize(400,400);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void init() {
        game.addPlayersToGame();
        try{
            game.endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        game.endGame();
        close();
    }

    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while(isRunning){
                Socket socket = ss.accept();
                new ReceiveConnection(socket).start();
                new WriteConnection(socket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(){
        isRunning = false;
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    private static class ReceiveConnection extends Thread{
        private BufferedReader in;
        private int clientId;
        private PhoneyHumanPlayer player;
        private Set<String> allowedCommands;

        public ReceiveConnection(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientId = id--;
            if(clientId == -2){
                allowedCommands = new HashSet<>(Arrays.asList("W", "A", "S", "D"));
            } else {
                allowedCommands = new HashSet<>(Arrays.asList("UP", "LEFT", "DOWN", "RIGHT"));
            }
        }

        @Override
        public void run() {
            player = new PhoneyHumanPlayer(clientId, game, (byte) 5);
            synchronized (lock) {
                players.put(clientId, player);
            }
            game.connectOnlinePlayer(player);
            while(isRunning){
                try {
                    String dir = in.readLine();
                    if(dir != null && allowedCommands.contains(dir)) {
                        player.setLastPressedDirection(Direction.toDirection(dir));
                        player.clearLastPressedDirection();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized (lock) {
                players.remove(clientId);
            }
        }
    }

    private static class WriteConnection extends Thread{
        private ObjectOutputStream out;

        public WriteConnection(Socket socket) throws IOException {
            out = new ObjectOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                while(isRunning){
                    out.reset();
                    out.writeUnshared(boardGui.getGame());
                    out.flush();
                    sleep(Game.REFRESH_INTERVAL);
                }
                out.writeObject(null);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
        init();
    }
}
