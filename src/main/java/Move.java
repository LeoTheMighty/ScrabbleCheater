/**
 * Represents an individual move in Scrabble, where you place a number of tiles at a certain
 * location on the board in a direction. Also stores the main word and score information after
 * it has been processed.
 */
public class Move implements Comparable<Move> {
  private String word;
  private String placedLetters;
  private int score;
  private int r, c;
  private boolean horizontal;

  /**
   * The default constructor for the Move class.
   *
   * @param placedLetters The letters to place on the board, skipping the already placed spaces.
   * @param r The row to start the word at.
   * @param c The column to start the words at.
   * @param horizontal Whether it is being placed vertically or horizontally.
   */
  public Move(String placedLetters, int r, int c, boolean horizontal) {
    this.placedLetters = placedLetters;
    this.word = null;
    this.score = -1;
    this.r = r;
    this.c = c;
    this.horizontal = horizontal;
  }

  public int getR() {
    return r;
  }

  public int getC() {
    return c;
  }

  public boolean isHorizontal() {
    return horizontal;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getPlacedLetters() {
    return placedLetters;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int compareTo(Move m) {
    return Integer.compare(getScore(), m.getScore());
  }

  @Override
  public String toString() {
    return String.format("Move(%s, %d) at (%d, %d) [%s]", word, score, c, r, horizontal ? "horizontally" : "vertically");
  }
}
