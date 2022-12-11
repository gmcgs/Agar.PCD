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
	private static JFrame frame = new JFrame("pcd.io");
	private static BoardJComponent boardGui;
	private static Game game;

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);

		buildGui();

	}

	public static Game getGame(){
		return game;
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);
		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static BoardJComponent getBoardGui(){
		return boardGui;
	}
	public void init() throws InterruptedException {
		frame.setVisible(true);
		game.startPlayers();
	}

	//fechar o GameGui (não utilizado)
	public static void notVisib() {
		frame.setVisible(false);
		frame.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}




}
