package game;

import environment.Direction;
import gui.BoardJComponent;

public class AutomaticPlayer extends Player {

    public AutomaticPlayer(int id, Game game, byte strength, BoardJComponent theBoard) {
        super(id, game, strength, theBoard);
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
