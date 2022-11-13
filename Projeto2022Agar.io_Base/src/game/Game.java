package game;


import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class Game extends Observable {
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 1000;

	protected Cell[][] board;

	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}
	
	/** 
	 * @param player 
	 */
	public void addPlayerToGame(Player player) {
		Cell initialPos=getRandomCell();
		initialPos.addPlayer(player);
		notifyChange();
	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		return getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
	}

	public Cell getPlayerCell(Player p){
		for (Cell[] l : board)
			for (Cell c : l)
				if (p.equals(c.getPlayer()))
					return c;
		return null;
	}

	public void playerMove(Cell pos, Cell newPos){
		//getCellsLock(pos, newPos);
		pos.getLock();
		newPos.getLock();
		try{
			if(!newPos.isOccupied()) {
				newPos.setPlayer(pos.getPlayer());
				pos.removePlayer();
			} else {
				if(newPos.getPlayer().getCurrentStrength() != 0 && newPos.getPlayer().getCurrentStrength() != 10)
					resolveConflite(pos.getPlayer(), newPos.getPlayer());
			}
			notifyChange();
		} finally {
			//freeCellsLock(pos, newPos);
			pos.getUnlock();
			newPos.getUnlock();
			//não percebi a diferença desta merda
			//pois, nem eu. acho que assumimos como está por agora
		}
	}


	private void resolveConflite(Player fighter, Player defender) {
		byte defenderValue = defender.getCurrentStrength();
		byte fighterValue = fighter.getCurrentStrength();
		boolean tieBreak = (Math.random() < 0.5);

		if(defenderValue > fighterValue){
			defender.setCurrentStrength(defenderValue + fighterValue);
			fighter.setCurrentStrength(0);
		} else if (defenderValue < fighterValue) {
			fighter.setCurrentStrength(defenderValue + fighterValue);
			defender.setCurrentStrength(0);
		} else {
			if (tieBreak){
				defender.setCurrentStrength(defenderValue + fighterValue);
				fighter.setCurrentStrength(0);
			}else{
				fighter.setCurrentStrength(defenderValue + fighterValue);
				defender.setCurrentStrength(0);
			}
		}
	}

	public Cell validate(Coordinate p){
		if(validatePos(p))
			return getCell(p);
		return null;
	}

	private boolean validatePos(Coordinate pos){
		return pos.x >= 0 && pos.x < DIMX && pos.y >= 0 && pos.y < DIMY;
	}


	//por explicar a utilidade
	private void getCellsLock(Cell actualPosition, Cell newPosition) {
		boolean lineFight = actualPosition.getPosition().x != newPosition.getPosition().x;
		if (lineFight && actualPosition.getPosition().y < newPosition.getPosition().y || actualPosition.getPosition().x < newPosition.getPosition().x) {
			actualPosition.getLock();
			newPosition.getLock();
		} else {
			newPosition.getLock();
			actualPosition.getLock();
		}
	}

	private void freeCellsLock(Cell actualPosition, Cell newPosition) {
		boolean lineFight = actualPosition.getPosition().x != newPosition.getPosition().x;
		if (lineFight && actualPosition.getPosition().y < newPosition.getPosition().y || actualPosition.getPosition().x < newPosition.getPosition().x) {
			actualPosition.getUnlock();
			newPosition.getUnlock();
		} else {
			newPosition.getUnlock();
			actualPosition.getUnlock();
		}
	}

}
