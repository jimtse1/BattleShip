import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * A board is a matrix in which the objects (ships) are hidden.
 * The user interacts with the board by targeting certain point locations. Once a user
 * targets a point location, the location should not be targeted again. The location should
 * indicate whether the user has sunk a ship or not. A player wins when he has sunk all ships on his
 * board.
 * 
 * NOTE: The Ships in the array of ships are the ship objects placed on the board. The gameBoard
 * contains the points that are on the board.
 * 
 * The name of the board is the board that is played on. That means that the board the computer
 * sets up, and the board that the player sets up, is not their own boards; in fact, it is
 * the boards of the opponent. 
 */
public class Board extends JPanel{
	private int height;
	private int length;
	private String playerName;
	private String shipHasSunkName;
	private Points[][] gameBoard;
	private Set<Ship> ships;
	private boolean firstStep;
	private int highScore;
	
	//Private variables mainly for the AI algorithm
	private boolean hotMode;
	private int hotModeX;
	private int hotModeY;
	private int placeHolderX;
	private int placeHolderY;
	private int randomDirection;
	
	/**
	 * Creates an empty board. An empty board should have all its points non-targeted and not 
	 * containing any current ship.
	 * @param x the length of the board
	 * @param y the height of the board
	 * @param name the name of the player
	 * @param whether the ship is in step one (placing other ships) or not.
	 */
	
	public Board(int x, int y, String name, boolean step){
		this.height = y; //width of board
		this.length = x; //length of board
		this.playerName = name; //name of board owner
		this.shipHasSunkName = "";
		this.gameBoard = new Points[height][length]; //the actual board of points
		this.ships = new HashSet<Ship>(); //set of ships belonging to the board
		this.firstStep = step; //whether the user is placing ships on the board or targeting ships
		this.highScore = 10000; //the high score of the game
		
		//Private variables mainly for the AI algorithm
		this.hotMode = false;
		this.hotModeX = 0;
		this.hotModeY = 0;
		this.placeHolderX = 0;
		this.placeHolderY = 0;
		this.randomDirection = 0;
		
		for (int i = 0; i < height; i++){
			for (int j = 0; j < length; j++){
				gameBoard[i][j] = new Points(i, j, null, -1);
			}
		}
	}
	
