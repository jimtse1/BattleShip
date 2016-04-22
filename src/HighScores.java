import java.util.*;
import java.io.*;

public class HighScores {
	
	private static final String BATTLESHIP_HIGHSCORE_FILE = "scores.txt";
	
	private ObjectOutputStream outputStream; 
	private ObjectInputStream inputStream;
	
	private ArrayList<String> arrayOfScores;
	private ArrayList<Integer> arrayOfIntegers;
	private ArrayList<String> arrayOfNames;
	
	
	public HighScores(){
		arrayOfScores = new ArrayList<String>();
		arrayOfIntegers = new ArrayList<Integer>();
		arrayOfNames = new ArrayList<String>();
		outputStream = null;
		inputStream = null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getMyScores() 
			throws FileNotFoundException, IOException, ClassNotFoundException{
		
		//update the array based on the file
		try {
		this.inputStream = new ObjectInputStream(new FileInputStream("scores.txt"));
		this.arrayOfScores = (ArrayList<String>) this.inputStream.readObject();
		}
		catch (FileNotFoundException e){
			System.out.println("File not found -- Try again m8");
		}
		catch (IOException e){
			System.out.println("Error: Try again m8");
		}
		catch(ClassNotFoundException e){
			System.out.println("Error: Try again m8");
		}
		finally{
			outputStream.flush();
			outputStream.close();
		}
		return this.arrayOfScores;
	}
	
	@SuppressWarnings("unchecked")
	public void addScore(String nameAndInteger) 
			throws FileNotFoundException, IOException{		
		//update the array based on the file
		try {
		this.inputStream = new ObjectInputStream(new FileInputStream("scores.txt"));
		this.arrayOfScores = (ArrayList<String>) this.inputStream.readObject();
		}
		catch (FileNotFoundException e){
			System.out.println("File not found -- Try again m8");
		}
		catch (IOException e){
			System.out.println("Error: Try again m8");
		}
		catch(ClassNotFoundException e){
			System.out.println("Error: Try again m8");
		}
		finally{
			try{
				if (outputStream != null){
				this.outputStream.flush();
				this.outputStream.close();
				}
			}
			catch (IOException e){
				System.out.println("Error: Try again");
			}
		}
		
		//add the score to the arrayList of scores
		this.arrayOfScores.add(nameAndInteger);
		
		//update the file to add the new score
		try {
		this.outputStream = new ObjectOutputStream(new FileOutputStream(BATTLESHIP_HIGHSCORE_FILE));
		this.outputStream.writeObject(this.arrayOfScores);
		}
		catch (FileNotFoundException e){
			System.out.println("File not found -- Try again m8");
		}
		catch (IOException e){
			System.out.println("Error: Try again m8");
		}
		finally{
			outputStream.flush();
			outputStream.close();
		}
	}
	
	//Get The High Score
	public String getHighScore(){
		for (int i = 0; i < this.arrayOfScores.size() ; i++){
			String needToConvert = this.arrayOfScores.get(i);
			String [] arrayParse = needToConvert.split(":");
			int needToAdd = Integer.parseInt(arrayParse[1].trim());
			this.arrayOfNames.add(arrayParse[0]);
			this.arrayOfIntegers.add(needToAdd);
		}
		int max = Integer.MIN_VALUE;
		int max2 = Integer.MIN_VALUE;
		int index1 = 0;
		int index2 = 0;
		
		for (int i = 0; i < arrayOfIntegers.size(); i++){
			if (arrayOfIntegers.get(i) > max){
				max = arrayOfIntegers.get(i);
				index1 = i;
			}
		}
		
		for (int i = 0; i < arrayOfIntegers.size(); i++){
			if (arrayOfIntegers.get(i) > max2 && i != index1){
				max2 = arrayOfIntegers.get(i);
				index2 = i;
			}
		}
		
		return arrayOfNames.get(index1) + ":" + max + "\n" + arrayOfNames.get(index2) + ":" + max2;
	}
}
