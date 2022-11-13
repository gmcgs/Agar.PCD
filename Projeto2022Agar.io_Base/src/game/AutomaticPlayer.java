package game;

import environment.Direction;

public class AutomaticPlayer extends Player {

    public AutomaticPlayer(int id, Game game, byte strength) {
        super(id, game, strength);
    }

    @Override
    public boolean isHumanPlayer() {
        return false;
    }

    @Override
    public Direction nextDirection() {
        return Direction.randomDirection();
    }

    @Override
    public String toString() {
        return "bot";
    }
}
