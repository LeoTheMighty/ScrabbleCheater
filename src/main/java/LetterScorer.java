import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class to score the individual letters in the game.
 */
public class LetterScorer {
  private Map<Character, Integer> letterScores;

  /**
   * Initializes the letter scorer from a csv {@link InputStream}. Each row should have the letter
   * and then the score for it.
   *
   * @param source The {@link InputStream} source for the csv file to init the scorer with.
   */
  public LetterScorer(InputStream source) {
    letterScores = new HashMap<>();
    Scanner sc = new Scanner(source);
    while (sc.hasNextLine()) {
      String[] letterScore = sc.nextLine().split(",");
      if (letterScore.length != 2) {
        throw new IllegalArgumentException("Improperly formatted score file");
      }
      String letter = letterScore[0].trim();
      Integer score = Integer.parseInt(letterScore[1].trim());
      if (letter.length() != 1) {
        throw new IllegalArgumentException(String.format("Letter (%s) value in score file too long", letter));
      }
      letterScores.put(letter.charAt(0), score);
    }
  }

  /**
   * Gets the score for an individual letter in the alphabet.
   *
   * @param c The letter to get the score for.
   * @return The score for the letter or null if it's not a letter in the scorer.
   */
  public Integer score(Character c) {
    // This is how we indicate the wildcard letters
    if (c >= 'a' && c <= 'z') {
      return 0;
    }
    return letterScores.get(c);
  }
}
