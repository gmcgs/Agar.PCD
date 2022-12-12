package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

public class AutomaticPlayer extends Player {

    public AutomaticPlayer(int id, Game game, byte strength) {
        super(id, game, strength);
    }
    /*public void movement(Direction direction) {
        switch (this.getCurrentStrength()){
            case 0:
            case 10:
                break;
            default:
                System.out.println("movement");
                if (direction != null) {
                    Cell position = game.getPlayerCell(this);
                    Coordinate newPosition = position.getPosition().translate(direction.getVector());
                    Cell newPos = game.validate(newPosition);
                    if (newPos != null) {
                        game.playerMove(position, newPos);
                    }
                }break;
        }
    }*/
    @Override
    public Direction nextDirection() {
        return Direction.randomDirection();
    }

    @Override
    public boolean isHumanPlayer() {
        return false;
    }
}
