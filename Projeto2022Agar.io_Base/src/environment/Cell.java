package environment;

import game.Game;
import game.Player;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;

	protected Lock lock = new ReentrantLock();

	protected Condition free = lock.newCondition();

	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOccupied() {
		return player!=null;
	}

	public void addPlayer(Player player){
		lock.lock();
		try{
			while (isOccupied()){
				System.out.println(player + " is waiting for " + this.player + " to move!");
				free.await(); //signalAll
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			this.player = player;
			lock.unlock();
		}
	}

	public void playerMove(Cell pos, Cell newPos){
		pos.lock.lock();
		newPos.lock.lock();
		synchronized (free) {
			try {
				game.notifyChange();
				if (!newPos.isOccupied()) {
					newPos.setPlayer(pos.getPlayer());
					pos.removePlayer();
				} else {
					if (newPos.getPlayer().getCurrentStrength() != 0 && newPos.getPlayer().getCurrentStrength() != 10) {
						game.solveConflict(pos.getPlayer(), newPos.getPlayer());
					}
					if (newPos.getPlayer().getCurrentStrength() == 0 && !player.isHumanPlayer()) {
						free.wait(2000);
					}
				}
			} catch (BrokenBarrierException | InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				pos.lock.unlock();
				newPos.lock.unlock();
			}
		}
	}


	public Player getPlayer() {
		return player;
	}

	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public void setPlayer(Player player) {
		this.player = player;
	}

	public void removePlayer() {
		lock.lock();
		this.player = null;
		free.signalAll();
		lock.unlock();
	}
}