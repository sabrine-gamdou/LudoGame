/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import ludo.Autoplay;
import ludo.Board;
import ludo.Dice;
import ludo.GameState;
import ludo.ImgPath;
import ludo.ImgPath.Color;
import ludo.LudoGUI;
import ludo.Player;
import ludo.QLearning;
import ludo.Token;
import static ludo.Player.OUT_OF_BOARD;

/**
 * Set of tests for each of the classes representing the elements of a ludo game.
 * 
 * @author Carla Villegas <carv@itu.dk>
 */
public class Test {

    /**
     * Prints a message indicating whether the token tests were successful.
     * <p>
     * Tests performed on 4 different tokens:
     * <ul>
     * <li>Send the tokens to their home area
     * <li>Move them to a new position
     * </ul>
     * @see #testSendHome(ludo.Token) 
     * @see #testMoveToken(ludo.Token) 
     */
    public void testToken(){
         Token[] tokens = new Token[4];
         for(int i=0; i<4; i++){
             tokens[i]=new Token(i);
             if(!(testSendHome(tokens[i])&&testMoveToken(tokens[i]))){
                 System.out.println("\n----- TOKEN TEST: FAILED -----");
                 System.exit(0);}
         }
         System.out.println("\n----- TOKEN TEST: PASSED -----");
     }
     
     /**
     * Sets the token position to a predefined position, then calls the method sendHome on the token and verifies if the new position is the expected value OUT_OF_BOARD.
     * Repeats the test 10 times while it is successful or stops and returns false if the result is different than the expected value.
     * @param token the token to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testSendHome(Token token){
         boolean test=true;
         int i=0;
         System.out.println("-----Test send Home-----");
         do{
            token.setPosition(token.getIndex()*10+i);
            System.out.printf("Token Position: %d",token.getPosition());
            token.sendHome();
            System.out.printf("  Token Position after sendHome: %d\n", token.getPosition()) ;
            test = test && token.getPosition()==OUT_OF_BOARD;
            i++;
         } while (test&&i<10);
         return test;
     }
     
       /**
        * Moves the token from a predefined position to a new one and compares if the new position is equal to the expected result.
        * The test considers 3 possible scenarios: 
        *   move when token is in the board (position range 0-50)     | initial position: 4, 14, 24, 34, dice result: 2, 3, 4, 5 expected final position:6, 17, 28, 39
        *   move when token is in final track                         | initial position: 0, 1, 2, 3 dice result: 4, 3, 2, 1 expected final position: 4, 4, 4, 4
        *   move when token is in final track and has to bounce back  | initial position: 1, 2, 3, 4 dice result: 6, 5, 4, 3 expected final position: 3, 3, 3, 3,
        * @param token the token to be tested     
        * @return true if test passed, false otherwise
        */
     private boolean testMoveToken(Token token){         
         boolean test=true;
         System.out.println("-----Test move -----");
         // first case
         token.setPosition(token.getIndex()*10+4);
         System.out.print("Move when token is in the board (position range 0-50)    ");
         System.out.printf("Initial Position: %d",token.getPosition());
         token.moveToken(token.getIndex()+2);
         System.out.printf("  dice: %d  ", token.getIndex()+2);
         test = test && token.getPosition()==6+11*token.getIndex();
         System.out.printf("Final Position: %d\n", token.getPosition()) ;
         
         // second case
         token.setFinalTrack(true);
         token.setPosition(token.getIndex());
         System.out.print("Move when token is in final track    ");
         System.out.printf("Initial Position: %d",token.getPosition());
         token.moveToken(4-token.getIndex());
         System.out.printf("  dice: %d  ", 4-token.getIndex());
         test = test && token.getPosition()==4;
         System.out.printf("Final Position: %d\n", token.getPosition()) ;
         
         // third case
         token.setPosition(token.getIndex()+1);
         System.out.print("Move when token is in final track and has to bounce back    ");
         System.out.printf("Initial Position: %d",token.getPosition());
         token.moveToken(6-token.getIndex());
         System.out.printf("  dice: %d  ", 6-token.getIndex());
         test = test && token.getPosition()==3;
         System.out.printf("Final Position: %d\n", token.getPosition()) ;
         
         return test;
     }

