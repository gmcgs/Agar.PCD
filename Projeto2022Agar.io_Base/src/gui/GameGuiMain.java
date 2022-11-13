package gui;

import java.util.ArrayList;
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
		// Demo players, should be deleted
		ArrayList<Thread> playerList = new ArrayList<>();
		for (int i = 30; i >0; i--){
			playerList.add(new AutomaticPlayer(i, game, (byte) 3));
		}
		for(Thread p : playerList){
			p.start();
		}
		for (Thread p :playerList){
			p.join();
		}
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
