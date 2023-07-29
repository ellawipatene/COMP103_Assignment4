// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2021T2, Assignment 4
 * Name: Ella Wipatene
 * Username: wipateella
 * ID: 300558005
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * A Decision Tree is a structure for getting to an answer by following
 * through a tree of yes/no questions.
 * Each node has a field to hold the text of a question or decision, and
 *  either two children or no children.
 * If a node has two children,
 * - the text represents the question to ask
 * - the Yes child is the decision tree to follow if the answer to the question is yes
 * - the No child is the decision tree to follow if the answer to the question is no
 * If a node has no children (a leaf node)
 * - the text represents the decision or answer
 * A leaf node can be turned into an internal node by
 *  changing its text to a question and adding two children.
 */

public class DTNode{

    private String text;
    private DTNode yes;
    private DTNode no;
    
    //For challenge
    // string = answer, DTNode = the answers node
    private Map<String, DTNode> moreAnswers = new HashMap<String, DTNode>(); 

    /**
     * Construct a new node with some text.
     */
    public DTNode(String txt){
        text = txt;
    }

    /**
     * Construct a new node with text and two children
     */
    public DTNode(String txt, DTNode yesChild, DTNode noChild){
        text = txt;
        setChildren(yesChild, noChild);
    }

    /** Getters */
    public String getText(){return text;}

    public DTNode getYes(){return yes;}

    public DTNode getNo(){return no;}
    
    public Set<String> getKeySet(){
        return moreAnswers.keySet(); 
    } 
    
    public Map<String,DTNode> getMap(){
        return this.moreAnswers; 
    }

    /** Is this an answer node? */
    public boolean isAnswer(){
        return (yes==null && no==null);
    }

    /** Setters */
    public void setText(String t ){text=t;}

    public void setChildren(DTNode yesChild, DTNode noChild){
        if ((yesChild==null) != (noChild == null)){
            throw new RuntimeException("Not allowed to have one null child");
        }
        yes = yesChild;
        no = noChild;
    }
    
    /**
     * For challenge
     */
    public void addAnswer(String answer, DTNode node){
        moreAnswers.put(answer, node); 
    }

    public static final int WIDTH = 95;
    public static final int HEIGHT = 15;

    /**
     * Draw the node (as a box with the text) on the graphics pane
     * (x,y) is the center of the box
     * The box should be WIDTH wide, and HEIGHT high, and
     * the text should be truncated to fit.
     */
    public void draw(double x, double y){
        double left = x-WIDTH/2;
        String toDraw = text.substring(0, Math.min(text.length(),15));

        UI.eraseRect(left, y-HEIGHT/2, WIDTH, HEIGHT);  // to clear anything behind it
        UI.drawRect(left, y-HEIGHT/2, WIDTH, HEIGHT);
        UI.drawString(toDraw, left+2, y+6);             // assume height of characters = 12
    }

}