    /**
     * Prints a message indicating whether the player tests were successful.
     * <p>
     * Tests performed on 4 different players:
     * <ul>
     * <li>Send each of the player's tokens to its start position in the board
     * <li>Move each of the player's tokens to a new position
     * <li>Check if the player's tokens are standing on a special tile (and act accordingly)
     * <li>Make a player's token start its own final track
     * <li>Move a player's token to the goal position
     * </ul>
     * @see #testStart(ludo.Player) 
     * @see #testMoveSelectedToken(ludo.Player) 
     * @see #testCheckSpecial(ludo.Player) 
     * @see #testStartFinalTrack(ludo.Player) 
     * @see #testCheckGoal(ludo.Player) 
     */
    public void testPlayer(){
         Player[] players = new Player[4];
         int i=0;
         boolean test=true,test1,test2,test3,test4,test5;
         String a ="passed";
         String b ="failed";
            
         //create players of the 4 different colors for testing
         for(Color color: Color.values()){
             players[i]=new Player(color);
             players[i].setPIndex(i);
             i++;
         }
         for(Player player: players){
            String color = player.getColor(); 
            test1=testStart(player);
            System.out.printf("\nPlayer %s Start test : %s",color, test1? a:b);
            test2=testMoveSelectedToken(player);
            System.out.printf("\nPlayer %s MoveSelectedToken test : %s",color, test2? a:b);
            test3=testCheckSpecial(player);
            System.out.printf("\nPlayer %s CheckSpecial test : %s",color, test3? a:b);
            test4=testStartFinalTrack(player);            
            System.out.printf("\nPlayer %s StartFinalTrack test : %s",color, test4? a:b);
            test5=testCheckGoal(player);
            System.out.printf("\nPlayer %s CheckGoal test : %s\n",color, test5? a:b); 
            test = test&&test1&&test2&&test3&&test4&&test5;
         }
         System.out.printf("\n----- PLAYER TEST: %s -----\n\n", test? a.toUpperCase():b.toUpperCase());
     }
     
    /**
     * Start each one of the player's tokens and check the out status, their new position (13*playerIndex is the start position of each player)
     * and finally check that the size of the array tokensOut is 4
     * @param player the player to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testStart(Player player){
         boolean test=true;
         List<Integer> expectedValues = Arrays.asList(0, 13, 26, 39);
         for (int i=0; i<4; i++){
            player.start(i);
            test=test&&player.getToken(i).getOut()&&player.getToken(i).getPosition()==expectedValues.get(player.getPIndex());}
         test = test&&player.getTokensOut().size()==4;
         return test;
     }
     
     /**
     * Move the player's tokens to a new position and check if the new positions are equal to the expected values
     * Consider these test cases: 
     * move the 4 tokens from their starting positions +2, +5(hit a STAR), +8(hit a GLOBE), +11(hit a STAR)
     * @param player the player to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testMoveSelectedToken(Player player){
         List<List<Integer>> expectedValues = Arrays.asList(Arrays.asList(2, 5, 8, 11),
                                                            Arrays.asList(15, 18, 21, 24),
                                                            Arrays.asList(28, 31, 34, 37),
                                                            Arrays.asList(41, 44, 47, 50));
         boolean test=true;
         for (int i=0; i<4; i++){
            player.moveSelectedToken(i, 3*i+2);
            test=test&&player.getToken(i).getPosition()==expectedValues.get(player.getPIndex()).get(i);
         }
         return test;
     }
     
     /**
     * Check if the player's tokens are standing on a special tile.
     * Consider this test scenario: 
     * 3 of the 4 player tokens are standing in special tiles on the board, token 0 is in a regular tile, tokens 1 and 3 are in star tiles and token 2 is in a globe tile
     * After method checkSpecial is run, check if the tokens in STAR tiles are moved to the tile with the next star 
     * and if the token in the GLOBE tile keeps its turn (gets a chance to throw dice again)
     * @param player the player to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testCheckSpecial(Player player){
         boolean test=true;
         List<List<Integer>> expectedValues = Arrays.asList(Arrays.asList(11, 18),
                                                            Arrays.asList(24, 31),
                                                            Arrays.asList(37, 44),
                                                            Arrays.asList(50, 5));
         Board board = new Board(true);
         for (int i=0; i<4; i++){
            player.checkSpecial(i, board);
         }
         test=test&&player.getToken(1).getPosition()==expectedValues.get(player.getPIndex()).get(0);
         test=test&&player.getToken(3).getPosition()==expectedValues.get(player.getPIndex()).get(1);
         test=test&&player.getTurn();
         return test;
     }
     
     /**
     * Move a player's token past the last board tile so that it starts its final track, 
     * check if the token's final track attribute is true and if the new position corresponds to a final track position [0-5].
     * @param player the player to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testStartFinalTrack(Player player){
         boolean test;
         player.moveSelectedToken(3, 35);
         test=player.getToken(3).getPosition()==2&&player.getToken(3).getFinalTrack();
         return test;
     }
     
     /**
     * Move a player's token to its goal area and check if the Goal attribute of player incremented in 1.
     * @param player the player to be tested     
     * @return true if test passed, false otherwise
     */
     private boolean testCheckGoal(Player player){
         boolean test;
         int goalTokens = player.getGoal();
         System.out.printf("\n--Tokens in goal before move: %d\n", player.getGoal());
         player.moveSelectedToken(3, 3);
         System.out.printf("--Tokens in goal after move: %d", player.getGoal());
         test=player.getGoal()-goalTokens==1;
         return test;
     }

