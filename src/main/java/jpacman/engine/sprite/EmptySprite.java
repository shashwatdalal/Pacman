package jpacman.engine.sprite;

import java.awt.Graphics;

/**
 * Empty Sprite which does not contain any data. When this sprite is drawn,
 * nothing happens.
 *
 * @author Jeroen Roosen
 */
public class EmptySprite implements Sprite {

  @Override
  public void draw(final Graphics g, final int x, final int y, final int width, final int height) {
    // nothing to draw.
  }

  @Override
  public Sprite split(final int x, final int y, final int width, final int height) {
    return new EmptySprite();
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

}
