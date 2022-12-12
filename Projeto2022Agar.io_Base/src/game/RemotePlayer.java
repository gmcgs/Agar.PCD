package game;

import environment.Direction;

import static environment.Direction.*;

public class RemotePlayer extends Player{
    private String lastDirectionRecorded;

    public RemotePlayer(int id, Game game, byte strength) {
        super(id, game, strength);
        lastDirectionRecorded = null;
    }

    public void setLastRecievedDirection(String direction) {
        if ("Up".equals(direction) || "Down".equals(direction) || "Left".equals(direction) || "Right".equals(direction))
            lastDirectionRecorded = direction;
    }

    @Override
    public Direction nextDirection() throws RuntimeException {
        switch (lastDirectionRecorded){
            case "Up" :
                lastDirectionRecorded = null;
                return UP;
            case "Down":
                lastDirectionRecorded = null;
                return DOWN;
            case "Left":
                lastDirectionRecorded = null;
                return LEFT;
            case "Right":
                lastDirectionRecorded = null;
                return RIGHT;
            default:
                throw new IllegalArgumentException("Direção inválida");
        }
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }
}
