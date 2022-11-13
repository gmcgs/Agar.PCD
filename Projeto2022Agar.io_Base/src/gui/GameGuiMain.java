package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import game.AutomaticPlayer;
import game.Game;
import game.HumanPlayer;
import game.Player;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();

	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);
		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException {
		frame.setVisible(true);
		ArrayList<Thread> playerList = new ArrayList<>();

		playerList.add(new HumanPlayer(0, game, getInitialEnergy()));

		for (int i = 1; i < 90 ; i++) {
			playerList.add(new AutomaticPlayer(i, game, getInitialEnergy()));
		}

		for (Thread player : playerList) {
			player.start();
		}

		for (Thread player : playerList) {
			player.join();
		}
	}

	public byte getInitialEnergy(){
		return (byte)(Math.random() * Game.MAX_INITIAL_STRENGTH + 1);
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}

}