	/**
	 * Draw a board
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (int i = 0; i < this.height; i++){
			for (int j = 0; j < this.length; j++){
				g.setColor(Color.black);
				g.drawRect(50 * j, 50 * i, 50, 50);
				if (firstStep && !gameBoard[i][j].isEmpty()){
					g.setColor(Color.green); //user has set ship on this point
				}
				else if (gameBoard[i][j].hasShipSunk()){
					g.setColor(Color.blue); //ship has been sunk
				}
				else if (!gameBoard[i][j].hasBeenAttacked()){
					g.setColor(Color.white); //point has not yet been targeted
				}
				else if (gameBoard[i][j].hasBeenAttacked() && gameBoard[i][j].isEmpty()){
					g.setColor(Color.red); //point with no ship has already been attacked
				}
				else {
					g.setColor(Color.GREEN); //user has targeted a point with a ship
				}
				g.fillRect(50 * j + 1, 50 * i + 1, 49, 49);
			}
		}
	}
	
	/**
	 * Get the size of the board
	 */
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(500,500);
	}
	
	public void makeRandomBoard(){
		Set<Ship> myShips = getSetShips();
		int width = this.getLength();
		int height = this.getHeight();
		Points[][] gameBoard = this.getGameBoard();
		
		boolean RotatePatrolBoat = Board.Converter((int) (Math.random() * 2));
		boolean RotateDestroyer = Board.Converter((int) (Math.random() * 2));
		boolean RotateBattleship = Board.Converter((int) (Math.random() * 2));
		boolean RotateAircraftCarrier = Board.Converter((int) (Math.random() * 2));

		boolean [] arrIsRotate = 
			{RotatePatrolBoat, RotateDestroyer, RotateBattleship, RotateAircraftCarrier};
		
		Ship randomAircraftCarrier = new Ship(arrIsRotate[3], 5, "Aircarft Carrier");
		Ship randomBattleShip = new Ship(arrIsRotate[2], 4, "BattleShip");
		Ship randomDestroyer = new Ship(arrIsRotate[1], 3, "Destroyer");
		Ship randomPatrolBoat = new Ship(arrIsRotate[0], 2, "Patrol Boat");
		
		myShips.add(randomPatrolBoat);
		myShips.add(randomDestroyer);
		myShips.add(randomBattleShip);
		myShips.add(randomAircraftCarrier);
		
		Ship[] myShipArray = new Ship[4];
		myShipArray[0] = randomPatrolBoat;
		myShipArray[1] = randomDestroyer;
		myShipArray[2] = randomBattleShip;
		myShipArray[3] = randomAircraftCarrier;
		
		for (int length = 2; length < myShips.size() + 2; length++){
			if (!arrIsRotate[length - 2]) {
				boolean doesNotIntersect = false;
				while (!doesNotIntersect){
					int guessShipX = (int) (Math.random() * (width - length + 1));
					int guessShipY = (int) (Math.random() * (height));
					if (this.checkNotOverlapping(false, length, guessShipX, guessShipY)){
						doesNotIntersect = true;
					    for (int i = guessShipX; i < guessShipX + length; i++){
						    Points toCheck = gameBoard[guessShipY][i];
						    toCheck.setCoordinates(i, guessShipY);
						    toCheck.setShipIndex(i - guessShipX);
						    toCheck.setShip(myShipArray[length - 2]);
						    myShipArray[length - 2].getPoints()[i - guessShipX] = toCheck;
					    }
					}
				}
			}
			else {
				boolean doesNotIntersect = false;
				while (!doesNotIntersect){
					int guessShipX = (int) (Math.random() * (width));
					int guessShipY = (int) (Math.random() * (height - length + 1));
					if (this.checkNotOverlapping(true, length, guessShipX, guessShipY)){
						doesNotIntersect = true;
					    for (int i = guessShipY; i < guessShipY + length; i++){
						    Points toCheck = gameBoard[i][guessShipX];
						    toCheck.setCoordinates(guessShipX, i);
						    toCheck.setShipIndex(i - guessShipY);
						    toCheck.setShip(myShipArray[length - 2]);
						    Ship thisShip = myShipArray[length - 2];
						    thisShip.getPoints()[i - guessShipY] = toCheck;						
					    }
					}
				}
			}
		}
	}
	
	/**
	 * Static method to convert a number computed from the random generator into a boolean value
	 * @param number the number from 0 to 1
	 * @return the boolean value associated with the number
	 */
	public static boolean Converter(int number){
		return number == 1;
	}
	
	/**
	 * Static method to convert a length to a name
	 * @param length the length of the ship
	 * @return name of the ship
	 */
	public static String ConverterShip(int length){
		if (length == 2){
			return "PatrolBoat";
		}
		else if (length == 3){
			return "Destroyer";
		}
		else if (length == 4){
			return "BattleShip";
		}
		else{
			return "Aircraft-Carrier";
		}
	}
	
	/**
	 * Return the game-board
	 * @return the game-board
	 */
	public Points[][] getGameBoard(){
		return gameBoard;
	}
	
	/**
	 * Get the height of the Board
	 * @return the height of the Board
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Get the length of the Board
	 * @return the length of the board
	 */
	public int getLength(){
		return length;
	}
	
	/**
	 * Get the Array of Ships in the Board
	 * @return Array of Ships in the Board
	 */
	public Set<Ship> getSetShips(){
		return ships;
	}
	
	/**
	 * Get the Name of the player using the board
	 * @return Name of player
	 */
	public String getName(){
		return playerName;
	}
	
	/**
	 * Set the name of the player using the board
	 */
	public void changeName(String newName){
		this.playerName = newName;
	}
	
	/**
	 * Returns whether board is in first step or not
	 */
	public boolean getStep(){
		return firstStep;
	}
	
	/**
	 * Indicates at what step the player is at (placing ships, or attacking ships)
	 */
	public void setStep(boolean step){
		this.firstStep = step;
	}
	
	/**
	 * Reset the Board
	 */
	public void reset(){
		for (int i = 0; i < length; i++){
			for (int j = 0; j < height; j++){
				Points reducePoint = gameBoard[i][j];
				if (!reducePoint.isEmpty()){
					reducePoint.setShip(null);
					reducePoint.setShipIndex(-1);
				}
			}
		}
		for (Ship myShip : ships){
			myShip.reset();
		}
	}
	
	/**
	 * Checks to see whether the ship placed is out of bounds
	 * @param x the x coordinate of leftmost or topmost point: zero-based
	 * @param y the y coordinate of rightmost or bottommost point: zero-based
	 * @param length the length of the ship
	 * @param isRotated whether the ship is rotated or not
	 * @return whether the ship placed is out of bounds
	 */
	public boolean notOutOfBounds (int x, int y, int shipLength, boolean isRotated){
		if (!isRotated){
			return (this.length - (x + shipLength)) >= 0;
		}
		else {
			return (this.height - (y + shipLength)) >= 0;
		}
	}
	
	/**
	 * Method to check whether adding a ship overlaps
	 * @param length the length of the ship
	 * @param x coordinate of leftmost piece of ship
	 * @param y coordinate of leftmost piece of ship
	 * @return boolean value whether the ship overlap
	 */
	public boolean checkNotOverlapping(boolean rotate, int length, int x, int y){
		if (!rotate){
		    for (int i = x; i < x + length; i++){
			    Points toCheck = gameBoard[y][i];
			    if (!toCheck.isEmpty()){
				    return false;
			    }
		    }
		    return true;
	    }
		else{
			for (int i = y; i < y + length; i++){
				Points toCheck = gameBoard[i][x];
			    if (!toCheck.isEmpty()){
				    return false;
			    }
			}
			return true;
		}
	}
	
	/**
	 * Put a boat on the Board. The coordinates of the argument is the leftmost
	 * or topmost point of the ship.
	 * @param x the x coordinate of the leftmost point (Column Value): zero-based
	 * @param y the y coordinate of the leftmost point (Row Value): zero-based
	 * @param AddedShip the Ship to be added
	 */
	public void putBoat(int x, int y, Ship addedShip){
		ships.add(addedShip);
		int length = addedShip.getLength();
		Points[] shipPoints = addedShip.getPoints();
		if (!this.notOutOfBounds(x, y, length, addedShip.isItRotated())){
			throw new IllegalArgumentException();
		}
		if (!this.checkNotOverlapping(addedShip.isItRotated(), length, x, y)){
			throw new IllegalArgumentException();
		}
		if (!addedShip.isItRotated()){
			for (int j = x; j < x + length; j++){
				Points setPoint = this.gameBoard[y][j];
				setPoint.setCoordinates(j, y);
				setPoint.setShip(addedShip);
				setPoint.setShipIndex(j-x);
				shipPoints[j-x] = setPoint;
			    }
		    }
		else if (addedShip.isItRotated()){
			for (int i = y; i < y + length; i++){
				Points setPoint = this.gameBoard[i][x];
				setPoint.setCoordinates(x, i);
				setPoint.setShip(addedShip);
				setPoint.setShipIndex(i-y);
				shipPoints[i-y] = setPoint;
			}
		}
	}
	
	/**
	 * When a user targets a specified location.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void targetLocation(int x, int y){
		Points targetedPoint = this.gameBoard[y][x];
		
		if (targetedPoint.hasBeenAttacked()){
			throw new IllegalArgumentException();
		}
		targetedPoint.Target();
		this.highScore = this.highScore - 1000;
	}
	
	/**
	 * checks whether the player is the winner
	 * @return whether the player is the winner or not.
	 */
	public boolean hasWon(){
		for (Ship individualShip : ships){
			if (!individualShip.Destroyed()){
				return false;
			}
		}
		this.highScore += 100000;
		return true;
	}
	
	/**
	 * Prints out the name of the player who has won
	 */
	public void hasWonMessage(){
		if(this.hasWon()){
			System.out.println(this.playerName + " has won");
		}
		else{
			System.out.println(this.playerName + " has not won yet");
		}
	}
	
	/**
	 * Returns whether a ship has been recently sunk due to a targeted point
	 * @return whether a ship has just been recently sunk
	 */
	public boolean hasAShipSunk(){
		for (Ship myShip : ships){
			if (!myShip.isDestroyed() && myShip.Destroyed()){
				Points[] shipPoints = myShip.getPoints();
				for (int i = 0; i < shipPoints.length; i++){
					shipPoints[i].sinkShip();
				}
				this.shipHasSunkName = myShip.getName();
				return true;
			}
		}
		return false;
	}
	
	public String returnSunkShipName(){
		return shipHasSunkName;
	}
	
	/**
	 * Return the high score of the particular board
	 * @return the high score
	 */
	public int returnScore(){
		return this.highScore;
	}

	
	/**
	 * ---------------------------------------Random "Fair" Algorithm ------------------------
	 */
	
	/**
	 * When the computer picks a point at random. Easy mode of the Game.
	 * @param ComputerBoard the board of the computer
	 */
	public void ComputerEasyModeAttack(){
		boolean notIntersect = false;
		
		while (!notIntersect){
			int x = (int) (Math.random() * this.length);
			int y = (int) (Math.random() * this.height);
			if (!this.gameBoard[y][x].hasBeenAttacked()){
				targetLocation(x, y);
				notIntersect = true;
			    }
			}
		}
	
	/**
	 * Scans the board to see if there exists a point that has been attacked by the user
	 * , where the point contains a ship but the ship has not yet been sunk
	 * @return whether there exists another point or not
	 */
	public boolean existsAnotherPoint(){
		for (int i = 0; i < this.height; i++){
			for (int j = 0; j < this.length; j++){
				Points pointToCheck = this.gameBoard[i][j];
				if (pointToCheck.hasBeenAttacked() && !pointToCheck.isEmpty()){
					if (!pointToCheck.getShip().Destroyed()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the exact point so that the AI may use that point for "hot mode"
	 * @return the actual point that could be considered as "hot mode"
	 * NOTE: Only use if existsAnotherPoint is true. Check existsAnotherPoint first.
	 */
	public Points getAnotherPoint(){
		for (int i = 0; i < this.height; i++){
			for (int j = 0; j < this.length; j++){
				Points pointToCheck = this.gameBoard[i][j];
				if (pointToCheck.hasBeenAttacked() && !pointToCheck.isEmpty()){
					if (!pointToCheck.getShip().Destroyed()){
						return pointToCheck;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * The regular 
	 */
	public void ComputerRegularModeAttack(){
		if (!hotMode){
			ComputerAIRandom();
		}
		else{
			computerAIHotMode();
		}
	}
	
	/**
	 * When the computer is at random mode. The computer should pick a point at random.
	 */
	public void ComputerAIRandom(){
		boolean notIntersect = false;
		while (!notIntersect){
			int x = (int) (Math.random() * this.length);
			int y = (int) (Math.random() * this.height);
			
			if (!this.gameBoard[y][x].hasBeenAttacked()){
				targetLocation(x, y);
				notIntersect = true;
				if (!gameBoard[y][x].isEmpty()) {
					hotModeX = x;
					hotModeY = y;
					placeHolderX = x;
					placeHolderY = y;
					hotMode = true;
					randomDirection = (int) (Math.random() * 4);
				    }
			    }
			}
		}

	/**
	 * When the computer is in hot mode. The computer should pick a point with the knowedlge that
	 * a point it has targeted contains a ship
	 */
	public void computerAIHotMode(){
		Points originalPoint = gameBoard[this.hotModeY][this.hotModeX];
		computerAIHotModeSeeking();
		if (originalPoint.getShip().Destroyed()){
			if (existsAnotherPoint()){
				Points newHotMode = getAnotherPoint();
				this.hotModeX = newHotMode.getXCoordinate();
				this.hotModeY = newHotMode.getYCoordinate();
				this.placeHolderX = hotModeX;
				this.placeHolderY = hotModeY;
				this.randomDirection = (int) (Math.random() * 4);
			}
			else{
				this.hotMode = false;
				this.hotModeX = 0;
				this.hotModeY = 0;
				this.placeHolderX = 0;
				this.placeHolderY = 0;
				this.randomDirection = 0;
			}
		}
	}
	
	/**
	 * The computer targets a point with the understanding that a nearby point contains a ship
	 */
	public void computerAIHotModeSeeking(){
		if (randomDirection == 0){
			int hotModeNext0 = placeHolderX + 1; //to the right
			if (hotModeNext0 < length){
				Points hotNext0 = gameBoard[this.hotModeY][hotModeNext0];
				if(!hotNext0.isEmpty() && hotNext0.hasBeenAttacked()){
					if (!hotNext0.getShip().Destroyed()){
						boolean check = false; //want to check the special case when the next point
						                       //is attacked and has shown to hold some boat
						for (int i = hotModeNext0 + 1; i < length; i++){
							Points trackNext0 = this.gameBoard[hotModeY][i];
							if (trackNext0.hasBeenAttacked() && trackNext0.isEmpty()){
								placeHolderX = hotModeX; //restart position
								randomDirection = (int) (Math.random() * 4);
								check = true;
								this.computerAIHotModeSeeking();
								i = length;
							}
							else if(!trackNext0.hasBeenAttacked()){
								check = true; //found a good position to check
								targetLocation(i, this.hotModeY);
								if (!trackNext0.isEmpty()){
									placeHolderX = i;
								}
								else{
									placeHolderX = hotModeX;
									randomDirection = (int) (Math.random() * 4);
								}
								i = length;
							}
						}
						if (!check){
							placeHolderX = hotModeX; //restart at original point
							randomDirection = (int) (Math.random() * 4);
							this.computerAIHotModeSeeking();
						}
					}
					else{
						placeHolderX = hotModeX; //restart at original point
						randomDirection = (int) (Math.random() * 4);
						this.computerAIHotModeSeeking();
					}
				}
				else if(!hotNext0.hasBeenAttacked()){
					targetLocation(hotModeNext0, hotModeY);
					if (!hotNext0.isEmpty()){
						placeHolderX = hotModeNext0;
					}
					else{
						placeHolderX = hotModeX;
						randomDirection = (int) (Math.random() * 4);
					}
				}
				else{
					placeHolderX = this.hotModeX;//right position is bad; restart at original point
					randomDirection = (int) (Math.random() * 4);
					this.computerAIHotModeSeeking();
				}
			}
			else{
				placeHolderX = this.hotModeX; //right position is bad; restart at original point
				randomDirection = (int) (Math.random() * 4);
				this.computerAIHotModeSeeking();
			}
		}
		
		else if (this.randomDirection == 1){
			int hotModeNext1 = placeHolderX - 1; //to the left
			if (hotModeNext1 >= 0){
				Points hotNext1 = gameBoard[hotModeY][hotModeNext1];
				if(!hotNext1.isEmpty() && hotNext1.hasBeenAttacked()){
					if(!hotNext1.getShip().Destroyed()){
					boolean check = false; //special case if the next left position has been
					                       //attacked but shown to hide some boat
					for (int i = hotModeNext1 - 1; i >= 0; i--){
						Points trackNext1 = gameBoard[this.hotModeY][i];
						if (trackNext1.hasBeenAttacked() && trackNext1.isEmpty()){
							placeHolderX = hotModeX;
							randomDirection = (int) (Math.random() * 4);
							check = true;
							computerAIHotModeSeeking();
							i = -1;
						}
						else if(!trackNext1.hasBeenAttacked()){
							check = true;
							targetLocation(i, hotModeY);
							if (!trackNext1.isEmpty()){
								placeHolderX = i;
							}
							else{
								placeHolderX = hotModeX;
								randomDirection = (int) (Math.random() * 4);
							}
							i = -1;
						}
					}
					if (!check){
						placeHolderX = hotModeX;
						randomDirection = (int) (Math.random() * 4);
						this.computerAIHotModeSeeking();
					}					
				}
					else{
						placeHolderX = hotModeX;
						randomDirection = (int) (Math.random() * 4);
						this.computerAIHotModeSeeking();	
					}
				}
				else if (!hotNext1.hasBeenAttacked()){
					targetLocation(hotModeNext1, this.hotModeY);
					if (!hotNext1.isEmpty()){
						placeHolderX = hotModeNext1;
					}
					else{
						placeHolderX = hotModeX;
						randomDirection = (int)(Math.random() * 4);
					}
				}
				else{
					placeHolderX = hotModeX;
					randomDirection = (int) (Math.random() * 4);
					this.computerAIHotModeSeeking();
				}
			}
			else{
				placeHolderX = hotModeX;
				randomDirection = (int) (Math.random() * 4);
				this.computerAIHotModeSeeking();
			}
		}
		
		else if (this.randomDirection == 2){
			int hotModeNext2 = placeHolderY + 1; //go up
			if (hotModeNext2 < height){
				Points hotNext2 = gameBoard[hotModeNext2][hotModeX];
				if(!hotNext2.isEmpty() && hotNext2.hasBeenAttacked()){
					if (!hotNext2.getShip().Destroyed()){
						boolean check = false; //special case when the next up position has been
						                       //attacked but shown to hold some boat
						for (int i = hotModeNext2 + 1; i < height; i++){
							Points trackNext2 = gameBoard[i][hotModeX];
							if (trackNext2.hasBeenAttacked() && trackNext2.isEmpty()){
								placeHolderY = hotModeY;
								randomDirection = (int) (Math.random() * 4);
								check = true;
								this.computerAIHotModeSeeking();
								i = length;
							}
							else if(!trackNext2.hasBeenAttacked()){
								check = true;
								targetLocation(hotModeX, i);
								if (!trackNext2.isEmpty()){
									placeHolderY = i;
								}
								else{
									placeHolderY = hotModeY;
									randomDirection = (int) (Math.random() * 4);
								}
								i = length;
							}
						}
						if (!check){
							placeHolderY = hotModeY;
							randomDirection = (int) (Math.random() * 4);
							this.computerAIHotModeSeeking();
						}
					}
					else{
						placeHolderY = hotModeY;
						randomDirection = (int) (Math.random() * 4);
						this.computerAIHotModeSeeking();		
					}
				}

				else if (!hotNext2.hasBeenAttacked()){
					targetLocation(this.hotModeX, hotModeNext2);
					if (!hotNext2.isEmpty()){
						placeHolderY = hotModeNext2;
					}
					else{
						placeHolderY = hotModeY;
						randomDirection = (int)(Math.random() * 4);
					}
				}
				else{
					placeHolderY = hotModeY;
					randomDirection = (int) (Math.random() * 4);
					this.computerAIHotModeSeeking();
				}
			}
			else{
				placeHolderY = this.hotModeY;
				randomDirection = (int) (Math.random() * 4);
				this.computerAIHotModeSeeking();
			}
		}
		
		else {
			int hotModeNext3 = placeHolderY - 1; //go down
			if (hotModeNext3 >= 0){
				Points hotNext3 = gameBoard[hotModeNext3][this.hotModeX];
				if(!hotNext3.isEmpty() && hotNext3.hasBeenAttacked()){
					if (!hotNext3.getShip().Destroyed()){
						boolean check = false; //special case when the next down position has been
						                       //attacked but has some boat
						for (int i = hotModeNext3 - 1; i >= 0; i--){
							Points trackNext3 = gameBoard[i][this.hotModeX];
							if (trackNext3.hasBeenAttacked() && trackNext3.isEmpty()){
								placeHolderY = hotModeY;
								randomDirection = (int) (Math.random() * 4);
								check = true;
								this.computerAIHotModeSeeking();
								i = 0;
								}
							else if(!trackNext3.hasBeenAttacked()){
								check = true;
								targetLocation(this.hotModeX, i);
								if (!trackNext3.isEmpty()){
									placeHolderY = i;
									}
								else{
									placeHolderY = this.hotModeY;
									randomDirection = (int) (Math.random() * 4);
									}
								i = 0;
								}
							}
						if (!check){
							placeHolderY = hotModeY;
							randomDirection = (int) (Math.random() * 4);
							computerAIHotModeSeeking();
							}
						}
					else{
						placeHolderY = hotModeY;
						randomDirection = (int) (Math.random() * 4);
						computerAIHotModeSeeking();
					}
				}
				else if (!hotNext3.hasBeenAttacked()){
					targetLocation(this.hotModeX, hotModeNext3);
					
					if(!hotNext3.isEmpty()){
						placeHolderY = hotModeNext3;
					}
					else{
						placeHolderY = this.hotModeY;
						randomDirection = (int)(Math.random() * 4);
					}
				}
				else{
					placeHolderY = this.hotModeY;
					randomDirection = (int) (Math.random() * 4);
					this.computerAIHotModeSeeking();
				}
			}
			else{
				placeHolderY = this.hotModeY;
				randomDirection = (int) (Math.random() * 4);
				this.computerAIHotModeSeeking();
			}
		}
	}
	
}