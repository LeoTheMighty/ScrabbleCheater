import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * The class for storing the dictionary of words and checking to see that a word exists in that
 * dictionary.
 */
public class Dictionary {
  // This is approximately how many words the set will have at most
  private final static int initialCapacity = 300000;
  private Set<String> dictionary;

  /**
   * Initializes the dictionary from a text file {@link InputStream}.
   *
   * @param source The {@link InputStream} for the text file to get the words from.
   */
  public Dictionary(InputStream source) {
    dictionary = new HashSet<>(initialCapacity);
    Scanner sc = new Scanner(source);
    while (sc.hasNextLine()) {
      dictionary.add(sc.nextLine().toUpperCase());
    }
  }

  /**
   * Discerns whether a word is in the dictionary or not.
   *
   * @param word The word to check for (case doesn't matter).
   * @return Whether the word is in the dictionary.
   */
  public boolean isValid(String word) {
    return dictionary.contains(word.toUpperCase());
  }
}
