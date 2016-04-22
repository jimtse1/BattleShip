/**
 * The AircraftCarrier Ship is a ship of length five. The name of the AircraftCarrier is 
 * "Aircraft-Carrier"
 *
 */
public class Ship {
	
	private int length;
	private String name;
	private boolean isRotated;
	private boolean isDestroyed;
	private Points[] myPoints;

	public Ship(boolean rotate, int shipLength, String myName){
		this.length = shipLength;
		this.name = myName;
		this.isRotated = rotate;
		this.myPoints = new Points[shipLength];
		this.isDestroyed = false;
	}
	
	/**
	 * The length of the ship.
	 * @return the length of the ship
	 */
	public int getLength(){
		return length;
	}
	
	/**
	 * The name of the ship based on its length
	 * @return the name of the ship
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Whether the ship has been rotated or not
	 * @return whether the ship is rotated
	 */
	public boolean isItRotated(){
		return isRotated;
	}
	
	/**
	 * Rotate the ship
	 */
	public void rotate(){
		isRotated = !isRotated;
	}
	
	/**
	 * Reset the points for the ship object
	 */
	public void reset(){
		myPoints = new Points [length];
	}
	
	/**
	 * Return the points that are currently hiding the ship
	 * @return the points of the ship
	 */
	public Points[] getPoints(){
		return myPoints;
	}
	
	/**
	 * Check whether the ship has been destroyed in the game
	 * @return if the ship has been destroyed or not
	 */
	public boolean Destroyed(){
		for (int i = 0; i < length; i++){
			if (myPoints[i] == null || !myPoints[i].hasBeenAttacked()){
				return false;
			}
		}
		isDestroyed = true;
		return true;
	}
	
	/**
	 * Check whether the ship has been displayed by the game as destroyed before update
	 */
	public boolean isDestroyed(){
		return isDestroyed;
	}
	
	/**
	 * Override the equals method inherited from type Object.
	 * Want equals to represent structural equality, not referential.
	 * @param o the object to be compared with to check equality
	 */
	@Override
	public boolean equals (Object o){
		if (this == o) {return true;}
		if (!(o instanceof Ship)) {return false;}
		Ship that = (Ship) o;
		if (this.length != that.getLength()) {return false;}
		if (!this.name.equals(that.getName())) {return false;}
		if (this.isRotated != that.isItRotated()) {return false;}
		if (!this.myPoints.equals(that.getPoints())) {return false;}
		return true;
	}
	
}