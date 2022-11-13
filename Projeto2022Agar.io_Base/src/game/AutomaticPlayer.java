package game;

import environment.Direction;
import gui.BoardJComponent;

public class AutomaticPlayer extends Player {

    public AutomaticPlayer(int id, Game game, byte strength, BoardJComponent board) {
        super(id, game, strength, board);
    }

    @Override
    public Direction nextDirection() {
        return Direction.randomDirection();
    }

    @Override
    public boolean isHumanPlayer() {
        return false;
    }
}
