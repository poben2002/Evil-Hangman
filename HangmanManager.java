// Benjamin Po
// 11/9/21
// CSE143X
// TA: Justin Shaw
// Assignment #7 (Evil Hangman)
//
// This class will keep track of the state of a special game of
// hangman that will delay picking a word until it is forced
// to.

import java.util.*;

public class HangmanManager {
   //fields for the the set of words that the program could choose from,
   // the amount of wrong guesses the user has remaining, the pattern of the
   // word being guessed, and a set of the letters that have been guessed
   // by the user
   private Set<String> wordsLeft;
   private int guessCount;
   private String pattern;
   private Set<Character> lettersGuessed;
   
   // Constructor for the HangmanManager that is passed a dictionary of words,
   // a target word length, and the max amount of wrong guesses the user has
   // and initializes the state of the game
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      // throws an IllegalArgumentException if the length of the word the user
      // chooses is less than 1, and the maximum amount of incorrect guesses
      // is less than 1
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      //Creates two new maps, one for the letters the user guesses
      // and the other for the remaining pool of words the program
      // can choose from
      lettersGuessed = new TreeSet<>();
      wordsLeft = new TreeSet<>();
 
      // For each loop that goes through the dictionary of allowed words
      // from Hangman Main. Adds the words with the same length as the length
      // chosen by the user to the Set of choosable words
      for (String word : dictionary) {
      // If the word length of the words from the dictionary is the same
      // as the length the user inputs at the start
         if (word.length() == length) {
            wordsLeft.add(word);
         }
      }
      // Contructs the pattern of dashes based on lenght of the word
      pattern = "";
      for (int k = 0; k < length; k++) {
         pattern += ("- ");
      }
      // Sets the guess count equal to the maximum amount of incorrect
      // guesses the user is alotted
      guessCount = max;
   }
   
   // Method that returns the current pattern to be displayed
   // while taking account guessses that have been inputted.
   // Throws exception if set is empty
   public String pattern() {
      // Exception thrown if the set of possible words is empty
      if (wordsLeft.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   // Method that is called by client to get access to get
   // the current set of words being considered
   public Set<String> words() {
      return wordsLeft;
   }
   
   // Method that is called by client to get access to find the
   // current set of letter that have already been guessed
   public Set<Character> guesses() {
      return lettersGuessed;
   }
   
   // Method that is called by the client to see how many remaining
   // guesses the player has 
   public int guessesLeft() {
      return guessCount;
   }
   
   // Private method that takes a String of a word and the character
   // that the user guesses and returns the updated pattern after
   // user guesses
   private String patternTracker(String word, char guess) {
      int count = 0;
      String commonPattern = "";
      // For loop that traverses throught the word checking for
      // matches of the guessed character within the word
      for (int k = 0; k < word.length(); k++) {
         if (word.charAt(k) != guess) {
         // Changing the pattern if the letter is not matching
            commonPattern += pattern.substring(count, count + 2);
         }
         else {
         // Adds the character to the pattern if they are matching
            commonPattern += guess + (" ");
         }
         count = count + 2;
      }
      return commonPattern;
   }
   
   // Tracks the occurences of matching letters with words
   // remaining in the possible word pool. If the number occurences 
   // is zero, amount is reduced by one. Returns
   // the maximum occurences of the letters
   private int occurances(char guess) {
      
      // Amount of occurances of the letter guessed and
      // adding the guessed character to the set
      int maxOccurance = 0;
      lettersGuessed.add(guess);
      
      // For loop that traverses the pattern and checks for occurances
      // of the guess in the pattern 
      for (int k = 0; k < pattern.length(); k++) {
         if (pattern.charAt(k) == guess) {
            // Adds one if there is an occurance
            maxOccurance++;
         }
      }
      // If there is no occurances, subtracts one
      if (maxOccurance == 0) {
         guessCount--;
      }
      return maxOccurance;
   }
   
   // Private method that takes a parameter of the guess char and
   // returns a map of String and a Set of strings that decides
   // what set of words to use after taking into account what letter
   // the user guesses
   private Map<String, Set<String>> groupWords(char guess) {
      
      // New Map of type String and Set<String> 
      Map<String, Set<String>> group = new TreeMap<>();
      // Adds guessed character to Set of already guessed characters
      lettersGuessed.add(guess);
      // For each loop that calls patternTracker
      for (String word : wordsLeft) {
         String wordPattern = patternTracker(word, guess);
         // If the map does not already contain the key
         if (!group.containsKey(wordPattern)) {
         // Puts the new wordPattern into a new TreeSet
            group.put(wordPattern, new TreeSet<>());
         }
         // Adding possible words to the map based on the word pattern
         group.get(wordPattern).add(word);
      }  
      return group;  
   }
   
   // Method that takes parameter of char guess and does the bulk of the work 
   // through calling other private helper methods. Records the next guess of 
   // the user, then uses the groupWords method to decide what set of words can 
   // still be used. Returns occurances of a guessed letter in the updated pattern
   // and uses a private helper method to update remaining guesses. Method throws
   // Illegal State Exception if number of remaining guesses is not at least 1 and
   // Illegal Argument Exception if previous exception was not thrown and the character
   // was already guessed
   public int record(char guess) {
      // Throws IllegalStateException when the remaining possible words set is empty
      // or the amount of remaining guesses is less than one
      if (wordsLeft.isEmpty() || guessCount < 1) {
         throw new IllegalStateException();
      }
      // Throws an IllegalArgumentException if previous exception was not thrown and
      // the set of possible words is not empty and the guess inputted was already guessed
      else if (!wordsLeft.isEmpty() && lettersGuessed.contains(guess)) {
         throw new IllegalArgumentException();
      }
      // Calling helper method groupWords to create a Map
      Map<String, Set<String>> wordGroup = groupWords(guess);
      
      int maxSize = 0;
      // If the map is not empty run for each loop 
      if (!wordGroup.isEmpty()) {
         for (String currentPattern: wordGroup.keySet()) {
            if (wordGroup.get(currentPattern).size() > maxSize) {
            // Updates the set of possible words and pattern the user sees
               wordsLeft.clear();
               wordsLeft.addAll(wordGroup.get(currentPattern));
               pattern = currentPattern;
               maxSize = wordGroup.get(currentPattern).size();
            }            
         }
      }
      // Calls occurances helper method to return number of occurances of 
      // the guessed character
      return occurances(guess);
   }

}