package ludo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ludo.Autoplay.AutoplayMode;
import ludo.ImgPath.*;
import static ludo.Player.GOAL;
import static ludo.Player.OUT_OF_BOARD;

/**
 * Represents the state of each of the elements present in a round of Ludo.
 * This state information includes:
 * <ul>
 * <li>The Board on which the game is played
 * <li>The Dice
 * <li>The set of Players
 * <li>The controller for the computer player (Autoplay)
 * <li>The Theme for the graphic representation
 * <li>The index of the current player at any given moment
 * <li>If a round of Ludo is being played
 * <li>If the current player should roll the dice or move a token
 * <li>List of indexes of the active players in the round
 * <li>List of the possible moves at any given moment
 * <li>List of players that completed the game (all tokens at the goal area)
 * <li>The results of a round
 * </ul>
 * <p>
 * @author Carla Villegas <carv@itu.dk>
 */

public class GameState {
    
    //default game settings:
    public static final Theme DEFAULT_THEME = Theme.plain;
    public static final boolean DEFAULT_BOARD = true;
    public static final boolean DEFAULT_AUTOPLAYER= false;
    public static final AutoplayMode DEFAULT_AUTOMODE= AutoplayMode.customAI;
    private static List<ImgPath.Color> DEFAULT_PLAYERS = Arrays.asList(Color.blue, Color.red, Color.yellow, Color.green);
    
    private Board board;
    private Dice dice;
    private Player[] players;
    private Autoplay computerPlayer;
    private Theme theme;
    private int currentPlayer, turn; //index of current player, count of turns since game 
    private boolean diceRoller, playing, debug; // check if click is dice roller or token selector
    private ArrayList<Integer> xPlayers, xTokens, winners; //indexes of active players
    private String gameResults;
    /** 
    * Initializes an instance of GameState using default game settings. 
    */
    public GameState(){
        this.gameResults = "";
        initVars();
        this.theme = DEFAULT_THEME;
        this.board = new Board(DEFAULT_BOARD);
        createSetOfPlayers(DEFAULT_PLAYERS, DEFAULT_AUTOPLAYER, DEFAULT_AUTOMODE);
    } 
    
    /**
    * Initializes an instance of GameState specifying theme, list of players, and type of board.
    * Uses default settings for auto/manual setting and AutoplayMode.
    * 
    * @param theme  the theme or style for the GUI
    * @param plColors  the list of colors of the players in this round
    * @param specialBoard  whether the board is regular(false) or special(true) 
    * 
    */
    public GameState(Theme theme, List<ImgPath.Color> plColors, boolean specialBoard){
        this.gameResults = "";
        initVars();
        this.theme = theme;
        this.board = new Board(specialBoard);
        createSetOfPlayers(plColors, DEFAULT_AUTOPLAYER, DEFAULT_AUTOMODE);
    } 
    
    /**
    * Initializes an instance of GameState specifying each player's auto/manual settings and AutoplayMode.
    * It also specifies theme, list of players, and type of board.
    * This constructor does not use default settings.
    * 
    * @param theme  the theme or style for the GUI
    * @param plColors  the list of colors of the players in this round
    * @param specialBoard  whether the board is regular(false) or special(true) 
    * @param auto if the player is a computer player (true) or a human player (false)
    * @param autoMode the computer algorithm used by the computer player to decide next moves
    */
    public GameState(Theme theme, List<ImgPath.Color> plColors, List<Boolean> auto, List<AutoplayMode> autoMode, boolean specialBoard){
        initVars();
        this.theme = theme;
        this.board = new Board(specialBoard);
        createSetOfPlayers(plColors, auto, autoMode);
    } 
    
    /**
    * Initializes common variables to all GameState constructors.
    */
    private void initVars(){
        this.dice = new Dice();
        this.playing = true;
        this.currentPlayer = 0;
        this.turn = 0;
        this.xPlayers=new ArrayList<>(); 
        this.players = new Player[4];
        this.diceRoller=true;
        this.xTokens=new ArrayList<>();
        this.winners=new ArrayList<>();
        this.computerPlayer= new Autoplay();
        this.gameResults = "";
        this.debug = false;
    }

    /**
    * Get the current state of the object representing a ludo board.
    * The Board class contains information about the type of board (regular or special),
    * the position of special tiles, and the board images for the GUI
    * @return the current state of the board
    * @see ludo.Board
    */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Sets the local attribute board to the value of the parameter board.
     * @param board an instance of the class Board
     * @see ludo.Board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

