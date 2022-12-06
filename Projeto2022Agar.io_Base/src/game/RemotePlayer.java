package game;

import environment.Direction;
import gui.BoardJComponent;

public class RemotePlayer extends Player{

    private String lastRecDirection;

    public RemotePlayer(int id, Game game, byte strength, BoardJComponent board) {
        super(id, game, strength, board);
        lastRecDirection = null;
    }

    public void setlastRecDirection(String dir){
        if ("UP".equals(dir) || "DOWN".equals(dir) || "LEFT".equals(dir) || "RIGHT".equals(dir))
            lastRecDirection = dir;
    }
    @Override
    public Direction nextDirection() {
        Direction nextDirection = stringToDirection(lastRecDirection);
        lastRecDirection = null;
        return nextDirection;
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    public static Direction stringToDirection(String str) {
        return switch (str) {
            case "UP" -> Direction.UP;
            case "DOWN" -> Direction.DOWN;
            case "LEFT" -> Direction.LEFT;
            case "RIGHT" -> Direction.RIGHT;
            case null -> null;
            default -> throw new RuntimeException("A string indicada não equivale a nenhuma direção");
        };
    }
}
