package game;


import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

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
	protected BoardJComponent board;

	public Cell getCurrentCell() {
		return game.getPlayerCell(this);
	}

	public Player(int id, Game game, byte strength, BoardJComponent board) {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
		this.board = board;
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
			while (this.getCurrentStrength() != 10 && this.getCurrentStrength() != 0) {
				movement(nextDirection());
				sleep(Game.REFRESH_INTERVAL * originalStrength);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void movement(Direction direction) {
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
