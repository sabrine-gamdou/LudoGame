
package ludo;

import static ludo.LudoGUI.TILE_SIZE;
import static ludo.Player.GOAL;
import static ludo.Player.OUT_OF_BOARD;

/**
 * Represents one of the 4 tokens of a player.
 * This object contains the following information:
 * <ul>
 * <li>The token's position in the board
 * <li>The token's coordinates in the graphic frame
 * <li>The token's index in the array of tokens from the Player object
 * <li>Whether the token is out of its home area
 * <li>Whether the token is in its final track
 * <li>Whether the token is safe and can not be sent home by other tokens
 * </ul>
 * <p>
 * 
 * @author Carla Villegas <carv@itu.dk>
 */
public class Token {
 
    private int index, position, coordinateX, coordinateY;
    private boolean out, finalTrack, safe;
   
    /**
     * Initializes an instance of Token given its position in the array of tokens from the Player object
     * @param i the index of the token in the tokens array
     * @see ludo.Player#tokens
     */
    public Token(int i) {
    this.index = i;
    this.position = OUT_OF_BOARD;
    this.out = false;
    this.finalTrack = false;
    this.coordinateX = TILE_SIZE*15;
    this.coordinateY = TILE_SIZE*15;
    }    
    
    /**
     * Gets the token's index in the array of tokens from the Player object.
     * @return int number [0-3] representing the index of the token in the tokens array
     * @see ludo.Player#tokens
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Gets the token's position in the board.
     * @return int number [0-51] or value of constant OUT_OF_BOARD, representing the position of the token in the board
     * @see ludo.Player#OUT_OF_BOARD
     */
    public int getPosition() {
        return this.position;
    }
    
    /**
     * Sets the token's position to a given position in the board.
     * @param pos int number [0-51] or value of constant OUT_OF_BOARD, representing the position of the token in the board
     * @see ludo.Player#OUT_OF_BOARD
     */
    public void setPosition(int pos) {
        this.position = pos;
    }
    
    /**
     * Gets the coordinate x of the token in the graphic frame.
     * @return coordinate x in pixels
     */
    public int getCoordinateX() {
        return this.coordinateX;
    }

     /**
     * Gets the coordinate y of the token in the graphic frame.
     * @return coordinate y in pixels
     */
    public int getCoordinateY() {
        return this.coordinateY;
    }
    
    /**
     * Sets the coordinate x of the token to a given number
     * @param x new value of the coordinate x in pixels
     */
    public void setCoordinateX(int x) {
        this.coordinateX = x;
    }
    
     /**
     * Sets the coordinate y of the token to a given number
     * @param y new value of the coordinate y in pixels
     */
    public void setCoordinateY(int y) {
        this.coordinateY = y;
    }
    
    /**
     * Indicates whether the token is safe and can not be sent home by other tokens.
     * @return true if the token is safe, false otherwise
     */
    public boolean getSafe() {
        return this.safe;
    }

    /**
     * Sets the token to safe/not safe mode.
     * @param safe true if the token should be set to safe, false otherwise
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }
    
    /**
     * Indicates whether the token is out of its home area.
     * @return true if the token is out, false otherwise
     */
    public boolean getOut() {
        return this.out;
    }

    /**
     * Sets the token to in/out of its home area.
     * @param out true if the token should be set out of the home area, false otherwise
     */
    public void setOut(boolean out) {
        this.out = out;
    }
    
    /**
     * Indicates whether the token is in its final track.
     * @return true if the token is in its final track, false otherwise
     */
    public boolean getFinalTrack() {
        return this.finalTrack;
    }

    /**
     * Sets the token to in/out of its final track.
     * @param ft true if the token should be set in the final track, false otherwise
     */
    public void setFinalTrack(boolean ft) {
        this.finalTrack = ft;
    }
    
    /**
     * Sends a given token to its home area.
     * Sets token position to constant OUT_OF_BOARD, and updates out and final track to false.
     */ 
    public void sendHome(){
        this.position = OUT_OF_BOARD;
        this.out = false;
        this.finalTrack = false;
    }
    
    /**
     * Updates a token's position according to a given dice result.
     * @param diceResult int number [1-6] representing the result after casting the dice
     */ 
    public void moveToken(int diceResult) {
        if (!this.finalTrack){
        this.position = (this.position + diceResult) % 52;
        }
        else{
            int x = this.position + diceResult;
            this.position = x>GOAL? GOAL-(x-GOAL):x;
            //this.position = Math.abs((int) ((x % 10) / 5) * 5 - x % 5);
        }
    }
}