    /**
     * Prints a message indicating whether the board tests were successful.
     * <p>
     * Tests performed:
     * <ul>
     * <li>Board constructor for a regular board (no special tiles).
     * <li>Setting the board to regular/special trying different arguments:
     * <ul>
     * <li>setSpecial with argument: boolean true -> expected result special attribute: true
     * <li>setSpecial with argument: String "any string" -> expected result special attribute: false
     * <li>setSpecial with argument: String "SpEcIaL" -> expected result special attribute: true
     * </ul>
     */
    public void testBoard(){
         Board board = new Board(false);
         boolean test = false;
         System.out.printf("Board created as regular type: boolean special value(%b)",board.getSpecial());
         board.setSpecial(true);
         System.out.printf("\nBoard after special value is changed with parameter boolean \"true\": boolean special value(%b)",board.getSpecial());
         test = board.getSpecial();
         board.setSpecial("any string");
         System.out.printf("\nBoard after special value is changed with parameter String \"any string\" special value(%b)",board.getSpecial());
         test=test&&!board.getSpecial();
         board.setSpecial("SpEcIaL");
         System.out.printf("\nBoard after special value is changed with parameter String \"SpEcIaL\" special value(%b)",board.getSpecial());
         test=test&&board.getSpecial();
         System.out.printf("\n\n----- BOARD TEST: %s -----\n\n", test? "PASSED":"FAILED\n");
     }

    /**
     * Test to check how random the dice cast results are.
     * This test simulates 1000 games of ludo between 4 players with 700 dice casts each.
     * The test considers the rule that if a player casts a 6, the player keeps its turn and can cast the dice again.
     * The result shows in percentage how many times each player got each result[1-6] and how many turns each player got in each game.
     */
    public void testDice(){
         Dice dice = new Dice();
         int iterations = 700;
         int turn = 0;
         int totalSimulations=1000;
         float[][] accStatistics = new float[4][6];
         
         for (int sim = 0; sim < totalSimulations; sim++) {
            float[][] statistics = new float[4][6];
            for (int j = 0; j < iterations; j++) {
                dice.roll();
                statistics[turn % 4][dice.getResult() - 1]++;
                if (!(dice.getResult() == 6)) {
                    turn++;
                }
            }
            if(sim%100==0)
                System.out.println("----- DICE TEST -----");
            for (int i = 0; i < statistics.length; i++) {
                float totalDiceCasts = 0;
                for (int j = 0; j < statistics[i].length; j++) {
                    totalDiceCasts += statistics[i][j];
                }
                if(sim%100==0){
                for (int j = 0; j < statistics[i].length; j++) {
                    System.out.printf("%d: %.2f%% ", j+1, statistics[i][j] / totalDiceCasts * 100);
                }
                System.out.printf("\u0009Total dice casts: %.0f  Total dice casts %%: %.2f%%\n", totalDiceCasts, totalDiceCasts / iterations * 100);
                }
            }
            for (int i = 0; i < statistics.length; i++)
                for (int j = 0; j < statistics[i].length; j++)
                    accStatistics[i][j] += statistics[i][j];
        }
         System.out.printf("\nACCUMULATED RESULTS : %d SIMULATIONS\n(Player # -> Dice Result : %% occurence)\n",totalSimulations);
         for (int i = 0; i < accStatistics.length; i++) {
             System.out.printf("Player %d -> ", i);
                float accDiceCasts = 0;
                for (int j = 0; j < accStatistics[i].length; j++) {
                    accDiceCasts += accStatistics[i][j];
                }
                for (int j = 0; j < accStatistics[i].length; j++) {
                    System.out.printf("%d: %.2f%% ", j+1, accStatistics[i][j] / accDiceCasts * 100);
                }
                System.out.printf("\u0009Total dice casts: %.0f  Total dice casts %%: %.2f%%\n", accDiceCasts, accDiceCasts/iterations/totalSimulations * 100);
         }
    }
    
