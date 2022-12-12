package server;

import com.sun.jdi.BooleanType;
import game.AutomaticPlayer;
import game.Game;
import game.HumanPlayer;
import game.Player;
import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class Server implements Observer {
    private JFrame frame = new JFrame("Server.io");
    public BoardJComponent boardGui;
    private Game game;

    private ArrayList<Thread> players;
    private SimpleServer server;

    public Server(){
        createGui();
        game = new Game();
        players = new ArrayList<>();
    }

    private void createGui() {
        boardGui = BoardJComponent.getInstance(game);
        frame.add(boardGui);
        frame.setSize(800, 800);
        frame.setLocation(0, 150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            players.add(new AutomaticPlayer(i, game, game.initialEnergy()));
        }

        for (Thread player : players) {
            player.start();
        }

        server = new SimpleServer(this);
        server.start();

        for (Thread player : players) {
            player.join();
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.init();
    }

}
