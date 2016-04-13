package ludo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import static javax.swing.JOptionPane.showMessageDialog;
import static ludo.Dice.DICE_SIZE;
import static ludo.ImgPath.*;
import ludo.ImgPath.Color;

/**
 * A user interface for the ludo game.
 * This object allows the user to modify game settings through a menu bar, through select windows and through key combinations.
 * This interface also allows the user to manually control the dice casts and token movements.
 * <p>
 * This class contains the following components:
 * <ul>
 * <li>A menu bar to allow the user to modify game settings, and to restart, pause and exit the game.
 * <li>A container(canvas) to render the graphics of all the elements of the ludo game.
 * <li>A timer to trigger the computer player response.
 * <li>A timer to trigger dice animation events.
 * <li>Mouse, event and key listeners.
 * </ul> 
 * <p>
 * The GUI accepts the following key combinations:
 * <ul>
 * <li>CTRL+D : allows the user to input the dice result instead of using a random value.
 * <li>CTRL+P:  shows a Selector dialog box to modify the number and colors of the players participating in the round of ludo.
 * <li>CTRL+B:  shows a Selector dialog box to modify the type of Board (regular or special).
 * <li>CTRL+T:  shows a Selector dialog box to modify the Theme.
 * </ul>
 * <p>
 * 
 * @author Carla Villegas <carv@itu.dk>
 */

public class LudoGUI extends JPanel implements ActionListener, MouseListener, KeyListener {
    public static final int IGNORE = 10;
    public static final int TILE_SIZE = 40;
    public static final int DICE_DELAY = 40;
    public static final int AUTOPLAYER_DELAY = 1000;
    
    private static final List<String> mGame = Arrays.asList("Restart", "Pause", "Exit");
    private static final List<String> mSettings = Arrays.asList("Players", "Theme", "Board", "Dice");
    private static final List<String> mPlayers = Arrays.asList("Yellow", "Red", "Green", "Blue");
    private static final List<String> mTheme = Arrays.asList("Plain", "Solid","Fruits");
    private static final List<String> mBoard = Arrays.asList("Regular", "Special");
    private static final List<String> mPSettings = Arrays.asList("Auto", "Manual", "Off");
    
    BufferedImage highlighter;
//    BufferedImage dice[] = new BufferedImage[6];
//    BufferedImage diceAnimation[] = new BufferedImage[25];

    Timer animation = new Timer(DICE_DELAY, this);
    Timer autoplayer = new Timer(AUTOPLAYER_DELAY, this);

    JMenuBar menuBar;
    GameState thisGame;
    static JDialog frame;
    
    int tileSize = TILE_SIZE;
    int frameSize = tileSize*15;
    int diceSize = (int)(tileSize*1.5);