     /**
     * Tests the methods writeQTable and readQTable from the QLearning class.
     * <p>
     * This method verifies:
     * <ul>
     * <li>If the values of the QTable are stored correctly in the QTable text file,
     * <li>If the QTable reader gets the correct value given its position on the table (row,column) 
     * <li>If the QTable reader parses the double values correctly (considering that these are stored as strings).
     * </ul>
     */
    public void testQLearning() {        
         if (testReadQTable()) {
             System.out.println("\n----- QLEARNING TEST: PASSED -----");;
         }
         else
             System.out.println("\n----- QLEARNING TEST: FAILED -----");
     }
    
    /**
     * Generates a QTable and compares the 2 dimensional array values to the values read from the generated text file.
     * @return true if the test is successful, false otherwise
     */
    public boolean testReadQTable(){
         QLearning testQTable = new QLearning();
         testQTable.learn();
         double readValueQ=0;
         for(int i=0; i<testQTable.getQTable().length; i++)
             for(int j=0; j<testQTable.getQTable()[i].length; j++){
                 try{readValueQ = QLearning.readQTable(j, i);}
                 catch (FileNotFoundException ex) {}
                 if(readValueQ!= testQTable.getQTable()[i][j])
                     return false;
             }
             return true; 
     }

    /**
     * Tests the game in Autoplayer mode in different scenarios and shows resulting statistics for each scenario.
     * <p>
     * Test scenarios:
     * <ul>
     * <li> 4 players in auto mode with customAI controller
     * <li> 4 players in auto mode with qLearning controller
     * <li> 4 players in auto mode with random controller
     * <li> 3 players each with a different controller
     * </ul>
     * Considerations: the timer delay for these tests is set to 2ms for the player timer and 1ms for the dice animation
     * The dice animator method is modified in order to complete the dice animation in 1 tick of the timer.
     * The method blockCurrentPlayerMenu() from the LudoGUI class is temporarily deactivated, considering the menus are not used in these tests.
     * All System.out.print* calls within the game are temporarily deactivated.
     * All dialog windows showing game results are temporarily deactivated
     */ 
    public void testGame(){ 
        int counter=0;
        int turnStats=0;
        int castStats=0;
        GameState game;
        final int ITERATIONS = 100;
        float[] gameDuration = new float[ITERATIONS];
        float[][] statistic= new float[4][4]; //row is place (1st, 2d, 3d, 4th) column is token(0,1,2,3)
        
////        1st scenario : 4 players customAI controller ------
//        ImgPath.Theme theme = ImgPath.Theme.plain;
//        List<ImgPath.Color> plColors = Arrays.asList(Color.yellow, Color.red, Color.green, Color.blue);
//        List<Boolean> auto = Arrays.asList(true, true, true, true);
//        List<Autoplay.AutoplayMode> autoMode = Arrays.asList(Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.customAI);
//        boolean special = true;
        
//        2nd scenario : 4 players qLearning controller ------
//        ImgPath.Theme theme = ImgPath.Theme.plain;
//        List<ImgPath.Color> plColors = Arrays.asList(Color.yellow, Color.red, Color.green, Color.blue);
//        List<Boolean> auto = Arrays.asList(true, true, true, true);
//        List<Autoplay.AutoplayMode> autoMode = Arrays.asList(Autoplay.AutoplayMode.qLearning, Autoplay.AutoplayMode.qLearning, Autoplay.AutoplayMode.qLearning, Autoplay.AutoplayMode.qLearning);
//        boolean special = true;
        
//        3nd scenario : 4 players random controller ------
//        ImgPath.Theme theme = ImgPath.Theme.plain;
//        List<ImgPath.Color> plColors = Arrays.asList(Color.yellow, Color.red, Color.green, Color.blue);
//        List<Boolean> auto = Arrays.asList(true, true, true, true);
//        List<Autoplay.AutoplayMode> autoMode = Arrays.asList(Autoplay.AutoplayMode.random, Autoplay.AutoplayMode.random, Autoplay.AutoplayMode.random, Autoplay.AutoplayMode.random);
//        boolean special = true;
        
        //4th scenario : 3 players each with a different controller ------
        ImgPath.Theme theme = ImgPath.Theme.plain;
        List<ImgPath.Color> plColors = Arrays.asList(Color.red,Color.green,Color.blue);
        List<Boolean> auto = Arrays.asList(true, true, true, true);
        List<Autoplay.AutoplayMode> autoMode = Arrays.asList(Autoplay.AutoplayMode.random, Autoplay.AutoplayMode.random, Autoplay.AutoplayMode.customAI, Autoplay.AutoplayMode.qLearning);
        boolean special = true;
        
        do{
        long initialTime = Calendar.getInstance().getTimeInMillis();
        game = new GameState(theme, plColors, auto, autoMode, special);
        game.setDebug(true);
        LudoGUI.drawGUI(game);
        long finalTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("\n-----GAME OVER!-----");
        for (int i = 0; i < game.getWinners().size(); i++) {
            System.out.printf("%d place - %s player\n", i + 1, game.getPlayers()[game.getWinners().get(i)].getColor());
                } 
        gameDuration[counter] = (finalTime-initialTime);
        System.out.printf("Game duration: %.1f miliseconds\n", gameDuration[counter]);
        System.out.println("Total dice casts: "+game.getDice().getDiceRollCount());
        castStats+=game.getDice().getDiceRollCount();
        System.out.println("Total turns: "+game.getTurnCount());
        turnStats+=game.getTurnCount();
        for(int i=0; i<game.getWinners().size(); i++)
            statistic[i][game.getWinners().get(i)]+=1;
        counter++;
        }
        while(counter<ITERATIONS);
        System.out.println("\n____________________________________________");
        System.out.println("Statistics");
        System.out.println("Place\u0009Yellow\u0009Red\u0009Green\u0009Blue");
        int place = 1;
        for (float[] row: statistic){
            System.out.printf(place+"\u0009");
            for (float column: row)
                System.out.printf("%.0f\u0009",column);
            System.out.println("");
            place++;
        }
        float avgGameDuration =0;
        System.out.println("\nGame Duration (ms)");
        for(float duration: gameDuration){
            System.out.printf("%.0f ",duration);
            avgGameDuration+= duration; 
        }
        System.out.println("");
        System.out.println("\nResults winnning% by Player color");
        System.out.println("Color\u00091st\u00092nd\u00093rd\u00094th");
        for(Color color: Color.values()){
            System.out.print(color.name()+":\u0009");
            for (int j=0; j<4; j++){
                System.out.printf("%.2f\u0009",statistic[j][color.ordinal()]*100/counter); 
            }
            System.out.println("");
        }
        System.out.println("\nAverage game duration: "+avgGameDuration/counter);    
        System.out.println("Average turns: "+turnStats/counter);
        System.out.println("Average casts: "+castStats/counter);
        System.out.println("____________________________________________\n");
    }
    
