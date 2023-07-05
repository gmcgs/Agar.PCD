package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;

import static game.Game.*;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread implements Serializable {


	protected  Game game;

	private int id;

	private byte currentStrength;
	protected byte originalStrength;

	public Cell getCurrentCell() {
		return game.getPlayerCell(this);
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}

	@Override
	public void run() {
		try {
			sleep(INITIAL_WAITING_TIME);
			while(this.getCurrentStrength() < 10 && this.getCurrentStrength() > 0){
				Direction dir = move();
				if(dir != null){
					Coordinate newCoordinate = this.getCurrentCell().getPosition().translate(dir.getVector());
					if(newCoordinate.x < DIMX && newCoordinate.x >= 0 && newCoordinate.y < DIMY && newCoordinate.y >= 0){
						Cell newPosition = game.board[newCoordinate.x][newCoordinate.y];
						game.turn(this.getCurrentCell(), newPosition);
						if(isHumanPlayer()){
							PhoneyHumanPlayer p = (PhoneyHumanPlayer) this;
							sleep(REFRESH_INTERVAL*p.getPlayerStrength());
						} else {
							sleep(REFRESH_INTERVAL*originalStrength);
						}
						sleep(Game.REFRESH_INTERVAL*originalStrength);
					}
				}
			}
			if(this.getCurrentStrength() >= 10){
				game.notifyWin(this);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
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
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public void setCurrentStrength(int value){
		currentStrength = (byte)value;
	}
	public abstract Direction move();
	public int getIdentification() {
		return id;
	}
}
