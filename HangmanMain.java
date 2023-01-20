// Class HangmanMain is the driver program for the Hangman program.  It reads a
// dictionary of words to be used during the game and then plays a game with
// the user.  This is a cheating version of hangman that delays picking a word
// to keep its options open.

import java.util.*;
import java.io.*;

public class HangmanMain {
	public static final String DICTIONARY_FILE = "dictionary2.txt";
	public static final boolean DEBUG = true;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Welcome to the cse143 hangman game.");
		System.out.println();

		// open the dictionary file and read dictionary into an ArrayList
		Scanner input = new Scanner(new File(DICTIONARY_FILE));
		List<String> dictionary = new ArrayList<String>();
		while (input.hasNext()) {
			dictionary.add(input.next().toLowerCase());
		}

		Scanner console = new Scanner(System.in);
		System.out.print("What length word do you want to use? ");
		int length = console.nextInt();
		System.out.print("How many wrong answers allowed? ");
		int max = console.nextInt();
		System.out.println();

		// Starts the game
		List<String> dictionary2 = Collections.unmodifiableList(dictionary);
		HangmanManager hangman = new HangmanManager(dictionary2, length, max);
		if (hangman.words().isEmpty()) {
			System.out.println("No words of that length in the dictionary.");
		} else {
			playGame(console, hangman);
			showResults(hangman);
		}
	}

	// Plays one game with the user
	public static void playGame(Scanner console, HangmanManager hangman) {
		while (hangman.guessesLeft() > 0 && hangman.pattern().contains("-")) {
			System.out.println("guesses : " + hangman.guessesLeft());
			if (DEBUG) {
				System.out.println(hangman.words().size() + " words left: "
						+ hangman.words());
			}
			System.out.println("guessed : " + hangman.guesses());
			System.out.println("current : " + hangman.pattern());
			System.out.print("Your guess? ");
			char ch = console.next().toLowerCase().charAt(0);
			if (hangman.guesses().contains(ch)) {
				System.out.println("You already guessed that");
			} else {
				int count = hangman.record(ch);
				if (count == 0) {
					System.out.println("Sorry, there are no " + ch + "'s");
				} else if (count == 1) {
					System.out.println("Yes, there is one " + ch);
				} else {
					System.out.println("Yes, there are " + count + " " + ch
							+ "'s");
				}
			}
			System.out.println();
		}
	}

	// Shows the result of the game
	public static void showResults(HangmanManager hangman) {
		String answer = hangman.words().iterator().next();
		System.out.println("answer = " + answer);
		if (hangman.guessesLeft() > 0) {
			System.out.println("You beat me");
		} else {
			System.out.println("Sorry, you lose");
		}
	}
}
