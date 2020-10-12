package sopra.model;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.json.JSONObject;
import sopra.comm.Direction;
import sopra.utils.Hashable;
import sopra.utils.Utils;

/**
 * Representation of a three-dimensional coordinate.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Coordinate implements Hashable {

  private static final EnumMap<Direction, Coordinate> OFFSETS = new EnumMap<>(Direction.class);

  static {
    OFFSETS.put(Direction.NORTH_EAST, new Coordinate(1, 0, -1));
    OFFSETS.put(Direction.EAST, new Coordinate(1, -1, 0));
    OFFSETS.put(Direction.SOUTH_EAST, new Coordinate(0, -1, 1));
    OFFSETS.put(Direction.SOUTH_WEST, new Coordinate(-1, 0, 1));
    OFFSETS.put(Direction.WEST, new Coordinate(-1, 1, 0));
    OFFSETS.put(Direction.NORTH_WEST, new Coordinate(0, 1, -1));
  }

  private final int[] tuple;

  public Coordinate(final int x, final int y, final int z) {
    this.tuple = new int[]{
        x,
        y,
        z
    };
  }

  private Coordinate(final Coordinate coordinate) {
    this.tuple = coordinate.tuple.clone();
  }

  private static Direction clockwise(final Direction direction) {
    return Direction.values()[(direction.ordinal() + 1) % Direction.values().length];
  }

  public static Coordinate fromJson(final JSONObject root) {
    return new Coordinate(root.getInt("x"), root.getInt("y"), root.getInt("z"));
  }

  private Coordinate apply(final Function<Integer, Integer> function) {
    return new Coordinate(function.apply(this.tuple[0]), function.apply(this.tuple[1]),
        function.apply(this.tuple[2]));
  }

  private Coordinate apply(final BiFunction<Integer, Integer, Integer> function,
      final Coordinate coordinate) {
    return new Coordinate(function.apply(this.tuple[0], coordinate.tuple[0]),
        function.apply(this.tuple[1], coordinate.tuple[1]),
        function.apply(this.tuple[2], coordinate.tuple[2]));
  }

  /**
   * Get {@link Coordinate} in direction with given distance.
   *
   * @param direction direction of target
   * @param distance  distance to target
   *
   * @return new {@link Coordinate}
   */
  public Coordinate computeCoordinate(final Direction direction, final int distance) {
    return new Coordinate(
        this.apply(Integer::sum, OFFSETS.get(direction).apply(i -> i * distance)));
  }

  /**
   * Get neighbouring {@link Coordinate} in direction.
   *
   * @param direction direction of target
   *
   * @return new {@link Coordinate}
   */
  public Coordinate computeCoordinate(final Direction direction) {
    return this.computeCoordinate(direction, 1);
  }

  /**
   * Calculates a Ring of Coordinates with this {@link Coordinate} as center.
   *
   * @param radius radius of ring
   *
   * @return queue of coordinates
   */
  public Queue<Coordinate> cubeRing(final int radius) {
    if (radius < 0) {
      throw new IllegalArgumentException("Radius must not be negative.");
    }
    final Queue<Coordinate> coordinates = new ArrayDeque<>();
    if (radius > 0) {
      Coordinate cube = this;
      Direction direction = Direction.SOUTH_EAST;
      for (int i = 0; i < radius; i++) {
        cube = cube.computeCoordinate(Direction.NORTH_EAST);
      }
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < radius; j++) {
          coordinates.add(cube);
          cube = cube.computeCoordinate(direction);
        }
        direction = clockwise(direction);
      }
    } else {
      coordinates.add(new Coordinate(this));
    }
    return coordinates;
  }

  /**
   * Uses {@link Coordinate#cubeRing(int)} to calculate spiral around center.
   *
   * @param radius radius of spiral
   *
   * @return queue of  coordinates in spiral ordering
   */
  public Queue<Coordinate> cubeSpiral(final int radius) {
    final Queue<Coordinate> coordinates = new ArrayDeque<>();
    IntStream.rangeClosed(0, radius).mapToObj(this::cubeRing).forEach(coordinates::addAll);
    return coordinates;
  }

  /**
   * Calculate distance between two {@link Coordinate}s.
   *
   * @param coordinate other {@link Coordinate}
   *
   * @return distance
   */
  public int distance(final Coordinate coordinate) {
    return Math.max(Math.abs(this.tuple[0] - coordinate.tuple[0]),
        Math.max(Math.abs(this.tuple[1] - coordinate.tuple[1]),
            Math.abs(this.tuple[2] - coordinate.tuple[2])));
  }

  public int getX() {
    return this.tuple[0];
  }

  public int getY() {
    return this.tuple[1];
  }

  public int getZ() {
    return this.tuple[2];
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tuple[0], this.tuple[1], this.tuple[2]);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    final Coordinate coordinate = (Coordinate) obj;
    return Objects.equals(this.tuple[0], coordinate.tuple[0]) && Objects
        .equals(this.tuple[1], coordinate.tuple[1]) && Objects
        .equals(this.tuple[2], coordinate.tuple[2]);
  }

  @Override
  public String toString() {
    return Utils.substitute("({}, {}, {})", this.tuple[0], this.tuple[1], this.tuple[2]);
  }

  public JSONObject toJSON() {
    final JSONObject root = new JSONObject();
    root.put("x", this.tuple[0]);
    root.put("y", this.tuple[1]);
    root.put("z", this.tuple[2]);
    return root;
  }

  /**
   * {@link Comparator} on {@link Coordinate}s to sort from left to right and top down.
   */
  public static class ReadingComparator implements Comparator<Coordinate> {

    @Override
    public int compare(final Coordinate a, final Coordinate b) {
      return a.tuple[2] == b.tuple[2] ? Integer.compare(a.tuple[0], b.tuple[0]) :
          Integer.compare(a.tuple[2], b.tuple[2]);
    }
  }
}
