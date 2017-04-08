package jpacman.concurrent;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import jpacman.concurrent.ghosts.Event;
import jpacman.engine.board.Direction;
import jpacman.engine.game.SinglePlayerGame;

/**
 * Created by Shashwat on 04-Mar-17.
 */
public class PacmanMessageBus {

  private static final PacmanMessageBus INSTANCE
      = new PacmanMessageBus();
  private static volatile GameView gameView;
  private static volatile SinglePlayerGame singlePlayerGame;
  private static Deque<Event> blockingDeque =
      new LinkedBlockingDeque<>();

  private PacmanMessageBus() {
  }

  public static PacmanMessageBus getInstance() {
    return INSTANCE;
  }

  public static void setGameView(GameView newGameView) {
    gameView = newGameView;
  }

  public static GameView getGameView() {
    return gameView;
  }

  public static SinglePlayerGame getSinglePlayerGame() {
    return singlePlayerGame;
  }

  public static void setSinglePlayerGame(SinglePlayerGame newSinglePlayerGame) {
    singlePlayerGame = newSinglePlayerGame;
  }

  public static void pushToBlockingDeque(Event event) {
    System.out.println(event);
    blockingDeque.push(event);
  }

  public static Event pollFromBlockingDeque() {
    try {
      return blockingDeque.poll();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public static void resestBlockingDeque() {
    blockingDeque.clear();
  }

}