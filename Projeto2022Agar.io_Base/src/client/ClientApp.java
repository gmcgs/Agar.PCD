package client;

import gui.BoardJComponent;

import javax.swing.*;

public class ClientApp {

    private BoardJComponent boardGui;

    private JFrame frame = new JFrame("pcd.io Cliente");
    private SimpleClient client;

    public ClientApp(){
        client = new SimpleClient(this);
        buildGui();

    }

    private void buildGui() {

        boardGui = BoardJComponent.getInstance(); //Always starts this one empty map
        frame.add(boardGui);
        frame.setSize(800, 800);
        frame.setLocation(0, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //public static void main(String[] args) {
    //}
}
