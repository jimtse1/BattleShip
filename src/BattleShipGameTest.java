import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;


public class BattleShipGameTest {
	
	@Test
	public void testHorizontalRotations(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][] gameBoard = testBoard.getGameBoard();
		testBoard.putBoat(0, 0, new Ship(false, 4, "Destroyer"));
		assertFalse("Point should not be empty", gameBoard[0][0].isEmpty());
		assertFalse("Point should not be empty", gameBoard[0][1].isEmpty());
		assertFalse("Point should not be empty", gameBoard[0][2].isEmpty());
		assertFalse("Point should not be empty", gameBoard[0][3].isEmpty());
		assertTrue("Point should be empty", gameBoard[1][0].isEmpty());
	}
	
	@Test
	public void testVerticalRotations(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][] gameBoard = testBoard.getGameBoard();
		testBoard.putBoat(0, 0, new Ship(true, 4, "BattleShip"));
		assertFalse("Point should not be empty", gameBoard[0][0].isEmpty());
		assertFalse("Point should not be empty", gameBoard[1][0].isEmpty());
		assertFalse("Point should not be empty", gameBoard[2][0].isEmpty());
		assertFalse("Point should not be empty", gameBoard[3][0].isEmpty());
		assertTrue("Point should be empty", gameBoard[0][1].isEmpty());
	}

	@Test
	public void testHasWon(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][] gameBoard = testBoard.getGameBoard();
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);
		
		testBoard.targetLocation(0, 1);
		testBoard.targetLocation(1, 1);
		testBoard.targetLocation(2, 1);
		testBoard.targetLocation(3, 1);
		
		testBoard.targetLocation(0, 2);
		testBoard.targetLocation(1, 2);
		testBoard.targetLocation(2, 2);
		
		testBoard.targetLocation(0, 3);
		testBoard.targetLocation(1, 3);
		
		assertTrue("Board should be sunk", testBoard.hasWon());
		
	}
	
	@Test
	public void testHasNotYetWon(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][] gameBoard = testBoard.getGameBoard();
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);
		
		testBoard.targetLocation(0, 1);
		testBoard.targetLocation(1, 1);
		testBoard.targetLocation(2, 1);
		testBoard.targetLocation(3, 1);
		
		testBoard.targetLocation(0, 2);
		testBoard.targetLocation(1, 2);
		testBoard.targetLocation(2, 2);
		
		testBoard.targetLocation(0, 3);
		
		assertFalse("Board should be sunk", testBoard.hasWon());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void placeShipOutOfBoundsHorizontal(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(6, 0, new Ship(false, 5, "Aircraft Carrier"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void placeShipOutOfBoundsVertical(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(0, 6, new Ship(true, 5, "Aircraft Carrier"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlaceAlreadyPlaced(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(0, 0, new Ship (true, 5, "Aircraft Carrier"));		
		testBoard.putBoat(0, 0, new Ship(true, 3, "Destroyer"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlaceAlreadyPlacedOneOverlapping(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(0, 0, new Ship (false, 5, "Aircraft Carrier"));		
		testBoard.putBoat(4, 0, new Ship(false, 3, "Destroyer"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHitAlreadyAttacked(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(0, 0, new Ship (false, 5, "Aircraft Carrier"));		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(0, 0);
	}
	
	@Test
	public void testIfEmpty(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][]gameBoard = testBoard.getGameBoard();
		testBoard.putBoat(0, 0, new Ship (false, 5, "Aircraft Carrier"));		
		assertTrue(gameBoard[9][9].isEmpty());
	}

	@Test
	public void testshipShouldSink(){
		Board testBoard = new Board(10, 10, "test", false);
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);
		
		testBoard.targetLocation(0, 1);
		testBoard.targetLocation(1, 1);
		testBoard.targetLocation(2, 1);
		testBoard.targetLocation(3, 1);
		
		testBoard.targetLocation(0, 2);
		testBoard.targetLocation(1, 2);
		testBoard.targetLocation(2, 2);
		
		testBoard.targetLocation(0, 3);
		testBoard.targetLocation(1, 3);
		
		Set<Ship> arrayShips = testBoard.getSetShips();
		for (Ship s : arrayShips){
			assertTrue(s.Destroyed());

		}
	}
	
	@Test
	public void testPointsHaveSunk(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][]gameBoard = testBoard.getGameBoard();
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);
		
		testBoard.targetLocation(0, 1);
		testBoard.targetLocation(1, 1);
		testBoard.targetLocation(2, 1);
		testBoard.targetLocation(3, 1);
		
		testBoard.targetLocation(0, 2);
		testBoard.targetLocation(1, 2);
		testBoard.targetLocation(2, 2);
		
		testBoard.targetLocation(0, 3);
		testBoard.targetLocation(1, 3);
		
		testBoard.hasAShipSunk();
		testBoard.hasAShipSunk();
		testBoard.hasAShipSunk();
		testBoard.hasAShipSunk();
		
		assertTrue(gameBoard[0][0].hasShipSunk());
		assertTrue(gameBoard[0][1].hasShipSunk());
		assertTrue(gameBoard[0][2].hasShipSunk());
		assertTrue(gameBoard[0][3].hasShipSunk());
	}
	
	@Test
	public void testShipNameHasJustSunk(){
		Board testBoard = new Board(10, 10, "test", false);
		Points[][]gameBoard = testBoard.getGameBoard();
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);

		testBoard.hasAShipSunk();
		String shipName = testBoard.returnSunkShipName();
		assertEquals("Aircraft Carrier", shipName);
	}
	
	@Test
	public void testShipNameShouldOnlyShowSunkOnce(){
		Board testBoard = new Board(10, 10, "test", false);
		
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		testBoard.targetLocation(0, 0);
		testBoard.targetLocation(1, 0);
		testBoard.targetLocation(2, 0);
		testBoard.targetLocation(3, 0);
		testBoard.targetLocation(4, 0);

		testBoard.hasAShipSunk();
		assertFalse(testBoard.hasAShipSunk());
	}
	
	@Test
	public void testRandomAIGenerator(){
		Board testBoard = new Board(10, 10, "test", false);
		testBoard.putBoat(0, 0, new Ship(false, 5, "Aircraft Carrier"));
		testBoard.putBoat(0, 1, new Ship(false, 4, "Battle Ship"));
		testBoard.putBoat(0, 2, new Ship(false, 3, "Destroyer"));
		testBoard.putBoat(0, 3, new Ship(false, 2, "Patrol Boat"));
		
		for (int i = 0; i < 100; i++){
			testBoard.ComputerEasyModeAttack();
		}
		assertTrue(testBoard.hasWon());
	}
	
}
	