import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LetterScorer {
  private Map<Character, Integer> letterScores;

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

  public Integer score(Character c) {
    // This is how we indicate the wildcard letters
    if (c >= 'a' && c <= 'z') {
      return 0;
    }
    return letterScores.get(c);
  }
}
