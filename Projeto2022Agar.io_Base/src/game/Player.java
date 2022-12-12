package game;


import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread {


	protected Game game;

	private int id;

	protected Lock cadeado = new ReentrantLock();

	protected Condition waiting = cadeado.newCondition();

	private byte currentStrength;
	protected byte originalStrength;

	public Cell getCurrentCell() {
		return game.getPlayerCell(this);
	}

	public Player(int id, byte strength) {
		super();
		this.id = id;
		currentStrength = strength;
		originalStrength = strength;
	}

	@Override
	public void run() {
		boolean onGame = false;

		while (!onGame) {
			try {
				game.addPlayerToGame(this);
				onGame = true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			sleep(Game.INITIAL_WAITING_TIME);
			//this.getCurrentStrength() != 10 && this.getCurrentStrength() != 0
			while (game.barrier.getNumberWaiting() < 3) {
				movement(nextDirection());
				sleep(Game.REFRESH_INTERVAL * originalStrength);
				//depois de atingir as 3 barreiras, o jogo fecha e aparece a lista com o top3
			}if(game.barrier.getNumberWaiting() >= 3){
				//GameGuiMain.notVisib();
				game.pop_up_win();
			}
		} catch (Exception ignored) {

		}
	}
	public void movement(Direction direction) throws InterruptedException {
		switch (this.getCurrentStrength()){
			case 0:
			case 10:
				break;
			default:
				if (direction != null) {
					Cell position = game.getPlayerCell(this);
					Coordinate newPosition = position.getPosition().translate(direction.getVector());
					Cell newPos = game.validate(newPosition);
					if (newPos != null) {

						position.playerMove(position, newPos);
					}
				}break;
		}
	}

	public abstract Direction nextDirection();

	public abstract boolean isHumanPlayer();

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return id == other.id;
	}


	public byte getCurrentStrength() {
		return currentStrength;
	}

	public void setCurrentStrength(int a) {
		if (a > 10) {
			this.currentStrength = 10;
		} else {
			this.currentStrength = (byte) a;
		}
	}

	public int getIdentification() {
		return id;
	}
}
