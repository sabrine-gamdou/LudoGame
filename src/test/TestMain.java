/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import ludo.ImgPath;

/**
 * This application executes a set of tests to all the classes representing the elements of a Ludo game.
 * 
 * @author Carla Villegas <carv@itu.dk>
 */
public class TestMain {

    /**
     *
     */
    public static Test test = new Test();
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) { 
        new ImgPath();
//        test.testToken();
//        test.testPlayer();
//        test.testBoard();
//        test.testDice();
//        
//        test.testQLearning();
        test.testGame();
//          test.testCustomAI();
    }
}
