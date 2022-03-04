// linxuan ji
// 2063382
// TA: Jun song
// February 5th, 2021
// This program get the words set from dictionary and use map to associate with the patterns.
// In the program we use set of word, set of characters to record the process the user play.
// Show out the guessleft time, the guess character and the pattern.
import java.util.*;

public class HangmanManager {
    
    private Set<String> family;     // the set String to store the words of one pattern
    private Set<Character> guessing;// the set of character of the guess character.
    private int guessesLeft;        // the time of guess left to guess.
    private String pattern;         // the pattern of the words in the family.
    
    // Construct the HangmanManager to passed a dictionary of words to initial the state.
    // And all the words have the given length eliminating any duplicates. 
    // throw the IllegalArgumentException if the words' length smaller than 1 or max less than 0.
    // parameter: int length - the given length of words.
    //            int max - the maximum number of wrong guesses the user can guess.
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        family = new TreeSet<String>();
        guessing = new TreeSet<Character>();
        guessesLeft = max;
        pattern = "-";
        for (int i = 0; i < length - 1; i++) {
            pattern += " -";
        }
        for (String word : dictionary) {
            if (word.length() == length) {
                family.add(word);
            }
        }
    }

    //return the current set of words 
    public Set<String> words() {
        return family;
    }
    
    // return the time of guesses left to guess.
    public int guessesLeft() {
        return guessesLeft;
    }
    
    // return the set of guess characters.
    public Set<Character> guesses() {
        return guessing;
    }
    
    // throw the IllegalStateException if the word string is empty.
    // return the current pattern displayed for the game
    public String pattern() {
        if(family.isEmpty()) {
            throw new IllegalStateException();
        }
        return pattern;
    }
    
    // during the guess, make the pattern and words correspond to each other.
    // assuming that all guesses passed to the record method are lowercase letters.
    // parameter: char guess - the character the users guess and assume to be lowercase.
    //            Map<String, Set<String>> hangMan - the map to associate 
    //            family patterns with the set of words.
    private void correspondPatt(char guess, Map<String, Set<String>> hangMan) {
        for (String strWord: family) {
            String patt = "";
            for(int i = 0; i < strWord.length(); i++) {
                if (guess == strWord.charAt(i)) {
                    patt += guess + " ";
                } else {
                    if (i * 2 + 2 > pattern.length()) {
                        patt += pattern.substring(i * 2);
                    } else {
                        patt += pattern.substring(i * 2, i * 2 + 2);
                    } 
                }
            }
            patt = patt.trim();
            if (!hangMan.containsKey(patt)) {
                hangMan.put(patt, new TreeSet<String>());
            } 
            hangMan.get(patt).add(strWord);
        }
    }

    //find the maxsize of the pattern has the most number of words to let the users guess 
    // return the current maxsize pattern 
    // Map<String, Set<String>> hangMan - the map to associate 
    //    family patterns with the set of words.
    private String maxSetPattern(Map<String, Set<String>> hangMan) {
        int maxOfSet = 0;
        for (String currPatt: hangMan.keySet()) {
            int mapSize = hangMan.get(currPatt).size();
            if (maxOfSet < mapSize) {
                maxOfSet = mapSize;
                pattern = currPatt;
                family = hangMan.get(currPatt);
            }
        }
        return pattern;
    }
    
    // record the process the user guess the words
    // find the set of words to use going forward
    // record the number of occurrences of the guessed letter in the new pattern
    // throw the IllegalStateException if time of guesses left is less than 1 
    // and the set of word is empty.
    // throw the IllegalArgumentException if the character being guessed was guessed previously.
    // return the update the number of time of guesses left. 
    // char guess - the character that unser guess and assume to be lowercase.
    public int record(char guess) {
        if (family.isEmpty() || guessesLeft < 1) {
            throw new IllegalStateException();
        }
        if (guessing.contains(guess)) {
            throw new IllegalArgumentException(); 
        }
        Map<String, Set<String>> hangMan = new TreeMap<String, Set<String>>();
        guessing.add(guess);
        correspondPatt(guess, hangMan);
        maxSetPattern(hangMan);
        int time = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == guess) {
                time++;
            }
        }
        if (time == 0) {
            guessesLeft--;
        }
        return time;     
    }
}
