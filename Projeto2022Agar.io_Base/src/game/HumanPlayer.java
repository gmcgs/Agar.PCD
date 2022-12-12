package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

import static environment.Direction.*;
import static environment.Direction.RIGHT;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class HumanPlayer extends Player {
	private BoardJComponent board;
	public HumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	@Override
	public Direction nextDirection() {
		board = BoardJComponent.getInstance(game);
		String nextDirection = board.getLastPressedDirection();
		switch (nextDirection) {
			case "Up":
				board.clearLastPressedDirection();
				return UP;
			case "Down":
				board.clearLastPressedDirection();
				return DOWN;
			case "Left":
				board.clearLastPressedDirection();
				return LEFT;
			case "Right":
				board.clearLastPressedDirection();
				return RIGHT;
			default:
				throw new IllegalArgumentException("Direção inválida");
		}
	}
}