     /**
     * Sets the attribute special from the object board to regular (false) or special (true).
     * @param special boolean indicating if the board should be special or not
     * @see ludo.Board#setSpecial(java.lang.String) 
     */
    public void setSpecial(String special) {
        if(special.equals("special"))
            this.board.setSpecial(true);
    }

    /**
     * Get the current state of the object representing a dice
     * The Dice class contains information about the result after casting the dice,
     * the current dice holder, and dice images for the GUI
     * @return the current state of the dice
     * @see ludo.Dice
     */
    public Dice getDice() {
        return this.dice;
    }

    /**
     * Sets the local attribute dice to the value of the parameter dice
     * @param dice an instance of the class dice
     * @see ludo.Dice
     */
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    /**
     * Gets an array with objects of the class Player, representing each of the players for this round
     * @return the set of players for this round
     * @see ludo.Player
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Sets the attribute players to reference the parameter array players
     * @param players array of size 4 containing objects of the class player
     * @see ludo.Player
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }
    
    /**
     * Returns an object of class Player given its index i in the attribute players array
     * @param i integer number [0-3] representing the index of one of the 4 players
     * @return player of index i
     * @see ludo.Player
     */
    public Player getPlayer(int i) {
        return this.players[i];
    }
    
    /**
     * Returns an object of class Player given its color
     * @param color string containing the name of the player's color attribute
     * @return player of Color color
     * @see ludo.Player
     */
    public Player getPlayer(String color) {
        for (Player player: this.players)
            if(player.getColor().equalsIgnoreCase(color))
                return player;
        return null;
    }
    
    /**
     * Gets the list of indexes of active players 
     * (Players that have started a round but don't have their 4 tokens in the Goal Area yet).
     * @return the list of indexes of the active players in the round
     */
    public ArrayList<Integer> getXPlayers() {
        return this.xPlayers;
    }

    /**
     * Adds a player index to the list of active players
     * After adding a player to the list, sorts the list in ascending order to keep the order of each player's turn
     * @param playerIndex integer number [0-3] representing the index of one of the 4 players to be added
     */
    public void addXPlayers(int playerIndex) {
        this.xPlayers.add(playerIndex);
        Collections.sort(this.xPlayers);
    }
    
    /**
     * Removes a player index from the list of active players
     * @param playerIndex integer number [0-3] representing the index of one of the 4 players to be removed
     */
    public void removeXPlayers(int playerIndex) {
        this.xPlayers.remove(this.xPlayers.indexOf(playerIndex));
    }
    
    /**
     * Gets the list of indexes of the active tokens of the current player 
     * (Tokens that can be moved at any given moment).
     * @return list of possible moves
     */
    public ArrayList<Integer> getXTokens() {
        return this.xTokens;
    }

    /**
     * Gets the list of players that completed the game (have all their tokens at the goal area)
     * @return list of winners
     */
    public ArrayList<Integer> getWinners() {
        return this.winners;
    }

    /**
     * Adds a player to the list of winners
     * @param playerIndex integer number [0-3] representing the index of one of the 4 players
     */
    public void addWinners(int playerIndex) {
        this.winners.add(playerIndex);
    }

     /**
     * Gets the current theme/style used for the GUI
     * @return the theme/style of the game graphics
     */
    public Theme getTheme() {
        return this.theme;
    }

    /**
     * Sets the game graphics style to the given theme
     * @param theme graphic style for the GUI
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    
    /**
     * Sets the game graphics style to the given theme name
     * @param theme string with the name of the theme/graphic style for the GUI
     */
    public void setTheme(String theme) {
        for(Theme t: Theme.values())
            if(theme.equalsIgnoreCase(t.name()))
                this.theme = t;
    }

    /**
     * Indicates whether it is time for the current player to roll the dice (true) or to select and move a token (false).
     * @return true if the current player should roll the dice, false if the player should move a token
     */
    public boolean getDiceRoller() {
        return this.diceRoller;
    }
    
    /**
     * Indicates whether a round of ludo is being played (true) or not (false).
     * @return true if a game is being played, false if the game is over
     */
    public boolean getPlaying() {
        return this.playing;
    }
    
