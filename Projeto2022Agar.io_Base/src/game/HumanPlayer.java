package game;

import environment.Direction;
import gui.BoardJComponent;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class HumanPlayer extends Player {
	public HumanPlayer(int id, Game game, byte strength, BoardJComponent theBoard) {
		super(id, game, strength, theBoard);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public Direction nextDirection() {
		Direction nextDirection = theBoard.getLastPressedDirection();
		theBoard.clearLastPressedDirection();
		return nextDirection;	}
}
