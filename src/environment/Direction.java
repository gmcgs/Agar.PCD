package environment;

import java.awt.event.KeyEvent;

public enum Direction {
	UP(0,-1),DOWN(0,1),LEFT(-1,0),RIGHT(1,0),W(0,-1),S(0,1),A(-1,0),D(1,0);
	private Coordinate vector;
	Direction(int x, int y) {
		vector=new Coordinate(x, y);
	}
	public Coordinate getVector() {
		return vector;
	}
	public static Direction getRandomDirection(){
		return Direction.values()[(int)(Math.random()*4)];
	}
	public static Direction toDirection(String str){
		return switch (str) {
			case "LEFT", "A" -> Direction.LEFT;
			case "RIGHT", "D" -> Direction.RIGHT;
			case "UP", "W" -> Direction.UP;
			case "DOWN", "S" -> Direction.DOWN;
			default -> null;
		};
	}
}