    /**
     * Sets the value of the attribute playing to the given parameter playing
     * @param playing boolean value indicating if the round of ludo should be started (true) or terminated(false)
     */
    public void setPlaying(boolean playing) {
        this.playing=playing;
    }
    
        /**
     * Indicates whether a test is being performed.
     * @return true if the game is in test mode
     */
    public boolean getDebug() {
        return this.debug;
    }
    
    /**
     * Sets the value of the attribute debug to the given parameter debug
     * @param debug boolean value indicating if the game is in debug mode (a test is being performed)
     */
    public void setDebug(boolean debug) {
        this.debug=debug;
    }

    /**
     * Gets the index of the current player in the array players
     * @return integer number [0-3] representing the index of current player
     */
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }
    
     /**
     * Sets the current player to a given index in the range 0-3
     */
    public void setCurrentPlayer(int index){
        this.currentPlayer=index%4;
    }
    
    /**
     * Gets the number of played turns at any given moment
     * A turn starts when a player gets the dice to roll and ends when the dice is passed to the next player
     * @return total number of played turns
     */
    public int getTurnCount(){
        return this.turn;
    }
    
    /**
     * Gets a string with the results of a round of ludo
     * This is a list of winners by first to last to have finished the game
     * @return string containing the results of the game
     */
    public String getGameResults(){
    	return this.gameResults;
    }

    /**
     * Initializes the attributes of each of the active players for this round.
     * @param colors list of colors of the active players for this round
     * @param auto list of boolean values for each player. the boolean value for each player should be 
     *             true if the player is computer operated, and false if the player is human
     * @param autoMode list of AutoplayMode values for each player. This mode will be used if/when auto is set to true
     */
    private void createSetOfPlayers(List<ImgPath.Color> colors, List<Boolean> auto, List<AutoplayMode> autoMode) {
        int i = 0;
        for (ImgPath.Color c : ImgPath.Color.values()) {
            this.players[i] = new Player(c, auto.get(i), autoMode.get(i));
            this.players[i].setPIndex(i);
            //System.out.println("Player "+c.name()+" created.");
            for(int j=0; j<4; j++){
                this.players[i].setXY(j);
            }
            if (colors.contains(c)) {
                this.players[i].setActive(true);
                this.xPlayers.add(i);
            }
            i++;
        }
    }
   
    /**
     * Initializes the attributes of each of the active players for this round.
     * In this case the auto and AutoplayMode settings are the same for all players
     * @param colors list of colors of the active players for this round
     * @param auto true if the players are computer operated, and false if the players are human
     * @param autoMode AutoplayMode value for all players. This mode will be used if/when auto is set to true
     */
    private void createSetOfPlayers(List<ImgPath.Color> colors, boolean auto, AutoplayMode autoMode) {
        int i = 0;
        for (ImgPath.Color c : ImgPath.Color.values()) {
            this.players[i] = new Player(c, auto, autoMode);
            this.players[i].setPIndex(i);
            //System.out.println("Player "+c.name()+" created.");
            for(int j=0; j<4; j++){
                this.players[i].setXY(j);
            }
            if (colors.contains(c)) {
                this.players[i].setActive(true);
                this.xPlayers.add(i);
            }
            i++;
        }
    }
    
    /**
     * Sends all the tokens of each active player to its starting position
     * and sets attributes turn, currentPlater and diceRoller to its original values.
     */
    public void restart(){
        for(Player p: players)
            if(p.getActive())
                p.reset();
        turn = 0;
        currentPlayer = 0;
        diceRoller=true;
    }
    
    /**
     * Initializes a player that was previously considered not-active in the game
     * @param color string containing the name of the player's color attribute
     */
    public void addPlayer(String color){
        if(!this.getPlayer(color).getActive()){
            this.getPlayer(color).reset();
            this.getPlayer(color).setActive(true);
            addXPlayers(this.getPlayer(color).getPIndex());
            turn = this.xPlayers.indexOf(currentPlayer);}
    }
    
    /**
     * Removes an active player from the game given its color
     * @param color string containing the name of the player's color attribute
     */
    public void removePlayer(String color){
        this.getPlayer(color).setActive(false);
        removeXPlayers(this.getPlayer(color).getPIndex());
        turn = this.xPlayers.indexOf(currentPlayer);
    }    

     /**
     * Compares the position of a given token with other players' tokens and if equal, sends the opponent's token to its home area
     * @param pIndex integer number [0-3] representing the index of the given player
     * @param tIndex integer number [0-3] representing the index of the given token
     */
    private void checkOtherTokens(int pIndex, int tIndex) {
        int tokenPosition = this.players[pIndex].getToken(tIndex).getPosition();
        for (int i = 0; i < this.xPlayers.size(); i++) {
            if (!this.players[pIndex].getColor().equals(players[xPlayers.get(i)].getColor())) {
                for (int j = 0; j < 4; j++) {
                    if (players[xPlayers.get(i)].getToken(j).getPosition() == tokenPosition && players[xPlayers.get(i)].getToken(j).getPosition() != OUT_OF_BOARD && !players[xPlayers.get(i)].getToken(j).getSafe()) {
                        players[xPlayers.get(i)].outOfBoard(j);
                    }
                }
            }
        }
    }

    /**
     * The current player rolls the dice and updates the list of active tokens
     */
    public void rollAndCheckActiveTokens() {
        this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
        this.dice.rollDice(this.currentPlayer);
        //System.out.printf("%s player rolls the dice: %d\n", this.players[currentPlayer].getColor(), this.dice.getResult());
        this.xTokens.clear();

        if (this.dice.getIsSix()) {
            this.players[currentPlayer].setTurn(true);// flag for throwing the dice again if a token is moved
            for (Token token : this.players[currentPlayer].getTokens()) {
                if (!(token.getFinalTrack() && token.getPosition() == GOAL)) {
                    this.xTokens.add(token.getIndex());
                }
            }
        } else {
            this.players[currentPlayer].setTurn(false);
            for (int index : this.players[currentPlayer].getTokensOut()) {
                this.xTokens.add(index);
            }
        }
    }
    
    /**
     * If there are no possible moves, the current player passes the dice to the next player 
     */
    public void checkMoveOrPass(){
        if (this.xTokens.size() > 0) {
            this.diceRoller = false;} 
        else { //if no tokens to move, pass and let player roll dice
            this.turn++;
            //System.out.println("next turn player " + this.players[currentPlayer].getColor());
        }
        this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
    }
    
    /**
     * Moves the selected token to a new position, checks for other tokens and special tiles (in case of special board), 
     * checks if the player has finished the game in this turn, and if so, checks if the game is over
     * @param tokenIndex integer number [0-3] representing the index of the selected token
     */
    public void selectAndMove(int tokenIndex) {
        Token thisToken = this.players[currentPlayer].getTokens()[tokenIndex];
        //System.out.println((thisToken.getFinalTrack()&&!thisToken.getOut()));
        if(!(thisToken.getFinalTrack()&&!thisToken.getOut())){
        this.players[currentPlayer].moveSelectedToken(tokenIndex,this.dice.getResult());
        if (!thisToken.getFinalTrack()) {
            this.checkOtherTokens(this.players[currentPlayer].getPIndex(), tokenIndex);
            if (this.board.getSpecial()) {
                this.players[currentPlayer].checkSpecial(tokenIndex, this.board);
            }
        }
        if (this.players[currentPlayer].getGoal() == 4) {
            this.addWinners(this.players[currentPlayer].getPIndex());
            this.removeXPlayers(this.players[currentPlayer].getPIndex());
            if (this.getXPlayers().isEmpty()) {
                this.playing = false;
                this.gameResults = "\nResults:\n\n";
                //System.out.println("-----GAME OVER!-----"+gameResults);
                for (int i = 0; i < this.getWinners().size(); i++) {
                    //System.out.printf("%d place - %s player\n", i + 1, this.getPlayers()[this.getWinners().get(i)].getColor());
                    this.gameResults += (i + 1)+" place - "+this.getPlayers()[this.getWinners().get(i)].getColor()+" player\n";
                }
            }
        }
        //System.out.println("Player turn:" + this.players[currentPlayer].getTurn());
        if (!this.players[currentPlayer].getTurn()) {
            this.turn++;
            //System.out.println("next turn player " + this.players[currentPlayer].getColor());
        }
        this.diceRoller = true;
        if (playing)
            this.currentPlayer = this.xPlayers.get(this.turn % this.xPlayers.size());
        }
    }
     
    /**
     * Calls the method SelectAndMove with the token selected by the computer player as the argument
     * @see #selectAndMove(int)
     */
    public void autoMove() {
        selectAndMove(computerPlayer.selectToken(this));
    }
}
