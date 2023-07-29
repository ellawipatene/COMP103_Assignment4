// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2021T2, Assignment 4
 * Name: Ella Wipatene
 * Username: wipateella
 * ID: 300558005
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;

/** 
 *  Compute all permutations of a list of Strings
 *
 *  You only have to write one method - the extendPermutations(...) method
 *  which does the recursive search.  
 */
public class Permutations {
    public int remainingItemsSize = 0; 
    /**
     * Constructs a list of all permutations of the given items
     * by calling a recursive method, passing in a set of the items to permute
     * and an empty list to build up.
     * Prints the total number of permutations in the message window (with
     *  UI.printMessage(...);
     */
    public List<List<String>> findPermutations(Set<String> items){
        UI.clearText();
        counter = 0;
        Set<String> copyOfItems = new HashSet<String>(items);   // a copy of the set of items that can be modified
        List<List<String>> ans = new ArrayList<List<String>>(); // where we will collect the answer
  
        List<String> itemsList = new ArrayList<String>(copyOfItems); // converts the set into a list so that I can refrence and item due to its index
        int length = itemsList.size(); 
        permute(itemsList, 0, length-1); 
        
        UI.println("-----------------------------------"); 
        UI.println("Items: " + length); 
        
        // if the length of the list is over 7, then the resulting permutations will be over 10,000
        // therefore, the counter will only go up to 10,000. If the counter = 10,000, then I calculate
        // the number of permutations by doing the factorial of the amount of items. 
        if(counter == 10000){
            long factorial = 1;
            for(int i = 1; i <= length; i++){
                factorial = factorial * i; 
            }
            UI.println("Permutations: " + factorial);
            UI.println("Only printed the first 10,000 permutations"); 
        }else{
            UI.println("Permutations: " + counter);
        }
        
        return ans;
    }
    
    /**
     * A recursive function
     * Iterates throught the list of items, swaping them until all permutations of
     * the string have been printed. 
     */
    public void permute(List<String> items, int left, int right){
        if(counter < 10000){
            if(left == right){ // If it has gone through the length of the items, print out the permutaion
                for(String str: items){UI.print(str + " ");}
                UI.println();
                counter++; 
            }else{
                for(int i = left; i <= right; i++){
                    String temp = items.get(0 + left); // swap the left letter with the letter at index i. 
                    items.set(0 + left, items.get(0 + i));
                    items.set(0 + i, temp);
                    
                    permute(items, left + 1, right); // calls itself. 
                    
                    String temp2 = items.get(0 + left);  // swap the letters back so that it can do different permutations. 
                    items.set(0 + left, items.get(0 + i));
                    items.set(0 + i, temp2);
                }
            }
        }
    }

    /**
     * Recursive method to build all permutations possible by adding the
     *  remaining items on to the end of the permutation built up so far 
     * If there are no remaining items, then permutationSoFar is complete,
     *   => add a copy of the permutation to allPermutations.
     * Otherwise,
     *  for each of the remaining items,
     *     extend the permutationSoFar with the item, and do a recursive call to extend it more:
     *     - remove the item from remaining items and
     *     - push it onto the permutation so far,
     *     - do the recursive call,
     *     - pop the item from the permutation so far and
     *     - put it back into the remaining items.
     *
     * So that you don't run out of memory, only add the first 10000 permutations to the allPermutations.
     */
    public void extendPermutation(Set<String> remainingItems, Stack<String> permutationSoFar, List<List<String>> allPermutations){
        /*# DOESNT WORK AND DIDN'T END UP USING */
        if(remainingItemsSize > 1){
            Set<String> new_remaining_items = remainingItems; 
            for(String s: remainingItems){
                new_remaining_items.remove(s); 
                permutationSoFar.push(s);
                extendPermutation(new_remaining_items, permutationSoFar, allPermutations);  
                String temp = permutationSoFar.pop(); 
                new_remaining_items.add(temp); 
            } 
        }else{
            allPermutations.add(permutationSoFar); 
            for(String s: permutationSoFar){
                UI.printMessage(s);
            }
            permutationSoFar.clear(); 
        }
    }

    

    //===================================================
    // User Interface code

    /**
     * Setup GUI
     * Buttons to run permutations on either letters or words
     */
    public void setupGUI(){
        UI.addButton("A B C D E", ()->{findPermutations(Set.of("A","B","C","D","E"));});
        UI.addTextField("Letters", (String v)->{findPermutations(makeSetOfLetters(v));});
        UI.addTextField("Words", (String v)->{findPermutations(makeSetOfWords(v));});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    public void printAll(List<List<String>> permutations){
        UI.clearText();
        for (int i=0; i<permutations.size(); i++){
            for (String str : permutations.get(i)){UI.print(str+" ");}
            UI.println();
        }
        UI.println("----------------------");
        UI.printf("%d items:\n", permutations.get(0).size());
        UI.printf("%,d permutations:\n", counter);
        UI.println("----------------------");
    }

    /**
     * Makes a set of strings, one string for each character in the argument
     */
    public Set<String> makeSetOfLetters(String str){
        Set<String> ans = new HashSet<String>();
        for (int i=0; i<str.length(); i++){
            if (str.charAt(i)!=' '){
                ans.add(""+str.charAt(i));
            }
        }
        return Collections.unmodifiableSet(ans);
    }

    /**
     * Makes a set of strings, one string for each word in the argument
     */
    public Set<String> makeSetOfWords(String str){
        Set<String> ans = new HashSet<String>();
        for (String v : str.split(" ")){ans.add(v);}
        return Collections.unmodifiableSet(ans);
    }

    // Counter for the number of complete permutations found
    private long counter = 0;  

    /** Report the value of counter in the message area */
    public void reportCounter(){
        if ((counter<<54)==0) {UI.printMessage((counter>10000000)?((counter>>>20)+"M"):((counter>>>10)+"K"));}
    }

    // Main
    public static void main(String[] arguments) {
        Permutations p = new Permutations();
        p.setupGUI();
    }
}
