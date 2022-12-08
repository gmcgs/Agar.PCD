package server;

import com.sun.jdi.BooleanType;
import game.AutomaticPlayer;
import game.Game;
import game.HumanPlayer;
import gui.BoardJComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Server implements Observer {
    private JFrame frame = new JFrame("Server.io");
    public BoardJComponent boardGui;
    private Game game;
    private ArrayList<Thread> players;

    public Server(){
        players = new ArrayList<Thread>();
        game = new Game();
        game.addObserver( this);
        createGui();
    }

    private void createGui() {
        boardGui = new BoardJComponent();
        frame.add(boardGui);
        frame.setSize(800, 800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    public void init() throws InterruptedException{
        frame.setVisible(true);
        for(int i = 0; i < 1; i++)
            players.add(new HumanPlayer(i, game, (byte)5, boardGui));

        for (int i = 1; i < 5; i++)
            players.add(new AutomaticPlayer(i, game, game.getInitialEnergy(), boardGui));

        for (Thread p : players){
            p.start();
        }
    }
}
