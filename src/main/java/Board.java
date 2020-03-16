import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 */
public class Board {
  // The board to hold the info
  private int width;
  private int height;
  private String tiles = null;
  private Character[][] letterBoard;
  private Multiplier[][] multiplierBoard;
  private Map<Character, Integer> letterScores;

  // Represents an empty spot on the board
  static final char emptyCharacter = ' ';

  public Board() {}

  public Board(InputStream letterBoardSource, InputStream multiplierBoardSource) {
    this(multiplierBoardSource);
    Scanner sc = new Scanner(letterBoardSource);
    tiles = sc.nextLine().trim();
    int h = 0;
    while (sc.hasNextLine()) {
      int w = 0;
      for (String letter : (" " + sc.nextLine() + " ").split(",")) {
        letter = letter.trim().toUpperCase();
        if (letter.length() > 1) {
          throw new IllegalStateException("Board file malformed: letter value too long");
        }
        char c = letter.length() == 0 ? ' ' : letter.charAt(0);
        if (c < 'A' || c > 'Z') {
          // Default to the empty character
          c = emptyCharacter;
        }
        setLetter(c, w, h);
        w++;
      }
      if (width != w) {
        throw new IllegalStateException(
                String.format("Board file malformed: row %d has an incorrect # of columns (is %d, should be %d)", h, w, width));
      }
      h++;
    }
    if (height != h) {
      throw new IllegalStateException("Board file malformed: file has incorrect number of columns");
    }
  }

  /**
   * Initializes a board from the board file.
   *
   * @param multiplierBoardSource The source for the multiplier board.
   */
  public Board(InputStream multiplierBoardSource) {
    Scanner sc = new Scanner(multiplierBoardSource);
    String[] dimensions = sc.nextLine().split(",");
    width = Integer.parseInt(dimensions[0].trim());
    height = Integer.parseInt(dimensions[1].trim());
    letterBoard = new Character[height][width];
    multiplierBoard = new Multiplier[height][width];
    int h = 0;
    while (sc.hasNextLine()) {
      int w = 0;
      for (String multiplier : sc.nextLine().split(",")) {
        setMultiplier(Multiplier.safeGetValue(multiplier.trim()), w, h);
        setLetter(emptyCharacter, w, h);
        w++;
      }
      if (width != w) {
        throw new IllegalStateException("Board file malformed: a row has an incorrect # of rows");
      }
      h++;
    }
    if (height != h) {
      throw new IllegalStateException("Board file malformed: file has incorrect number of columns");
    }
  }

  /**
   * Creates a stack with the tiles given in the string.
   *
   * @param tileString The string representation of the tiles.
   * @return The new stack representing the tiles.
   */
  private Stack<Character> createTileStack(String tileString) {
    // Create a stack to hold the tiles
    Stack<Character> tiles = new Stack<>();
    char[] letters = tileString.toCharArray();
    // backwards because that's how stacks work
    for (int i = letters.length - 1; i >= 0; i--) {
      tiles.push(letters[i]);
    }
    return tiles;
  }

  private int wordScore(String placedLetters, int r, int c, boolean horizontal, Dictionary d, LetterScorer scorer) {
    int score = 0;
    int wordMultiplier = 1;

    // Create a stack to hold the tiles
    Stack<Character> tiles = createTileStack(placedLetters);

    StringBuilder finalWord = new StringBuilder();
    boolean wordContinues = true;
    boolean moveValid = true;
    while (wordContinues) {
      boolean isLetterPlaced = false;
      Multiplier m = getMultiplier(c, r);
      Character l = getLetter(c, r);
      if (l == emptyCharacter) {
        // The word is done if we're at an empty character
        // and have no more tiles to place
        if (tiles.empty()) {
          break;
        }
        // Take from the placed letters
        l = tiles.pop();
        isLetterPlaced = true;
      }

      // Add to the score
      int letterScore = scorer.score(l);
      if (isLetterPlaced) {
        switch (m) {
          case L2:
            letterScore *= 2;
            break;
          case L3:
            letterScore *= 3;
            break;
          case W2:
            wordMultiplier *= 2;
            break;
          case W3:
            wordMultiplier *= 3;
            break;
        }
      }
      score += letterScore;

      // l is part of the word we're evaluating.
      finalWord.append(l);

      // Continue on the board
      if (horizontal) {
        c++;
        if (c >= width) {
          wordContinues = false;
          moveValid = tiles.empty();
        }
      }
      else {
        r++;
        if (r >= height) {
          wordContinues = false;
          moveValid = tiles.empty();
        }
      }
    }
    // Make sure the move was valid
    // 1. check that we used all the tiles and 2. check that the word exists.
    if (!moveValid || !d.isValid(finalWord.toString())) {
      return -1;
    }
    return score * wordMultiplier;
  }

  /**
   * Returns either the y or x position of the adjacent word (for horizontal and not horizontal
   * respectively). Returns -1 if no adjacent word.
   *
   * TODO this took way too long to write, I might have been a little bit too tricky here.
   *
   * @param r
   * @param c
   * @param horizontal
   * @return
   */
  private int adjacentWordIndex(int r, int c, boolean horizontal) {
    // We're looking for a perpendicular adjacent word
    int startIndex = horizontal ? r : c;
    // Keep decreasing the startIndex until we hit the edge or a space
    while (startIndex - 1 >= 0) {
      startIndex--;
      if (getLetter(horizontal ? c : startIndex, horizontal ? startIndex : r) == emptyCharacter) {
        startIndex++;
        break;
      }
    }

    // If we didn't move the start index
    if (startIndex == (horizontal ? r : c)) {
      // Then if the end index also doesn't move, then there is no adjacent word
      // If the end index does move then there is an adjacent word!
      int nextIndex = startIndex + 1;
      if (nextIndex >= (horizontal ? width : height) ||
              getLetter(horizontal ? c : nextIndex, horizontal ? nextIndex : r) == emptyCharacter) {
          return -1;
      }
    }

    return startIndex;
  }

