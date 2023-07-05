package game;

import environment.Direction;
import gui.BoardJComponent;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Client extends Thread implements Observer {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = Server.PORT;
    private JFrame frame = new JFrame("Client");
    private BoardJComponent boardGui;
    private Game game;
    private static boolean isRunning = false;
    private PrintWriter out;
    private ObjectInputStream in;
    private Socket socket;

    public Client() {
        isRunning = true;
        game = new Game();
        game.addObserver(this);
        boardGui = new BoardJComponent(game);
        buildGui();
    }

    private void buildGui() {
        frame.add(boardGui);
        frame.setSize(400, 400);
        frame.setLocation(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void start() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new ObjectInputStream(socket.getInputStream());
            new ReceiveConnection(in, boardGui).start();
            new WriteConnection(out, boardGui).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    private class ReceiveConnection extends Thread {

        private ObjectInputStream in;
        private BoardJComponent boardGui;
        public ReceiveConnection(ObjectInputStream in, BoardJComponent boardGui){
            this.in = in;
            this.boardGui = boardGui;
        }
        @Override
        public void run() {
            try {
                while (isRunning) {
                    Game game = (Game) in.readUnshared();
                    boardGui.setGame(game);
                    boardGui.repaint();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class WriteConnection extends Thread {
        private PrintWriter out;
        private BoardJComponent boardGui;
        public WriteConnection(PrintWriter out, BoardJComponent boardGui){
            this.out = out;
            this.boardGui = boardGui;
        }
        @Override
        public void run() {
            while (isRunning) {
                Direction newDirection = boardGui.getLastPressedDirection();
                if (newDirection != null) {
                    String dirString = newDirection.toString();
                    out.println(dirString);
                    boardGui.clearLastPressedDirection();
                    try {
                        sleep(Game.REFRESH_INTERVAL);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
