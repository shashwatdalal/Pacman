package jpacman.engine.npc.ghost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;

/**
 * Navigation provides utility to nagivate on {@link Square}s.
 *
 * @author Jeroen Roosen
 */
public final class Navigation {

  private Navigation() {
  }

  /**
   * Calculates the shortest path. This is done by BFS. This search ensures
   * the traveller is allowed to occupy the squares on the way, or returns the
   * shortest path to the square regardless of terrain if no traveller is
   * specified.
   *
   * @param from The starting square.
   * @param to The destination.
   * @param traveller The traveller attempting to reach the destination. If traveller is set to
   * <code>null</code>, this method will ignore terrain and find the shortest path whether it can
   * actually be reached or not.
   * @return The shortest path to the destination or <code>null</code> if no such path could be
   * found. When the destination is the current square, an empty list is returned.
   */
  public static List<Direction> shortestPath(final Square from, final Square to,
      final Unit traveller) {
    return shortestPath(from, to, traveller, Collections.emptyList());
  }


  public static List<Direction> shortestPath(final Square from, final Square to,
      final Unit traveller, final Collection<Square> ignored) {
    if (from.equals(to)) {
      return new ArrayList<>();
    }

    final List<Node> targets = new ArrayList<>();
    final Set<Square> visited = new HashSet<>();
    visited.addAll(ignored);
    targets.add(new Node(null, from, null));
    while (!targets.isEmpty()) {
      final Node n = targets.remove(0);
      final Square s = n.getSquare();
      if (s.equals(to)) {
        return n.getPath();
      }
      visited.add(s);
      addNewTargets(traveller, targets, visited, n, s);
    }
    return null;
  }

  private static void addNewTargets(final Unit traveller, final List<Node> targets,
      final Set<Square> visited, final Node n, final Square s) {
    for (final Direction d : Direction.values()) {
      final Square target = s.getSquareAt(d);
      if (!visited.contains(target)
          && (traveller == null || target
          .isAccessibleTo(traveller))) {
        targets.add(new Node(d, target, n));
      }
    }
  }

  /**
   * Finds the nearest unit of the given type and returns its location. This
   * method will perform a breadth first search starting from the given
   * square.
   *
   * @param type The type of unit to search for.
   * @param currentLocation The starting location for the search.
   * @return The nearest unit of the given type, or <code>null</code> if no such unit could be
   * found.
   */
  public static Unit findNearest(final Class<? extends Unit> type,
      final Square currentLocation) {
    final List<Square> toDo = new ArrayList<>();
    final Set<Square> visited = new HashSet<>();

    toDo.add(currentLocation);

    while (!toDo.isEmpty()) {
      final Square square = toDo.remove(0);
      final Unit unit = findUnit(type, square);
      if (unit != null) {
        return unit;
      }
      visited.add(square);
      for (final Direction d : Direction.values()) {
        final Square newTarget = square.getSquareAt(d);
        if (!visited.contains(newTarget) && !toDo.contains(newTarget)) {
          toDo.add(newTarget);
        }
      }
    }
    return null;
  }

  /**
   * Determines whether a square has an occupant of a certain type.
   *
   * @param type The type to search for.
   * @param square The square to search.
   * @return A unit of type T, iff such a unit occupies this square, or <code>null</code> of none
   * does.
   */
  public static Unit findUnit(final Class<? extends Unit> type, final Square square) {
    for (final Unit u : square.getOccupants()) {
      if (type.isInstance(u)) {
        return u;
      }
    }
    return null;
  }

  /**
   * Helper class to keep track of the path.
   *
   * @author Jeroen Roosen
   */
  private static final class Node {

    /**
     * The direction for this node, which is <code>null</code> for the root
     * node.
     */
    private final Direction direction;

    /**
     * The parent node, which is <code>null</code> for the root node.
     */
    private final Node parent;

    /**
     * The square associated with this node.
     */
    private final Square square;

    /**
     * Creates a new node.
     *
     * @param d The direction, which is <code>null</code> for the root node.
     * @param s The square.
     * @param p The parent node, which is <code>null</code> for the root node.
     */
    Node(final Direction d, final Square s, final Node p) {
      this.direction = d;
      this.square = s;
      this.parent = p;
    }

    /**
     * @return The direction for this node, or <code>null</code> if this node is a root node.
     */
    private Direction getDirection() {
      return this.direction;
    }

    /**
     * @return The square for this node.
     */
    private Square getSquare() {
      return this.square;
    }

    /**
     * @return The parent node, or <code>null</code> if this node is a root node.
     */
    private Node getParent() {
      return this.parent;
    }

    /**
     * Returns the list of values from the root of the tree to this node.
     *
     * @return The list of values from the root of the tree to this node.
     */
    private List<Direction> getPath() {
      if (getParent() == null) {
        return new ArrayList<>();
      }
      final List<Direction> path = this.parent.getPath();
      path.add(getDirection());
      return path;
    }
  }
}
