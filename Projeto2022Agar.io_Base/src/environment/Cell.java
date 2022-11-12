package environment;

import game.Game;
import game.Player;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;

	protected Lock lock = new ReentrantLock();
	protected Condition freedom = lock.newCondition();
	
	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
	}

	public void addPlayer(Player player){
		lock.lock();
		try{
			while (isOcupied()){
				System.out.println(player + "is waiting");
				freedom.await();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			this.player = player;
			lock.unlock();
		}
	}

	public Player getPlayer() {
		return player;
	}

	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	

}
