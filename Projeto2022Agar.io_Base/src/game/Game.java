package game;


import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import environment.Cell;
import environment.Coordinate;
import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;

public class Game extends Observable {
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 300;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 1000;

	protected static Cell[][] board;

	private final JFrame win = new JFrame("winners");

	public ArrayList<Thread> winners;
	public ArrayList<Thread> playerList;
	private static Game INSTANCE;

	public CyclicBarrier barrier = new CyclicBarrier(NUM_FINISHED_PLAYERS_TO_END_GAME+1);

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y),this);

		winners = new ArrayList<>();
		playerList = new ArrayList<>();
		INSTANCE = this;
	}

	public void end(){
		for (Thread thread : playerList) {
			thread.interrupt();
		}
	}

	public byte getInitialEnergy(){
		return (byte)(Math.random() * Game.MAX_INITIAL_STRENGTH + 1);
	}

	/**
	 * @param player
	 */
	public void addPlayerToGame(Player player) {
		Cell initialPos=getRandomCell();
		initialPos.addPlayer(player);
		notifyChange();
	}

	public static Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		return getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
	}

	public Cell getPlayerCell(Player p){
		for (Cell[] l : board)
			for (Cell c : l)
				if (p.equals(c.getPlayer()))
					return c;
		return null;
	}


	public void solveConflict(Player fighter, Player defender) throws BrokenBarrierException, InterruptedException {
		byte defenderValue = defender.getCurrentStrength();
		byte fighterValue = fighter.getCurrentStrength();
		double draw = Math.random();

		if(defenderValue > fighterValue){
			defender.setCurrentStrength(defenderValue + fighterValue);
			fighter.setCurrentStrength(0);
		} else if (defenderValue < fighterValue) {
			fighter.setCurrentStrength(defenderValue + fighterValue);
			defender.setCurrentStrength(0);
		} else {
			if (draw > 0.5){
				defender.setCurrentStrength(defenderValue + fighterValue);
				fighter.setCurrentStrength(0);
			}else{
				fighter.setCurrentStrength(defenderValue + fighterValue);
				defender.setCurrentStrength(0);
			}
		}
		if(defender.getCurrentStrength() == 10){
			System.out.println("defender");
			winners.add(defender);
			barrier.await();

		} else if (fighter.getCurrentStrength() == 10){
			System.out.println("fighter");
			winners.add(fighter);
			barrier.await();

		}
	}

	public Cell validate(Coordinate pos){
		if(pos.x >= 0 && pos.x < DIMX && pos.y >= 0 && pos.y < DIMY)
			return getCell(pos);
		return null;
	}

	//Auxiliar para o winners window
	public void pop_up_win(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<3; i++){
			sb.append(winners.toArray()[i]).append("\n");
		}
		JOptionPane.showMessageDialog(null, sb.toString());

	}

	public static Game getInstance() {
		if (INSTANCE != null)
			return INSTANCE;
		return new Game();
	}
}


