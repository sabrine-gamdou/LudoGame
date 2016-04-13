package ludo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import static ludo.Board.GLOBE_TILES;
import static ludo.Board.STAR_TILES;

/**
 * Generator of a QLearning text file according to a given q-learning rule.
 * DISCLAIMER: This is a modification of the class ActionQLudo.java provided by Kasper Støy.
 * The original class is currently available in the SPWS-Spring 2014 site on LearnIT.
 * 
 * @author Carla Villegas <carv@itu.dk>
 */
public class QLearning {
        static final double ALPHA = 0.001;
        static final double GAMMA = 0.75;
        static final int ITERATIONS = 100000000;
        static final int BOARDLENGTH = 57;
        static final int LASTSQUARE = BOARDLENGTH - 1;

        static double QTable[][] = new double[BOARDLENGTH][6];
        static File inputFile = new File("qtable.txt");

    /**
     * Gets a 2 dimensional array of double values representing the rewards for a move to each of the board tiles given a dice result.
     * @return 2-D array of double containing qLearning rewards
     */
    public double[][] getQTable() { 
	    return QTable;
    }
        
    /**
     * Implements going backwards if the player dice roll would bring her past the last square
     * @param i is the index of the board that potentially may be higher than the board is long
     **/
    private int getIndex( int i ) {
	if (i<BOARDLENGTH) { 
	    return(i);
	} else {
	    return(LASTSQUARE-(i-LASTSQUARE));
	}
    } 

    /**
     * Prints the QTable 
     **/
    void printQTable() {
	for(int j=0;j<6;j++) {
	    for(int i=0;i<BOARDLENGTH;i++) {
		System.out.print( String.format( "%.1f", QTable[i][j] ) + " "); //prints with 3 decimals
	    }
	    System.out.println("");
	}
	System.out.println("");
    }
    
    /**
     * Find the maximum Q-Value for the possible actions in the current state
     * @param i index into q-table in range from 0 to length of board - 1
     * @return The maximum Q-value for index i 
     **/
    double QForMaxAction( int i ) {
	double max=0;
	for(int j=0;j<6;j++) {
	    if (QTable[i][j]>max) max = QTable[i][j];
	}
	return(max);
    }

    /**
     * This method updates the reward value of each tile with each iteration (simulation of a game).
     */
    public void learn() {
	int d;
	double r;
	int i=0; // token position (index in QTable)

	for(int cnt=0; cnt<ITERATIONS; cnt++) {
	    d = 1 + (int)(Math.random()*6); //dice roll
	    
	    //reward function modified to fit a board with special tiles
	    if ( i+d == LASTSQUARE )
		r = 1; // 1 point given if token lands on last square
            else{
                if (GLOBE_TILES.contains(i+d)||STAR_TILES.contains(i+d))
                    r=0.1;
                else
                    if (i+d==50)
                        r=-0.5;
                    else
                        r=0;
            }
            
            //q-learning rule
	    QTable[i][d-1] = (1-ALPHA)*QTable[i][d-1] + ALPHA*(r + GAMMA * QForMaxAction(getIndex(i+d))); 
	    
	    i = getIndex(i+d); // move token (backwards if it hits the end
	    if(i==LASTSQUARE) i=0; // if land on the last square - return to start = new game
	    //if (cnt%100000==0) printQTable(); // once in a while print QTable
	}
        printQTable();
        //writes the table into a text file
        try {writeQTable();} 
        catch (FileNotFoundException ex) {}
    }
    
    /**
     * Stores each of the values of the qTable into a text file.
     * Makes a new line for each of the dice results [1-6]
     * Each line will contain the value of the constant BOARDLENGTH number of double values, they represent each of the tiles of the ludo board.
     * @throws FileNotFoundException
     * @see #BOARDLENGTH
     */
    public static void writeQTable() throws FileNotFoundException{
        PrintWriter out = new PrintWriter(inputFile);
        for(int j=0;j<6;j++) {
	    for(int i=0;i<BOARDLENGTH;i++) {
                out.print(QTable[i][j]+ " ");
	    }
	    out.println("");
	}
        out.close();
    }
    
    /**
     * Reads and returns a double value from a qTable file given the row and column where the value is located.
     * @param row int number [1-6] representing the dice result
     * @param column int number [0-56] representing the position of the tile in the board
     * @return the q value for the given state (dice result and new position)
     * @throws FileNotFoundException
     */
    public static double readQTable(int row, int column) throws FileNotFoundException{
        double qValue;
        Scanner inData = new Scanner(inputFile);
        String qTableRow="";
        String[] qTableColumn;
        for(int j=0;j<row+1;j++) {
            qTableRow=inData.nextLine();
	}
        qTableColumn = qTableRow.split(" ");
	qValue=Double.parseDouble(qTableColumn[column]);
        inData.close();
        return qValue;
    }
}