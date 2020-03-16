import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that handles solving the best solution for a board.
 */
public class Solver {
  private Board board;
  private Dictionary dictionary;
  private LetterScorer scorer;

  /**
   * Initializes the Solver with the board, dictionary, and scorer.
   *
   * @param board The board to solve for.
   * @param dictionary The dictionary of words to use.
   * @param scorer The scorer to give the letters scores.
   */
  public Solver(Board board, Dictionary dictionary, LetterScorer scorer) {
    // TODO Do some pre-caching here?
    this.board = board;
    this.dictionary = dictionary;
    this.scorer = scorer;
  }

  /**
   * Gets a list of the best moves to do for this board and tileset configuration.
   *
   * @param numMoves The amount of best moves to get.
   * @return The list of the numMoves best moves.
   */
  public List<Move> bestMoves(int numMoves) {
    List<Move> moves = new ArrayList<Move>() {
      public boolean add(Move m) {
        int i = 0;
        for (; i < super.size(); i++) {
          if (m.getScore() > ((Move)super.get(i)).getScore()) {
            break;
          }
        }
        super.add(i, m);
        return true;
      }
    };

    Set<String> tilePermutations = getTilePermutations(board.getTiles());

    int height = board.getHeight();
    int width = board.getWidth();
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        for (int h = 0; h < 2; h++)  {
          boolean horizontal = h == 0;

          // Can't start it here if there is something before
          if ((horizontal ? c : r) > 0
                  && board.getLetter(horizontal ? c - 1 : c, horizontal ? r : r - 1) != Board.emptyCharacter) {
            continue;
          }

          for (String placeTiles : tilePermutations) {
            // If the placing of the tiles will definitely go over, don't do it
            if (placeTiles.length() + (horizontal ? c : r) >= (horizontal ? width : height)) {
              continue;
            }
            Move m = new Move(placeTiles, r, c, horizontal);
            // returns whether the move is valid
            if (board.updateMoveScore(m, dictionary, scorer)) {
              moves.add(m);
            }
          }
        }
      }
    }

    // probably a better way of doing this...
    return moves.subList(0, numMoves);
  }

  /**
   * Gets a String with the ith and the jth indexes swapped in the given String.
   *
   * @param s The string to swap characters of.
   * @param i The index of the first character.
   * @param j The index of the second character.
   * @return The String with the swapped characters.
   */
  private String swap(String s, int i, int j) {
    char temp;
    char[] charArray = s.toCharArray();
    temp = charArray[i];
    charArray[i] = charArray[j];
    charArray[j] = temp;
    return String.valueOf(charArray);
  }

  /**
   * Helper function to start the permute recursive function.
   *
   * @param str The string to get the string permutations for.
   * @return The set of permutations for the string.
   */
  private Set<String> permute(String str) {
    return permute(str, 0, str.length() - 1);
  }

  /**
   * Gets the set of all possible permutations in a string.
   *
   * @param str The string to get the permutations for.
   * @param l The left index of the last unlocked character for permuting.
   * @param r The right index of the last unlocked character for permuting.
   * @return The set of all the possible permutations in a string given the locked and unlocked
   *         indices.
   */
  private Set<String> permute(String str, int l, int r) {
    if (l == r) {
      Set<String> set = new HashSet<>();
      for (int i = 1; i <= str.length(); i++) {
        set.add(str.substring(0, i));
      }
      return set;
    }
    else {
      Set<String> set = new HashSet<>();
      for (int i = l; i <= r; i++) {
        set.addAll(permute(swap(str, l, i), l+1, r));
      }
      return set;
    }
  }

  /**
   *  Gets all the possible orders to place tiles on the board, including permutations and wildcard
   *  logic.
   * @param tiles The string representation of tile set.
   * @return The set of string representations for ways to place tiles.
   */
  public Set<String> getTilePermutations(String tiles) {
    Set<String> permutations = new HashSet<>();
    char[] chars = tiles.toUpperCase().toCharArray();
    boolean hasWildcard = false;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '?') {
        hasWildcard = true;
        // lower case means that this letter gets no points
        for (char w = 'a'; w <= 'z'; w++) {
          chars[i] = w;
          permutations.addAll(permute(new String(chars)));
        }
      }
    }
    if (!hasWildcard) {
      permutations = permute(tiles);
    }

    return permutations;
  }
}