    public void testRuntime(){
    
    }
    
    public void testCustomAI(){
        ImgPath.Theme theme = ImgPath.Theme.plain;
        List<ImgPath.Color> plColors = Arrays.asList(Color.yellow, Color.red, Color.green, Color.blue);
        boolean special = true;
        
        GameState game = new GameState(theme, plColors, special);
        Autoplay custom = new Autoplay();
        
//        Rewards:
//        "startToken", 10
//        "hitStar", 6
//        "hitGlobe", 8
//        "behindOpponent", 2 
//        "aheadOpponent", -3 
//        "sendHome", 10
//        "hitLastStar", -15
//        "startFinalTrack", 9
//        "hitGoal", 5

//        Scenario 1: All tokens of all players will be at position OUT_OF_BOARD
//                    Dice value for all turns will be 6
//                    Expected reward for all moves is 10
        System.out.println("\n----- startToken - Expected reward: 10 -----");
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            game.getDice().setResult(6);
            System.out.println(game.getPlayer(game.getCurrentPlayer()).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));           
        }
        
//        Scenario 2: All tokens on their each player's START tile
//                    Dice value for all turns will be 5 (first star)
//                    Expected reward for all moves is 6
        System.out.println("\n----- hitStar - Expected reward: 6 -----");
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            for (int j=0; j<4; j++)
                game.getPlayer(i).start(j);
            game.getDice().setResult(5);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));           
        }  
        
//        Scenario 3: All tokens set 1 space behind the closest globe
//                    Dice value for all turns will be 1 (to hit the closest globe)
//                    Expected reward for all moves is 8
        System.out.println("\n----- hitGlobe - Expected reward: 8 -----");
        
        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                game.getPlayer(i).getToken(j).moveToken(7);
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            game.getDice().setResult(1);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));           
        }  
        
