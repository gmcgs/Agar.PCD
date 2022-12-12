package server;

import game.AutomaticPlayer;
import game.Game;
import game.HumanPlayer;
import game.Player;
import gui.BoardJComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

public class Server implements Observer {
    private final JFrame frame = new JFrame("Server.io");
    public BoardJComponent boardGui;
    private final Game game;

    private ArrayList<Thread> players;
    private SimpleServer server;

    public Server(){
        players = new ArrayList<>();
        game = new Game();
        game.addObserver(this);
        createGui();
    }

    private void createGui() {
        boardGui = BoardJComponent.getInstance(game);
        frame.add(boardGui);
        frame.setSize(800, 800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() throws InterruptedException {
        frame.setVisible(true);
        sleep(Game.INITIAL_WAITING_TIME);

        players.add(new HumanPlayer(0, game, game.getInitialEnergy()));

        for (int i = 0; i < 90; i++) {
            players.add(new AutomaticPlayer(i, game, game.getInitialEnergy()));
        }

        for (Thread player : players) {
            player.start();
        }

        for (Thread player : players) {
            player.join();
        }

        server = new SimpleServer(this);
        server.start();
    }
    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    public void addPlayer(Player player){
        players.add(player);
        player.start();
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.init();
    }

}
