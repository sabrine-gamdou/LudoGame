package ludo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import ludo.ImgPath.Color;
import ludo.ImgPath.Theme;

/**
* This program is an implementation of the popular board game Ludo.
* 
* @author Carla Villegas <carv@itu.dk>
*/

public class Main {
    
    private static GameState game;
    private static Selector selectWindow;
    
 /**
 * Main method of the application.
 * Initializes game and selectWindow. 
 Sets up the attributes of game according to the user‘s input (through selectWindow). 
 Calls the method drawGUI from the LudoGUI class.
 */
    public static void main(String[] args) {     
    	
        new ImgPath();

    	selectWindow= new Selector("theme");
    	Theme theme = selectWindow.selectedTheme();
        
    	selectWindow= new Selector("player", theme.name());
    	List<Color> plColors = selectWindow.selectedPlayers();
        
    	selectWindow= new Selector("board", theme.name());
    	boolean special = selectWindow.selectedBoard();

    	game = new GameState(theme, plColors, special); 
//        ImgPath.Theme theme = ImgPath.Theme.plain;
//        List<ImgPath.Color> plColors = Arrays.asList(Color.yellow, Color.red, Color.green, Color.blue);
//        List<Boolean> auto = Arrays.asList(true, true, true, true);
//        List<Autoplay.AutoplayMode> autoMode = Arrays.asList(Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI);
//        boolean special = true;
//        
//        game = new GameState(theme, plColors, auto, autoMode, special);
        LudoGUI.drawGUI(game);
        System.exit(0); // make sure all frames are disposed
    }
}
