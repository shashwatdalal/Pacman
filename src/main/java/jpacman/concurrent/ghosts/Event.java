package jpacman.concurrent.ghosts;

import jpacman.concurrent.BaseCharActor;
import jpacman.engine.board.Direction;

/**
 * Created by Shashwat on 05-Mar-17.
 */
public class Event {

  private final BaseCharActor ACTOR;
  private final Direction DIRECTION;

  public Event(BaseCharActor ACTOR, Direction DIRECTION) {
    this.ACTOR = ACTOR;
    this.DIRECTION = DIRECTION;
  }

  public BaseCharActor getACTOR() {
    return ACTOR;
  }

  public Direction getDIRECTION() {
    return DIRECTION;
  }

  @Override
  public String toString() {
    return ACTOR.toString() + " " + getDIRECTION();
  }
}
