package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.BoardJComponent;

public class AutomaticPlayer extends Player {

    public AutomaticPlayer(int id, Game game, byte strength, BoardJComponent board) {
        super(id, game, strength, board);
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
            sleep(game.INITIAL_WAITING_TIME);
            while (this.getCurrentStrength() != 10 && this.getCurrentStrength() != 0) {
                movement(nextDirection());
                sleep(game.REFRESH_INTERVAL * originalStrength);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
