public class Move implements Comparable<Move> {
  private String word;
  private String placedLetters;
  private int score;
  private int r, c;
  private boolean horizontal;

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
