import java.io.InputStream;

/**
 * The entrance class for the ScrabbleCheater project.
 */
public class Main {
  // How many moves to display
  private final static int NUM_MOVES = 5;
  // The resource paths for the files (relative to the /src/main/resources folder)
  private final static String dictResourcePath = "dictionaries/scrabble.txt";
  private final static String letterBoardResourcePath = "letterBoards/scrabble3.csv";
  private final static String multiplierBoardResourcePath = "multiplierBoards/scrabble.csv";
  private final static String scoreResourcePath = "letterScores.csv";

  /**
   * The main function for the ScrabbleCheater project.
   *
   * @param args The arguments passed into the function.
   */
  public static void main(String[] args) {
    // Choose the type of board to get the best move for
    InputStream dictSource = Main.class.getResourceAsStream(dictResourcePath);
    InputStream letterBoardSource = Main.class.getResourceAsStream(letterBoardResourcePath);
    InputStream multiplierBoardSource = Main.class.getResourceAsStream(multiplierBoardResourcePath);
    InputStream scoreSource = Main.class.getResourceAsStream(scoreResourcePath);

    Dictionary d;
    try {
      d = new Dictionary(dictSource);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    Board b;
    try {
      b = new Board(letterBoardSource, multiplierBoardSource);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    LetterScorer s;
    try {
      s = new LetterScorer(scoreSource);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    System.out.println(b.toString());

    Solver solver = new Solver(b, d, s);

    // Get and print out the moves
    for (Move m : solver.bestMoves(NUM_MOVES)) {
      System.out.println(m.toString());
    }
  }
}
