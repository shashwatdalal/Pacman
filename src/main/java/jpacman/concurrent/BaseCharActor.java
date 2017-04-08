package jpacman.concurrent;

import java.sql.PseudoColumnUsage;

/**
 * Created by mateus on 20/02/17.
 */
public abstract class BaseCharActor implements Runnable {

  private final long actionSleepInterval;
  protected GameView gameView;
  protected PacmanMessageBus pmb = PacmanMessageBus.getInstance();


  public BaseCharActor(final long actionSleepInterval) {
    this.actionSleepInterval = actionSleepInterval;
  }

  @Override
  public void run() {
    do {
      try {
        Thread.sleep(actionSleepInterval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gameView = PacmanMessageBus.getGameView();
      doAction();
    }
    while (pmb.getSinglePlayerGame().isInProgress());
  }

  // Feel free to change the signature of this method!
  protected abstract void doAction();
}
