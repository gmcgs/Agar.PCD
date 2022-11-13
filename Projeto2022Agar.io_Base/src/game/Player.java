package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

import javax.swing.*;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread {


	protected Game game;

	private int id;

	private byte currentStrength;
	protected byte originalStrength;

	protected BoardJComponent theBoard;

	public Cell getCurrentCell() {
		//done
		return game.getPlayerCell(this);
	}

	public Player(int id, Game game, byte strength, BoardJComponent theBoard) {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
		this.theBoard = theBoard;
	}

	@Override
	public void run() {
		boolean notOnTheGame = true;

		while (notOnTheGame) {
			try {
				game.addPlayerToGame(this);
				notOnTheGame = false;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		try {
			sleep(game.INITIAL_WAITING_TIME);
			while (this.currentStrength != 10 && this.currentStrength != 0) {
				movement(nextDirection());
				sleep(game.REFRESH_INTERVAL * originalStrength);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract Direction nextDirection();

	public void movement(Direction direction) {
		//if redundante
		//if (this.currentStrength == 0 || this.currentStrength == 10) return;
		System.out.println("movement");
		if (direction != null) {
			Cell position = game.getPlayerCell(this);
			Coordinate newPosition = position.getPosition().translate(direction.getVector());
			Cell newPos = game.validate(newPosition);
			if (newPos != null) {
				game.playerMove(position, newPos);
			}
		}
	}

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

	public byte setCurrentStrength(int a) {
		if (a > 10)
			return this.currentStrength = 10;
		else return this.currentStrength = (byte) a;
	}


	public int getIdentification() {
		return id;
	}
}
