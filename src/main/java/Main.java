import java.io.InputStream;

/**
 *
 */
public class Main {
  // How many moves to display
  final static int NUM_MOVES = 5;
  final static String dictResourcePath = "dictionaries/scrabble.txt";
  final static String letterBoardResourcePath = "letterBoards/scrabble3.csv";
  final static String multiplierBoardResourcePath = "multiplierBoards/scrabble.csv";
  final static String scoreResourcePath = "letterScores.csv";

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

    // Add the letters
    Solver solver = new Solver(b, d, s);

    // Get the answer
    for (Move m : solver.bestMoves(NUM_MOVES)) {
      System.out.println(m.toString());
    }
  }
}