    /**
     * Initializes the event listeners and starts the timer for the computer-operated players.
     * @param game instance of the GameState class containing the state of each of the elements present in a round of Ludo
     */
    public LudoGUI(GameState game) {
        thisGame = game;
        autoplayer.start();
        addMenu();
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(frameSize, frameSize));
        //System.out.println(thisGame.getTheme());
        try {
            highlighter = ImageIO.read(new File(GEN_PATH+DIR+"highlight.png"));} 
        catch (IOException e) {}
    }
    
    /**
     * Gets the menu bar of the GUI.
     * @return the menu bar used in the ludo GUI
     */
    public JMenuBar getMenu(){
        return this.menuBar;
    }

    private void render(Graphics2D g2) {
        //System.out.println("---- current player is: "+thisGame.getPlayer(thisGame.getCurrentPlayer()).getColor());
        g2.drawImage(thisGame.getBoard().getImg(thisGame.getTheme()), 0, 0, frameSize, frameSize, null);
        if (thisGame.getBoard().getSpecial()) {
            g2.drawImage(thisGame.getBoard().getImgSp(thisGame.getTheme()), 0, 0, frameSize, frameSize, null);
        }
        Player[] players = thisGame.getPlayers();
        for (Player player : players) {
            if (player.getActive()) {
                //int[] coordXY;
                for (Token token : player.getTokens()) {
                    g2.drawImage(player.getImage(thisGame.getTheme()), token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                    if (!thisGame.getDiceRoller()) {
                        if (!animation.isRunning()) {
                            //if (thisGame.getPlayers()[thisGame.getDice().getHolder()].getColor().equals(player.getColor())) {
                            if (thisGame.getCurrentPlayer() == player.getPIndex()) {    
                                if (thisGame.getDice().getIsSix() & !token.getFinalTrack()) {
                                    g2.drawImage(highlighter, token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                                } else {
                                    if (player.getTokensOut().contains(token.getIndex())) {
                                        g2.drawImage(highlighter, token.getCoordinateX(), token.getCoordinateY(), tileSize, tileSize, null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            g2.drawImage(thisGame.getDice().getDiceImg(), thisGame.getDice().getCoordinates(0), thisGame.getDice().getCoordinates(1), DICE_SIZE, DICE_SIZE, null);
    }

    /**
     * Draws the images of each ludo game element in the canvas.
     * @param g Graphics object that encapsulates state information needed for the basic rendering operations that Java supports
     */
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        render(g2);
    }
    
     /**
     * Creates the menu bar and each of the sub-menus and menu items on it.
     */
    private void addMenu(){
        JMenuBar menu = new JMenuBar();
        menu.setPreferredSize(new Dimension(15*TILE_SIZE,25));
        JMenu gameMenu = createSubMenu("Game",mGame,true);
        JMenu settingsMenu = new JMenu("Settings");
        JMenu playerMenu = new JMenu("Players");
        JMenu themeMenu = createSubMenu("Theme",mTheme,true);
        JMenu boardMenu = createSubMenu("Board",mBoard,true);
        for(String player: mPlayers){
            playerMenu.add(createSubMenu(player,mPSettings,false));} 
        menu.add(gameMenu);
        menu.add(settingsMenu);
        settingsMenu.add(playerMenu);
        settingsMenu.add(themeMenu);
        settingsMenu.add(boardMenu);
        this.menuBar = menu;
    }
    
    //itemName: true for name same as label, false for name same as text
    /**
     * Creates the sub-menus of the menu bar.
     * @param label string of the label for the sub-menu
     * @param menuList list of strings of the text of each menu item for this sub-menu
     * @param itemName true if the name of the menu items should be the text of the item or false if the name of the menu items should be the label of the sub-menu
     */
    private JMenu createSubMenu(String label, List<String> menuList, boolean itemName){
        JMenu menu = new JMenu(label);
        for(String option: menuList){
            JMenuItem item = new JMenuItem(option);
            item.setName(itemName? option: label);
            item.addActionListener(this);
            menu.add(item);
        }
        return menu;
    }

    /**
     * Event handler for the mouse events.
     * If the current player is human and it is its turn to roll the dice, a click on the frame will trigger a dice roll.
     * If the player is human and it is its time to move a token, a click on a player's token will move it to its destination.
     * @param e event triggered my a mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!thisGame.getPlaying()) {
            showMessageDialog(frame, thisGame.getGameResults()+"\n", "Game Over", JOptionPane.PLAIN_MESSAGE); 
            closeGUI();
        } 
        else {
        if (!thisGame.getPlayer(thisGame.getCurrentPlayer()).getAuto()){
            if (thisGame.getDiceRoller()) {
                    thisGame.rollAndCheckActiveTokens();
                    animation.start();} 
            else {
                int[] clickXY = new int[2];
                clickXY[0] = e.getX();
                clickXY[1] = e.getY();
                int selectedToken = thisGame.getPlayer(thisGame.getCurrentPlayer()).getTokenbyCoord(clickXY);
                System.out.println("SelectedToken: "+selectedToken);
                if (thisGame.getDice().getIsSix() && selectedToken != IGNORE) {
                    thisGame.selectAndMove(selectedToken);} 
                else {
                    if (thisGame.getPlayer(thisGame.getCurrentPlayer()).getTokensOut().contains(selectedToken)) {
                        thisGame.selectAndMove(selectedToken);}}
                autoplayer.start();
            }
        }
        repaint();
        }
    }
    
    /**
     * Event handler for the keyboard events.
     * The GUI accepts the following key combinations:
     * <ul>
     * <li>CTRL+D : allows the user to input the dice result instead of using a random value.
     * <li>CTRL+P:  shows a Selector dialog box to modify the number and colors of the players participating in the round of ludo.
     * <li>CTRL+B:  shows a Selector dialog box to modify the type of Board (regular or special).
     * <li>CTRL+T:  shows a Selector dialog box to modify the Theme.
     * </ul>
     * <p>
     * @param ke keyboard event
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.isControlDown()){
            switch(ke.getKeyCode()){
                case KeyEvent.VK_D:{
                    boolean debugMode=thisGame.getDice().getDebug();
                    System.out.printf("Debug switched %s\n",debugMode? "off":"on");
                    thisGame.getDice().setDebug(!debugMode);
                    break;}
                case KeyEvent.VK_A:{
                    for(Player player: thisGame.getPlayers())
                        player.setAuto(true);
                    autoplayer.start();
                    break;}
                case KeyEvent.VK_M:{
                    for(Player player: thisGame.getPlayers())
                        player.setAuto(false);
                    break;}
                case KeyEvent.VK_T:{
                    Selector s = new Selector("theme");
                    thisGame.setTheme(s.selectedTheme());
                    break;}
                case KeyEvent.VK_P:{
                    Selector s = new Selector("player", thisGame.getTheme().name(),thisGame.getPlayer(thisGame.getCurrentPlayer()).getColor());
                        for(Color color: Color.values())
                            if(s.selectedPlayers().contains(color))
                                thisGame.addPlayer(color.name());
                            else
                                if(thisGame.getPlayer(color.name()).getActive())
                                    thisGame.removePlayer(color.name());
                    break;}
                case KeyEvent.VK_B:{
                    Selector s = new Selector("board", thisGame.getTheme().name());
                    thisGame.getBoard().setSpecial(s.selectedBoard());
                    break;}                
            }
            repaint();
        }
    }

    /**
     * Event handler for the timers and menu events.
     * If the event is triggered by a timer call method timerEvent, and if it's triggered by a menu event call menuEvent.
     * @param ae action event triggered by the timer clock or a menu item being selected by the user
     * @see #timerEvent(javax.swing.Timer) 
     * @see #menuEvent(javax.swing.JMenuItem) 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object event = ae.getSource();
        switch(event.getClass().getSimpleName()){
            case ("Timer"):{
                timerEvent((Timer)event);
                break;}
            case ("JMenuItem"):{
                menuEvent((JMenuItem)event);
                break;}}
        repaint();
    }
    
    /**
     * Identifies the timer that triggered the event and:
     * If it is the timer for the dice animation, calls the method playAnimation
     * If it is the timer that triggers the computer player, blocks the player from the menu bar so it can not be modified 
     * while playing a turn, and calls the method autoPlay.
     * @param timer
     * @see #autoPlay() 
     * @see #playAnimation() 
     */
    public void timerEvent(Timer timer) {
        if (timer == animation) {
            playAnimation();} 
        else {
            if (timer == autoplayer) {
                if(!thisGame.getDebug())
                    blockCurrentPlayerMenu();
                autoPlay();}}
    }
    
    /**
     * Blocks the current player from the menu bar so it can not be modified while playing a turn, in order to avoid null pointer exceptions during execution.
     */
    private void blockCurrentPlayerMenu(){ 
        JMenu menu = (JMenu)menuBar.getMenu(1).getPopupMenu().getComponent(0);
        for (int i=0; i<4; i++){
            if(thisGame.getPlayer(thisGame.getCurrentPlayer()).getColor().equalsIgnoreCase(menu.getItem(i).getText()))
                menu.getItem(i).setEnabled(false);
            else
                menu.getItem(i).setEnabled(true);}
    }
    
    /**
     * Updates the dice image with every tick of the timer and moves the dice position from the player's corner to the center of the board.
     */
    public void playAnimation(){
       thisGame.getDice().animateDice();
       if (thisGame.getDice().getCoordinates(0) == (TILE_SIZE * 15 - DICE_SIZE) / 2) {
           animation.stop();
           thisGame.checkMoveOrPass();
           autoplayer.start();} 
    }
    
    /**
     * Checks if it is the computer player's turn to roll the dice or to move a token, and acts accordingly.
     */
    public void autoPlay(){
        if (!thisGame.getPlaying()) {
            showMessageDialog(frame, thisGame.getGameResults()+"\n", "Game Over", JOptionPane.PLAIN_MESSAGE); 
            closeGUI();
        } 
        else {
            autoplayer.stop();
            if (thisGame.getPlayer(thisGame.getCurrentPlayer()).getAuto()) {
                if (thisGame.getDiceRoller()) {
                    thisGame.rollAndCheckActiveTokens();
                    animation.start();} 
                else {
                thisGame.autoMove();
                autoplayer.start();}
            }
        }
    }
    
    /**
     * Identifies the menu item that triggered the event and acts accordingly.
     * <p>
     * MENU Game :  
     * <ul>
     * <li>MENU ITEM : Restart -> Restarts the game
     * <li>MENU ITEM : Pause -> Pauses/Resumes the game
     * <li>MENU ITEM : Exit -> Terminates the game
     * </ul>
     * <p>
     * MENU Settings :  
     * <ul>
     * <li>SUBMENU Players : SUBMENU: Yellow/Red/Green/Blue : 
     * <ul>
     * <li>MENU ITEM : Auto -> (Adds and) sets the selected player to computer-operated mode
     * <li>MENU ITEM : Manual -> (Adds and) sets the selected player to manually operated mode
     * <li>MENU ITEM : Off -> Removes the player from the current round
     * </ul>
     * <li>SUBMENU Theme : 
     * <ul>
     * <li>MENU ITEM : Plain -> Sets the graphics to the selected theme
     * <li>MENU ITEM : Solid -> Sets the graphics to the selected theme
     * <li>MENU ITEM : Fruits -> Sets the graphics to the selected theme
     * </ul> 
     * <li>SUBMENU Board :
     * <ul>
     * <li>MENU ITEM : Regular -> Sets the board to regular (removes special tiles)
     * <li>MENU ITEM : Special -> Sets the board to special (adds special tiles)
     * </ul>   
     * </ul>
     * @param item the menu item that triggered the event
     */
    public void menuEvent(JMenuItem item){
        if(mGame.contains(item.getName()))                    
                    switch(item.getName()){
                        case "Restart":{
                            restartGUI();
                            break;}
                        case "Pause":{
                            pause(item.getText().equals("Pause"));   
                            break;}
                        case "Exit":{
                            closeGUI();
                            break;}}
                else{
                    if(mTheme.contains(item.getText())){
                        thisGame.setTheme(item.getText());}
                    else{
                        if(mBoard.contains(item.getText())){
                            thisGame.getBoard().setSpecial(item.getText());
                            System.out.println("Item set to "+item.getText());}
                        else{
                            if(mPSettings.contains(item.getText())){
                                switch(item.getText()){
                                    case "Auto":{
                                        thisGame.addPlayer(item.getName());
                                        thisGame.getPlayer(item.getName()).setAuto(true);
                                        break;}
                                    case "Manual":{
                                        thisGame.addPlayer(item.getName());
                                        thisGame.getPlayer(item.getName()).setAuto(false);
                                        break;}
                                    case "Off":{
                                        thisGame.removePlayer(item.getName());
                                        break;}}}}}}
    }
    
    /**
     * Sends all the tokens of the active players to their home area.
     */
    public void restartGUI(){
        animation.stop();
        autoplayer.restart();
        thisGame.restart();
    }
    
    /**
     * Stops/restarts the timers of the GUI.
     * @param playing true to pause the game, false to resume
     */
    public void pause(boolean playing){
        if(playing){
            animation.stop();
            autoplayer.stop();
            menuBar.getMenu(0).getItem(1).setText("Resume");}
        else{
            animation.start();
            autoplayer.start();
            menuBar.getMenu(0).getItem(1).setText("Pause");}  
    }
            
    /**
     * Stops the timers and disposes of the GUI.
     */
    public void closeGUI(){
        animation.stop();
        autoplayer.stop();
        //setFocusable(false);
        try{frame.dispose();}
        catch(NullPointerException e){}
    }
    
    /**
     * Creates and initializes the container of the ludoGUI and shows it on screen.
     * @param game instance of the GameState class containing the state of each of the elements present in a round of Ludo
     */
    public static void drawGUI(GameState game) {
        frame = new JDialog();
        frame.setTitle("LUDO");
        frame.setModal(true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        game.setPlaying(true);
        LudoGUI ludoPanel=new LudoGUI(game);  
        frame.setJMenuBar(ludoPanel.getMenu());
        frame.add(ludoPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); //center frame on screen
        frame.setVisible(true);
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }  
}
