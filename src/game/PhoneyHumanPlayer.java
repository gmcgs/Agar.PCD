package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import static game.Game.*;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player {
	private Direction lastPressedDirection;
	public byte playerStrength;
	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
		playerStrength = 1;
	}

	@Override
	public Direction move() {
		return lastPressedDirection;
	}

	public void setLastPressedDirection(Direction direction){
		lastPressedDirection = direction;
	}

	public void clearLastPressedDirection(){
		lastPressedDirection = null;
	}

	public byte getPlayerStrength() {
		return playerStrength;
	}

	public boolean isHumanPlayer() {
		return true;
	}
}
