import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Dictionary {
  // This is approximately how many words the set will have at most
  private final static int initialCapacity = 300000;
  private Set<String> dictionary;

  public Dictionary(InputStream source) {
    dictionary = new HashSet<>(initialCapacity);
    Scanner sc = new Scanner(source);
    while (sc.hasNextLine()) {
      dictionary.add(sc.nextLine().toUpperCase());
    }
  }

  public boolean isValid(String word) {
    return dictionary.contains(word.toUpperCase());
  }
}
