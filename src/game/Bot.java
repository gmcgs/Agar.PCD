package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;

import static game.Game.*;

public class Bot extends Player implements Serializable {

    public Bot(int id, Game game, byte strength){
        super(id, game, strength);
    }

    @Override
    public Direction move() {
        return Direction.getRandomDirection();
    }

    @Override
    public boolean isHumanPlayer() {
        return false;
    }
}
