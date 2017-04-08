package jpacman.engine.sprite;

import java.awt.Graphics;

/**
 * Animated sprite, renders the frame depending on the time of requesting the
 * draw.
 *
 * @author Jeroen Roosen
 */
public class AnimatedSprite implements Sprite {

  /**
   * Static empty sprite to serve as the end of a non-looping sprite.
   */
  private static final Sprite END_OF_LOOP = new EmptySprite();

  /**
   * The animation itself, in frames.
   */
  private final Sprite[] animationFrames;

  /**
   * The delay between frames.
   */
  private final int animationDelay;

  /**
   * Whether is animation should be looping or not.
   */
  private final boolean looping;

  /**
   * The index of the current frame.
   */
  private int current;

  /**
   * Whether this sprite is currently animating or not.
   */
  private boolean animating;

  /**
   * The {@link System#currentTimeMillis()} stamp of the last update.
   */
  private long lastUpdate;

  /**
   * Creates a new animating sprite that will change frames every interval. By
   * default the sprite is not animating.
   *
   * @param frames The frames of this animation.
   * @param delay The delay between frames.
   * @param loop Whether or not this sprite should be looping.
   */
  public AnimatedSprite(final Sprite[] frames, final int delay, final boolean loop) {
    this(frames, delay, loop, false);
  }

  /**
   * Creates a new animating sprite that will change frames every interval.
   *
   * @param frames The frames of this animation.
   * @param delay The delay between frames.
   * @param loop Whether or not this sprite should be looping.
   * @param isAnimating Whether or not this sprite is animating from the start.
   */
  public AnimatedSprite(final Sprite[] frames, final int delay, final boolean loop,
      final boolean isAnimating) {
    assert frames.length > 0;

    this.animationFrames = frames.clone();
    this.animationDelay = delay;
    this.looping = loop;
    this.animating = isAnimating;

    this.current = 0;
    this.lastUpdate = System.currentTimeMillis();
  }

  /**
   * @return The frame of the current index.
   */
  private Sprite currentSprite() {
    Sprite result = END_OF_LOOP;
    if (this.current < this.animationFrames.length) {
      result = this.animationFrames[this.current];
    }
    assert result != null;
    return result;
  }

  /**
   * Starts or stops the animation of this sprite.
   *
   * @param isAnimating <code>true</code> to animate this sprite or <code>false</code> to stop
   * animating this sprite.
   */
  public void setAnimating(final boolean isAnimating) {
    this.animating = isAnimating;
  }

  /**
   * (Re)starts the current animation.
   */
  public void restart() {
    this.current = 0;
    this.lastUpdate = System.currentTimeMillis();
    setAnimating(true);
  }

  @Override
  public void draw(final Graphics g, final int x, final int y, final int width, final int height) {
    update();
    currentSprite().draw(g, x, y, width, height);
  }

  @Override
  public Sprite split(final int x, final int y, final int width, final int height) {
    update();
    return currentSprite().split(x, y, width, height);
  }

  /**
   * Updates the current frame index depending on the current system time.
   */
  private void update() {
    final long now = System.currentTimeMillis();
    if (this.animating) {
      while (this.lastUpdate < now) {
        this.lastUpdate += this.animationDelay;
        this.current++;
        if (this.looping) {
          this.current %= this.animationFrames.length;
        } else if (this.current == this.animationFrames.length) {
          this.animating = false;
        }
      }
    } else {
      this.lastUpdate = now;
    }
  }

  @Override
  public int getWidth() {
    assert currentSprite() != null;
    return currentSprite().getWidth();
  }

  @Override
  public int getHeight() {
    assert currentSprite() != null;
    return currentSprite().getHeight();
  }

}
