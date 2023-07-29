// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2021T2, Assignment 4
 * Name: Ella Wipatene
 * Username: wipateella
 * ID: 300558005
 */

/**
 * Implements a decision tree that asks a user yes/no questions to determine a decision.
 * Eg, asks about properties of an animal to determine the type of animal.
 * 
 * A decision tree is a tree in which all the internal nodes have a question, 
 * The answer to the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have the decision (the kind of animal in the example tree).
 *
 * The decision tree may be a predermined decision tree, or it can be a "growing"
 * decision tree, where the user can add questions and decisions to the tree whenever
 * the tree gives a wrong answer.
 *
 * In the growing version, when the program guesses wrong, it asks the player
 * for another question that would help it in the future, and adds it (with the
 * correct answers) to the decision tree. 
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class DecisionTree {

    public DTNode theTree;    // root of the decision tree;
    public Map <DTNode, Integer> drawMap = new HashMap <DTNode, Integer>(); 
    public Map <Integer, Integer> layerNums = new HashMap <Integer, Integer>(); 
    public int max_layer = 0; 
    public boolean left = true;  
    

    /**
     * Setup the GUI and make a sample tree
     */
    public static void main(String[] args){
        DecisionTree dt = new DecisionTree();
        dt.setupGUI();
        dt.loadTree("sample-animal-tree.txt");
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Load Tree", ()->{loadTree(UIFileChooser.open("File with a Decision Tree"));});
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Run Tree", this::runTree);
        UI.addButton("Grow Tree", this::growTree);
        UI.addButton("Save Tree", this::saveTree);  // for completion
        UI.addButton("Draw Tree", this::drawTree);  // for challenge
        UI.addButton("Add Answers", this::addAnswers); 
        UI.addButton("Reset", ()->{loadTree("sample-animal-tree.txt");});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree,
     * and then its "no" subtree.
     * Needs a recursive "helper method" which is passed a node.
     * 
     * COMPLETION:
     * Each node should be indented by how deep it is in the tree.
     * The recursive "helper method" is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters)
     */
    public void printTree(){
        UI.clearText();
        /*# YOUR CODE HERE */
        preOrderCompl(theTree, "",""); 
    }
    
    /**
     * Prints out elements of the tree recursivley
     */
    public void preOrder(DTNode leaf, String binary){
        if(leaf != null){
            UI.println(binary + leaf.getText()); 
            preOrder(leaf.getYes(), "Yes: "); 
            preOrder(leaf.getNo(), "No: "); 
        }
    }
    
    /**
     * Pre order but is printed nice with indents
     */
    public void preOrderCompl(DTNode leaf, String binary, String indent){
        if(leaf != null){
            UI.println(indent + binary + leaf.getText()); 
            preOrderCompl(leaf.getYes(), "Yes: ", indent + " "); 
            preOrderCompl(leaf.getNo(), "No: ", indent + " "); 
        }
    }

    /**
     * Run the tree by starting at the top (of theTree), and working
     * down the tree until it gets to a leaf node (a node with no children)
     * If the node is a leaf it prints the answer in the node
     * If the node is not a leaf node, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     */
    public void runTree() {
        /*# YOUR CODE HERE */
        boolean is_answer = false; 
        DTNode current_node = theTree;
        String answer = ""; 
        while(!is_answer){
            answer = UI.askString("Is it true:" + current_node.getText() + "(Yes/No):"); 
            
            // Traverse tree acording to answer
            if(answer.equals("No") || answer.equals("no") || answer.equals("n") || answer.equals("N")){
                current_node = current_node.getNo();
                is_answer = current_node.isAnswer();
            } else if (answer.equals("Yes") || answer.equals("yes") || answer.equals("y") || answer.equals("Y")){
                current_node = current_node.getYes();
                is_answer = current_node.isAnswer();
            }
            
            if(is_answer){
                UI.println("The answer is: " + current_node.getText()); 
            }
        }
    }

    /**
     * Grow the tree by allowing the user to extend the tree.
     * Like runTree, it starts at the top (of theTree), and works its way down the tree
     *  until it finally gets to a leaf node. 
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it prints the decision, and asks if it is right.
     * If it was wrong, it
     *  - asks the user what the decision should have been,
     *  - asks for a question to distinguish the right decision from the wrong one
     *  - changes the text in the node to be the question
     *  - adds two new children (leaf nodes) to the node with the two decisions.
     */
    public void growTree () {
        /*# YOUR CODE HERE */
        
        boolean is_answer = false; 
        DTNode current_node = theTree;
        String answer = ""; 
        String new_question = "";
        while(!is_answer){ // if it is a question
            answer = UI.askString("Is it true:" + current_node.getText() + "(Yes/No):"); // ask answer to q. 
        
            // Traverse the tree acording to the answer
            if(answer.equals("No") || answer.equals("no") || answer.equals("n") || answer.equals("N")){
                current_node = current_node.getNo();
                is_answer = current_node.isAnswer();
            } else if (answer.equals("Yes") || answer.equals("yes") || answer.equals("y") || answer.equals("Y")){
                current_node = current_node.getYes();
                is_answer = current_node.isAnswer();
            }
            
            // if they have arrived at an answer
            if(is_answer){
                answer = UI.askString("I think I know, is it a " +  current_node.getText() + "?"); 
            }
        }
        
        // if it is not what was suggested
        if(answer.equals("No") || answer.equals("no") || answer.equals("n") || answer.equals("N")){
            answer = UI.askString("Ok, what should the answer be?"); // get the correct answer
            UI.println("Oh. I can't distinguish a " + current_node.getText() + " from a " + answer); 
            UI.println("Tell me something that is true for a " + answer + " but not a " + current_node.getText()); 
            new_question = UI.askString("Property: "); //get the new question
            
            DTNode yes = new DTNode(answer); // insert answer into tree
            DTNode no = new DTNode(current_node.getText());
            
            current_node.setText(new_question + "?"); // insert question into tree
            current_node.setChildren(yes, no); // add the answers
            
            UI.println("Thank you! I've updated my decision tree.");  
        }else{
            UI.println("Yay! I guessed correct!"); 
        }

    }

    // You will need to define methods for the Completion and Challenge parts.
    
    /**
     * Method to save the tree to a file. 
     */
    public void saveTree(){
        String fileName = UIFileChooser.save("Filename to save to:");
        try {
            PrintStream outfile = new PrintStream(fileName);
            saveNode(outfile, theTree);
            outfile.close();
        } catch (IOException e) { UI.println("File failure: " + e); }
    }
    
    
    /**
     * A recursive method that traverses the tree
     * Printing to the outfile if it is a question or answer.
     * For saving the tree to a file. 
     */
    public void saveNode(PrintStream outfile, DTNode leaf){
        if(leaf != null){
            if(!leaf.isAnswer()){
                outfile.println("Question: " + leaf.getText()); 
            }else{
                outfile.println("Answer: " + leaf.getText()); 
            }
            
            saveNode(outfile, leaf.getYes()); 
            saveNode(outfile, leaf.getNo()); 
        }
    }
    
    /**
     * Calls method to draw tree
     */
    public void drawTree(){ 
        //breathFirst(theTree); 
        UI.clearGraphics(); 
        theTree.draw(400, 10); 
        test(theTree, 400, 10); 
    }
        
    
    /**
     * A recursive method that draws out the tree
     */
    public void test(DTNode node,  float x, float y){
        if(node != null){
                if(!node.isAnswer()){  // if it is not an answer
                    //Draw the Yes node
                    node.getYes().draw(x + x/3, y + 50); 
                    UI.drawLine(x, y + 7.5, x + x/3, y + 42.5); // connect with a line
                    UI.println(node.getYes().getText() + ": " + String.valueOf(x + x/2) + " " + String.valueOf(y + 50)); // print out the coords for testing
                    test(node.getYes(), x + x/3, y + 50); // call recursive method
                    
                    if(y == 60){ // shifts the right side of the graph by 200 so that there are no overlaps
                        x = x + 200;  
                    }
                    
                    // Draw the No node
                    node.getNo().draw(x - x/3, y + 50);
                    UI.drawLine(x, y + 7.5, x - x/3, y + 42.5);
                    UI.println(node.getNo().getText() + ": " + String.valueOf(x - x/2) + " " + String.valueOf(y + 50)); 
                    test(node.getNo(), x - x/3, y + 70); 
                }
        }
    }
    
    /**
     * Returns true if number is odd
     */
    public boolean isOdd(int num){
        if ( num % 2 == 0 ){return false;}
        return true; 
    }
    
    /**
     * Challenge 2:
     * Add more answers to questions
     */
    public void addAnswers(){
        boolean is_answer = false; 
        DTNode current_node = theTree;
        String answer = ""; 
        String new_question = "";
        while(!is_answer){ // if it is a question
            UI.println("Answers for: " + current_node.getText()); 
            Set<String> answers = current_node.getKeySet(); 
            for(String s: answers){ // list the current answers
                UI.println("   - " + s); 
            }

            answer = UI.askString("Do you want to add an answer to this question? (Yes/No):"); // ask answer to q. 
            
            if(answer.equals("No") || answer.equals("no") || answer.equals("n") || answer.equals("N")){ // if they dont want to change that question
                answer = UI.askString("Which answer node do you want to traverse now?");
                if(!answers.contains(answer)){ // if they entered an invalid answer
                    answer = UI.askString("Please enter one of the answers above?");
                }
                Map<String, DTNode> traverseMap = current_node.getMap(); 
                current_node = traverseMap.get(answer); // traverse to a different node
                is_answer = current_node.isAnswer();
            } else if (answer.equals("Yes") || answer.equals("yes") || answer.equals("y") || answer.equals("Y")){ // if they want to change that question
                answer = UI.askString("Ok, what is the new answer you want to add?"); // get new answer
                String node_text = UI.askString("What is the text for node should this answer lead to?"); // get new node
                DTNode temp = new DTNode(node_text);
                current_node.addAnswer(answer, temp); // set new node
                is_answer = true;
            }
            
            // if they have either traversed the whole tree or added an answer
            if(is_answer){
                UI.println(); 
                UI.println("Here is the edited tree:"); 
                printTreeChallenge(theTree, ""); 
            }
        }
    }
    
    /**
     * Prints the tree if it has multiple answers
     * Recursive method
     */
    public void printTreeChallenge(DTNode node, String answer){
        if(node != null){
            if(!node.isAnswer()){
                UI.println("Question: " + node.getText()); 
            }else{
                UI.println(answer + ": " + node.getText()); 
            }
            
            Map<String, DTNode> traverseMap = node.getMap(); 
            for(String s: traverseMap.keySet()){
                printTreeChallenge(traverseMap.get(s), s); 
            } 
        
        }
    }
    
    //-----------------------------------------------------------------------------------
    //IGNORE BELLOW CODE - Doesn't work perfefctly :) 
    
    public void printMap(){
        for(Integer i: layerNums.keySet()){
            UI.println(i + ": " + layerNums.get(i));
        }
    }
    
        /** 
     * Didn't end up using this method
     */
    public void breathFirst(DTNode root){
        if(root != null){
            Queue<DTNode> todo = new ArrayDeque<DTNode>();
            todo.offer(root);
            
            int counter = 0;
            int curr_layer = 0; 
            
            double y_dist = 0;
            double curr_y = 300; 
            double curr_x = 20;
            
            while(!todo.isEmpty()){
                DTNode node = todo.poll();
                node.draw(curr_x, curr_y);
                UI.println(counter);
                
                layerNums.put(curr_layer, layerNums.get(curr_layer) -1); 
                if(layerNums.get(curr_layer) == 0){
                    curr_layer++;
                    if(layerNums.containsKey(curr_layer)){
                        y_dist = 600/layerNums.get(curr_layer);
                    }else{break;}
                     
                    curr_y = y_dist - y_dist/2;
                    curr_x = curr_x + 150; 
                }else{
                    curr_y = curr_y + y_dist; 
                }
                
                counter++; 
                
                if(node.getYes() != null){
                    todo.offer(node.getYes());
                }
                if(node.getNo() != null){
                    todo.offer(node.getNo()); 
                }
            }
        }
    }
    
    
    public void indent(DTNode leaf,  String indent){
        if(leaf != null){
            if(!layerNums.containsKey(indent.length())){layerNums.put(indent.length(), 1);}
            else{
                int temp = layerNums.get(indent.length()) + 1;
                layerNums.put(indent.length(), temp);
            }
            
            UI.println(indent.length() + " "  +  leaf.getText());  
            indent(leaf.getYes(), indent + " "); 
            indent(leaf.getNo(), indent + " "); 
        }
    }


    // Written for you

    /** 
     * Loads a decision tree from a file.
     * Each line starts with either "Question:" or "Answer:" and is followed by the text
     * Calls a recursive method to load the tree and return the root node,
     *  and assigns this node to theTree.
     */
    public void loadTree (String filename) { 
        if (!Files.exists(Path.of(filename))){
            UI.println("No such file: "+filename);
            return;
        }
        try{theTree = loadSubTree(new ArrayDeque<String>(Files.readAllLines(Path.of(filename))));}
        catch(IOException e){UI.println("File reading failed: " + e);}
    }

    /**
     * Loads a tree (or subtree) from a Scanner and returns the root.
     * The first line has the text for the root node of the tree (or subtree)
     * It should make the node, and 
     *   if the first line starts with "Question:", it loads two subtrees (yes, and no)
     *    from the scanner and add them as the  children of the node,
     * Finally, it should return the  node.
     */
    public DTNode loadSubTree(Queue<String> lines){
        Scanner line = new Scanner(lines.poll());
        String type = line.next();
        String text = line.nextLine().trim();
        DTNode node = new DTNode(text);
        if (type.equals("Question:")){
            DTNode yesCh = loadSubTree(lines);
            DTNode noCh = loadSubTree(lines);
            node.setChildren(yesCh, noCh);
            node.addAnswer("Yes", yesCh); //For challenge
            node.addAnswer("No", noCh);  // For challenge
        }
        return node;

    }



}
