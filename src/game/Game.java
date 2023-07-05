package game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Cell;
import environment.Coordinate;

public class Game extends Observable implements Serializable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 200;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;

	protected Cell[][] board;
	private Lock lock = new ReentrantLock();
	private Condition cellAvailable = lock.newCondition();
	public Player[] players = new Player[NUM_PLAYERS];
	public Player[] winners = new Player[NUM_FINISHED_PLAYERS_TO_END_GAME];
	public CountDownLatch endLatch;
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
		endLatch = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);

		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y));
	}

	public void addPlayersToGame() {
		for(int i = 0; i < NUM_PLAYERS-2; i++){
			int finalI = i;
			new Thread(() -> {
				players[finalI] = (new Bot(finalI, this, (byte) ((Math.random()*MAX_INITIAL_STRENGTH)+1)));
				Cell randomCell = getRandomCell();
				lock.lock();
				try {
					while (true) {
						if(!randomCell.isOcupied()){
							randomCell.setPlayer(players[finalI]);
							break;
						} else {
							System.out.println( "The " + players[finalI] + " is waiting for " + randomCell.getPlayer() + " to move!");
							cellAvailable.await();
						}
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}
				notifyChange();
				players[finalI].start();
			}).start();
		}
	}

	public void turn(Cell position, Cell newPosition){
		lock.lock();
		try {
			if (!newPosition.isOcupied()) {
				newPosition.setPlayer(position.getPlayer());
				position.setPlayer(null);
				notifyChange();
				cellAvailable.signalAll();
			} else if(!position.getPlayer().isHumanPlayer()){
				if(newPosition.isOcupied() && (newPosition.getPlayer().getCurrentStrength() != 0 || newPosition.getPlayer().getCurrentStrength() != 10)){
					conflict(position.getPlayer(), newPosition.getPlayer());
					notifyChange();
				} else if(newPosition.isOcupied() && (newPosition.getPlayer().getCurrentStrength() == 0 || newPosition.getPlayer().getCurrentStrength() > 10)){
					Thread waitThread = new Thread(() -> {
						try {
							Thread.sleep(MAX_WAITING_TIME_FOR_MOVE);
							lock.lock();
							cellAvailable.signal();
							lock.unlock();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					});
					waitThread.start();
					cellAvailable.await();
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
	public void conflict(Player attacker, Player defender){
		int attStrength = attacker.getCurrentStrength();
		int defStrength = defender.getCurrentStrength();
		if(attStrength > defStrength){
			attacker.setCurrentStrength(checkMaxStrength(attStrength+defStrength));
			defender.setCurrentStrength(0);
		} else if (defStrength > attStrength){
			defender.setCurrentStrength(checkMaxStrength(attStrength+defStrength));
			attacker.setCurrentStrength(0);
		} else {
			if(Math.random() < 0.5){
				attacker.setCurrentStrength(checkMaxStrength(attStrength+defStrength));
				defender.setCurrentStrength(0);
			} else {
				defender.setCurrentStrength(checkMaxStrength(attStrength+defStrength));
				attacker.setCurrentStrength(0);
			}
		}
	}

	private int checkMaxStrength(int value){
		if(value > 10){
			return 10;
		} else {
			return value;
		}
	}
	public void notifyWin(Player p){
		for (int i = 0; i < winners.length; i++) {
			if(winners[i] == null){
				winners[i] = p;
				break;
			}
		}
		endLatch.countDown();
	}
	public void endGame() {
		for (Thread thread : players) {
			//noinspection removal
			thread.stop();
		}
		System.out.println("The winners are:");
		for (int i = 1; i <= NUM_FINISHED_PLAYERS_TO_END_GAME; i++){
			System.out.println(i + "º Lugar: " + winners[i-1]);
		}
	}

	public void connectOnlinePlayer(Player p){
		for (int i = NUM_PLAYERS-2; i < NUM_PLAYERS; i++) {
			if(players[i] == null){
				players[i] = p;
				Cell randomCell = getRandomCell();
				lock.lock();
				try {
					while (true) {
						if(!randomCell.isOcupied()){
							randomCell.setPlayer(players[i]);
							break;
						} else {
							System.out.println( "The " + players[i] + " is waiting for " + randomCell.getPlayer() + " to move!");
							cellAvailable.await();
						}
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}
				notifyChange();
				p.start();
				break;
			}
		}
	}
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}
	public Cell getPlayerCell(Player p){
		for (Cell[] line : board) {
			for (Cell cell : line) {
				if (p.equals(cell.getPlayer())) {
					return cell;
				}
			}
		}return null;
	}

	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		return getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
	}
}
