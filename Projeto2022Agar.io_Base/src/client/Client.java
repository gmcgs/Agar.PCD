package client;

import environment.Direction;
import game.Game;
import game.RemotePlayer;
import gui.BoardJComponent;

import javax.swing.*;

public class Client {

    private JFrame frame = new JFrame("Client pcd.io");

    private BoardJComponent boardGui;

    private ClientPlayer client;

    public Client(){
        client = new ClientPlayer(this);
        client.start();
        buildGui();
    }

    private void buildGui() {

        boardGui = BoardJComponent.getInstance(new Game());
        frame.add(boardGui);
        frame.setSize(800, 800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        frame.setVisible(true);
    }
    public void update(Game game) {
        boardGui.setGame(game);
        boardGui.repaint();
    }

    public Direction getLastPressDirection(){
        Direction direction = boardGui.getLastPressedDirection();
        boardGui.clearLastPressedDirection();
        return direction;
    }

    public static void main(String[] args) throws InterruptedException {
        Client game =  new Client();
        game.init();
    }
}
