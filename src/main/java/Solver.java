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

  public Solver(Board board, Dictionary dictionary, LetterScorer scorer) {
    // Do some pre-caching?
    this.board = board;
    this.dictionary = dictionary;
    this.scorer = scorer;
  }

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

          // TODO Skip if it's impossible to be valid
          // Can't start it here if there is something before
          if ((horizontal ? c : r) > 0
                  && board.getLetter(horizontal ? c - 1 : c, horizontal ? r : r - 1) != Board.emptyCharacter) {
            continue;
          }

          System.out.println(String.format("Checking (%d, %d, %b)", r, c, horizontal));

          for (String placeTiles : tilePermutations) {
            // If the placing of the tiles will definitely go over, don't do it
            if (r == 4 && c == 0 && !horizontal && placeTiles.equals("VIOlINS")) {
              System.out.println("HERE");
            }
            if (placeTiles.length() + (horizontal ? c : r) >= (horizontal ? width : height)) {
              continue;
            }
            if (r == 4 && c == 0 && !horizontal && placeTiles.equals("VIOlINS")) {
              System.out.println("HERE");
            }
            Move m = new Move(placeTiles, r, c, horizontal);
            if (board.updateMoveScore(m, dictionary, scorer)) {
              moves.add(m);
            }
          }
        }
      }
    }

    return moves.subList(0, numMoves);
  }

  private String swap(String s, int i, int j) {
    char temp;
    char[] charArray = s.toCharArray();
    temp = charArray[i];
    charArray[i] = charArray[j];
    charArray[j] = temp;
    return String.valueOf(charArray);
  }

  private Set<String> permute(String str) {
    return permute(str, 0, str.length() - 1);
  }

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