  /**
   *
   *
   * @param m
   * @return If the move is valid.
   */
  public boolean updateMoveScore(Move m, Dictionary dictionary, LetterScorer scorer) {
    // First get the actual first
    // Update the move object with the score and the main word.
    int totalScore = 0;
    StringBuilder mainWord = new StringBuilder();
    int r = m.getR();
    int c = m.getC();
    boolean horizontal = m.isHorizontal();

    // First compute the mainWordScore
    int mainWordScore = wordScore(m.getPlacedLetters(), r, c, horizontal, dictionary, scorer);
    if (mainWordScore == -1) {
      return false;
    }
    totalScore += mainWordScore;

    // Create a stack to hold the tiles
    Stack<Character> tiles = createTileStack(m.getPlacedLetters());

    boolean hasAdjacent = false;
    boolean wordContinues = true;
    while (wordContinues) {
      boolean isLetterPlaced = false;
      Character l = getLetter(c, r);
      if (l == emptyCharacter) {
        // The word is done if we're at an empty character
        // and have no more tiles to place
        if (tiles.empty()) {
          break;
        }
        // Take from the placed letters
        l = tiles.pop();
        isLetterPlaced = true;
      }

      // Find a possible adjacent word if we placed a new letter here
      int startIndex = adjacentWordIndex(r, c, horizontal);
      if (startIndex != -1) {
        hasAdjacent = true;
        if (isLetterPlaced) {
          int adjacentScore;
          if (horizontal) {
            adjacentScore = wordScore(String.valueOf(l), startIndex, c, false, dictionary, scorer);
          }
          else {
            adjacentScore = wordScore(String.valueOf(l), r, startIndex, true, dictionary, scorer);
          }
          if (adjacentScore == -1) {
            return false;
          }
          totalScore += adjacentScore;
        }
      }

      mainWord.append(l);

      // Continue on the board
      if (horizontal) {
        c++;
        if (c >= width) {
          wordContinues = false;
        }
      }
      else {
        r++;
        if (r >= height) {
          wordContinues = false;
        }
      }
    }

    // Update the move fields
    m.setScore(totalScore);
    m.setWord(mainWord.toString());

    return hasAdjacent;
  }

  public Board deepCopy() {
    Board newBoard = new Board();
    newBoard.width = width;
    newBoard.height = height;
    newBoard.letterBoard = new Character[height][width];
    newBoard.multiplierBoard = new Multiplier[height][width];

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        newBoard.setMultiplier(getMultiplier(c, r), c, r);
        newBoard.setLetter(getLetter(c, r), c, r);
      }
    }

    return newBoard;
  }

  public String getTiles() {
    return tiles;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void setLetter(Character ch, int x, int y) {
    letterBoard[y][x] = new Character(ch);
  }

  public Character getLetter(int x, int y)  {
    return letterBoard[y][x];
  }

  private void setMultiplier(Multiplier m, int x, int y) {
    multiplierBoard[y][x] = m;
  }

  public Multiplier getMultiplier(int x, int y) {
    return multiplierBoard[y][x];
  }

  public String multiplierBoardString() {
    String border = "#";
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < width + 2; i++) {
      sb.append(border);
    }
    sb.append("\n");
    for (int r = 0; r < height; r++) {
      sb.append(border);
      for (int c = 0; c < width; c++) {
        sb.append(getMultiplier(c, r));
      }
      sb.append(border);
      sb.append("\n");
    }
    for (int i = 0; i < width + 2; i++) {
      sb.append(border);
    }
    sb.append("\n");

    return sb.toString();
  }

  public String letterBoardString() {
    String border = "#";
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < width + 2; i++) {
      sb.append(border);
    }
    sb.append("\n");
    for (int r = 0; r < height; r++) {
      sb.append(border);
      for (int c = 0; c < width; c++) {
        sb.append(getLetter(c, r));
      }
      sb.append(border);
      sb.append("\n");
    }
    for (int i = 0; i < width + 2; i++) {
      sb.append(border);
    }
    sb.append("\n");

    return sb.toString();
  }

  @Override
  public String toString() {
    return letterBoardString();
  }

  /**
   * Enum representing all the possible values for a multiplier on the board.
   */
  private enum Multiplier {
    N,  // normal
    L2, // 2x Letter
    L3, // 3x Letter
    W2, // 2x Word
    W3,;  // 3x Word

    /**

     * Gets the Multiplier value from a string with a default Multiplier when it doesn't default to
     * anything.
     *
     * @param multiplier The string value for the multiplier.
     * @return The Multiplier enum value from the multiplier.
     */
    static Multiplier safeGetValue(String multiplier) {
      try {
        return Multiplier.valueOf(multiplier.toUpperCase());
      }
      catch (IllegalArgumentException e) {
        return N;
      }
    }
  }
}
