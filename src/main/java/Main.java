import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 */
public class Main {
  public static void main(String[] args) {
    // Choose the type of board to get the best move for
    String dictResourcePath = "dictionaries/scrabble.txt";
    String letterBoardResourcePath = "letterBoards/scrabble3.csv";
    String multiplierBoardResourcePath = "multiplierBoards/scrabble.csv";
    String scoreResourcePath = "letterScores.csv";

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
    for (Move m : solver.bestMoves(5)) {
      System.out.println(m.toString());
    }
  }
}
