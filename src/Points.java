
/**
 * 
 * A unit for the Game Board. Each point represents a specified coordinate and tracks
 * whether the coordinate has already been targeted by the user.
 * 
 */
public class Points {
	
	private int x;
	private int y;
	private boolean hasBeenAttacked;
	private boolean shipHasSunk;
	private Ship myShip;
	private int shipIndex;
	
	public Points(int xCoordinate, int yCoordinate, Ship newShip, int shipIndexValue){
		this.x = xCoordinate;
		this.y = yCoordinate;
		this.hasBeenAttacked = false;
		this.shipHasSunk = false;
		this.myShip = newShip;  // null if no ship is at this point
		this.shipIndex = shipIndexValue; // -1 if no ship is on this point
	}
	
	/**
	 * Returns whether the Point is currently empty or not. In other words, the method checks 
	 * whether the coordinate hides a boat or not.
	 * @return whether the ship holds a boat or not.
	 */
	public boolean isEmpty(){
		return myShip == null;
	}
	
	public int getXCoordinate() {
		return x;
	}
	
	public int getYCoordinate() {
		return y;
	}
	
	public int getShipIndex(){
		return shipIndex;
	}
	
	public boolean hasBeenAttacked(){
		return hasBeenAttacked;
	}
	
	public Ship getShip(){
		return myShip;
	}
	
	/**
	 * Target the point. The point as a result should now be indicated as targeted.
	 */
	public void Target(){
		hasBeenAttacked = true;
	}
	
	/**
	 * Sets the ship that the point is a part of
	 */
	public void setShip(Ship newShip){
		myShip = newShip;
	}
	
	/**
	 * Set new coordinates for the point
	 * @param newXCoordinate the new x coordinate of the point
	 * @param newYCoordinate the new y coordinate of the point
	 */
	public void setCoordinates(int newXCoordinate, int newYCoordinate){
		x = newXCoordinate;
		y = newYCoordinate;
	}
	
	/**
	 * Set the Ship Index of the point
	 * @param shipIndexValue new ship index of the point
	 */
	public void setShipIndex(int shipIndexValue){
		shipIndex = shipIndexValue;
	}
	
	/**
	 * Sink the ship. The points as a result should show that the ship has been sunk.
	 */
	public void sinkShip(){
		this.shipHasSunk = true;
	}

	/**
	 * Return whether the ship the point contains has sunk yet or not
	 * @return whether the ship the point contains has sunk yet or not
	 */
	public boolean hasShipSunk(){
		return shipHasSunk;
	}
	
	@Override
	public boolean equals (Object o){
		if (this == o) {return true;}
		if (!(o instanceof Points)) {return false;}
		Points that = (Points) o;
		if (this.x != that.getXCoordinate()) {return false;}
		if (this.y != that.getYCoordinate()) {return false;}
		if (this.hasBeenAttacked != that.hasBeenAttacked()) {return false;}
		if (this.shipHasSunk != that.shipHasSunk) {return false;}
		if (!this.myShip.equals(that.getShip())) {return false;}
		if (this.shipIndex != that.getShipIndex()) {return false;}
		return true;
	}
}