//        Scenario 4: Move all the tokens of 1 player 7 tiles behind the tokens of another player
//                    Dice value for all turns will be 1 (to be within 6 tiles behind an opponent)
//                    Expected reward for all moves is 8
        System.out.println("\n----- behindOpponent - Expected reward: 8 -----");
        
        for (int i=0; i<3; i++){
            for (int j=0; j<4; j++)
                game.getPlayer(i).getToken(j).moveToken(6);
            game.setCurrentPlayer(i);
            game.getDice().setResult(1);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));           
        }
        for (int j=0; j<4; j++){
            game.getPlayer(0).getToken(j).moveToken(-6);
            game.getPlayer(3).getToken(j).moveToken(6);
        }
        game.setCurrentPlayer(3);
        game.getDice().setResult(1);
        System.out.println(game.getPlayer(3).getColor());
        System.out.println("Selected Token: "+custom.selectToken(game));
        for (int j=0; j<4; j++)
            game.getPlayer(0).getToken(j).moveToken(6);
        
    
//        Scenario 5: Move all the tokens of 1 player 1 tile behind the tokens of another player
//                    Dice value for all turns will be 7 (to be within 6 tiles ahead an opponent)
//                    Expected reward for all moves is -12
        System.out.println("\n----- aheadOpponent - Expected reward: -12 -----");
        
        for (int i=0; i<3; i++){
            for (int j=0; j<4; j++)
                game.getPlayer(i).getToken(j).moveToken(12);
            game.setCurrentPlayer(i);
            game.getDice().setResult(7);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));           
        }
        for (int j=0; j<4; j++){
            game.getPlayer(0).getToken(j).moveToken(-12);
            game.getPlayer(3).getToken(j).moveToken(12);
        }
        game.setCurrentPlayer(3);
        game.getDice().setResult(7);
        System.out.println(game.getPlayer(3).getColor());
        System.out.println("Selected Token: "+custom.selectToken(game));
        
        
//        Scenario 6: Move all the tokens of 1 player 1 tile behind the tokens of another player
//                    Dice value for all turns will be 1 (to send opponents token to its home area)
//                    Expected reward for all moves is 10
        System.out.println("\n----- sendHome - Expected reward: 10 -----");
        
        for (int i=3; i>0; i--){
            game.setCurrentPlayer(i);
            game.getDice().setResult(1);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));  
            for (int j=0; j<4; j++)
                game.getPlayer(i).getToken(j).moveToken(-12);
        } 
        for (int j=0; j<4; j++){
            game.getPlayer(0).getToken(j).moveToken(12);
        }
        game.setCurrentPlayer(0);
        game.getDice().setResult(1);
        System.out.println(game.getPlayer(0).getColor());
        System.out.println("Selected Token: "+custom.selectToken(game));
        
        for (int j=0; j<4; j++){
            game.getPlayer(0).getToken(j).moveToken(-12);
        }
        
//        Scenario 7: All the tokens of a player will be set in the tile before the last star for that color
//                    Dice value for all turns will be 1 (to hit the last star)
//                    Expected reward for all moves is -9 (+6 hitStar , -15 hitLastStar)
        System.out.println("\n----- hitLastStar - Expected reward: -9 -----"); 
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            for (int j=0; j<4; j++)
                game.getPlayer(i).getToken(j).moveToken(36);
            game.getDice().setResult(1);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));
        }
        
//        Scenario 8: All the tokens of a player will be set in the tile before the last star for that color
//                    Dice value for all turns will be 2 (to start the final track)
//                    Expected reward for all moves is 9
        System.out.println("\n----- startFinalTrack - Expected reward: 9 -----"); 
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            game.getDice().setResult(2);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));
        }
        
//        Scenario 9: All the tokens of a player will be set in the first tile of their final track
//                    Dice value for all turns will be 5 (to hit the goal tile)
//                    Expected reward for all moves is 5     
        
        for (int i=0; i<4; i++)
            for (int j=0; j<4; j++)
                game.getPlayer(i).moveSelectedToken(j, 2);
        System.out.println("\n----- hitGoal - Expected reward: 5 -----"); 
        for (int i=0; i<4; i++){
            game.setCurrentPlayer(i);
            game.getDice().setResult(5);
            System.out.println(game.getPlayer(i).getColor());
            System.out.println("Selected Token: "+custom.selectToken(game));
        }
    }
}