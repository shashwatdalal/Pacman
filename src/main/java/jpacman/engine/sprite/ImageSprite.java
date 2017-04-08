package jpacman.engine.sprite;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Basic implementation of a Sprite, it merely consists of a static image.
 *
 * @author Jeroen Roosen
 */
public class ImageSprite implements Sprite {

  /**
   * Internal image.
   */
  private final Image image;

  /**
   * Creates a new sprite from an image.
   *
   * @param img The image to create a sprite from.
   */
  public ImageSprite(final Image img) {
    this.image = img;
  }

  @Override
  public void draw(final Graphics g, final int x, final int y, final int width, final int height) {
    g.drawImage(this.image, x, y, x + width, y + height, 0, 0,
        this.image.getWidth(null), this.image.getHeight(null), null);
  }

  @Override
  public Sprite split(final int x, final int y, final int width, final int height) {
    if (withinImage(x, y) && withinImage(x + width - 1, y + height - 1)) {
      final BufferedImage newImage = newImage(width, height);
      newImage.createGraphics().drawImage(this.image, 0, 0, width, height, x,
          y, x + width, y + height, null);
      return new ImageSprite(newImage);
    }
    return new EmptySprite();
  }

  private boolean withinImage(final int x, final int y) {
    return x < this.image.getWidth(null) && x >= 0 && y < this.image.getHeight(null)
        && y >= 0;
  }

  /**
   * Creates a new, empty image of the given width and height. Its
   * transparency will be a bitmask, so no try ARGB image.
   *
   * @param width The width of the new image.
   * @param height The height of the new image.
   * @return The new, empty image.
   */
  private BufferedImage newImage(final int width, final int height) {
    final GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getDefaultScreenDevice()
        .getDefaultConfiguration();
    return gc.createCompatibleImage(width, height, Transparency.BITMASK);
  }

  @Override
  public int getWidth() {
    return this.image.getWidth(null);
  }

  @Override
  public int getHeight() {
    return this.image.getHeight(null);
  }

}
