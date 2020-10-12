package sopra.view;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.Coordinate.ReadingComparator;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Player;

/**
 * Logs changing map during execution to {@link Logger}.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class RenderObserver implements Observer {

  private final Map<Coordinate, String> cache;
  private final Logger logger;
  private Coordinate camera;

  public RenderObserver(final Class<?> subclass) {
    this.logger = LoggerFactory.getLogger(subclass);
    this.cache = new HashMap<>(Config.CACHE_SIZE);
  }

  @Override
  public void notifyActNow() {
    // empty
  }

  @Override
  public void notifyChoiceWindow(final String json) {
    // empty
  }

  @Override
  public void notifyCommandFailed(final String message) {
    // empty
  }

  @Override
  public void notifyDrawWorld(final World world) {
    final Queue<Coordinate> coordinates =
        world.getPlayerPosition().cubeSpiral(Config.SIGHT_RADIUS).stream()
            .sorted(new ReadingComparator()).collect(Collectors.toCollection(ArrayDeque::new));
    coordinates.forEach(
        coordinate -> world.getTile(coordinate)
            .ifPresent(tile -> this.notifyUpdateWorld(tile, world)));
    this.render();
  }

  @Override
  public void notifyGameEnd(final boolean win) {
    this.logger.info("You have {}.", win ? "won" : "lost");
  }

  @Override
  public void notifyGameStarted() {
    // empty
  }

  @Override
  public void notifyMessage(final String message) {
    this.logger.info("{}", message);
  }

  @Override
  public void notifyNextCycle(final int cycle) {
    // empty
  }

  @Override
  public void notifyRegistrationAborted() {
    // empty
  }

  @Override
  public void notifySetCamera(final Coordinate coordinate) {
    this.cache.clear();
    this.camera = coordinate;
    this.logger.debug("camera {}", this.camera);
  }

  @Override
  public void notifyUpdatePlayer(final Player player) {
    // empty
  }

  @Override
  public void notifyUpdateWorld(final Tile tile) {
    this.cache.put(tile.getPosition(), tile.getRepresentation().replace('.', '_'));
    this.render();
  }

  private void notifyUpdateWorld(final Tile tile, final World world) {
    final Optional<Coordinate> destination = world.getDestination(tile.getPosition());
    if (destination.isPresent()) {
      final String representation =
          String.valueOf(world.getRoom(destination.get()).orElseThrow(ServerError::new).getLevel());
      this.cache.put(tile.getPosition(), representation);
    } else {
      this.cache.put(tile.getPosition(), tile.getRepresentation());
    }
  }

  /**
   * Some Java magic.
   */
  private void render() {
    if (this.cache.size() > 1) {
      this.logger.debug("   x {}", IntStream.rangeClosed(-Config.SIGHT_RADIUS, Config.SIGHT_RADIUS)
          .mapToObj(i -> String.format("%2d", Math.abs(this.camera.getX() + i) % 10))
          .collect(Collectors.joining()));
      final Map<Integer, List<Coordinate>> groups =
          this.camera.cubeSpiral(Config.SIGHT_RADIUS).stream()
              .collect(Collectors.groupingBy(Coordinate::getZ));
      int padding = groups.size() / 2;
      this.logger.debug("z {}{}", " ".repeat(padding + 3), " X".repeat(Config.SIGHT_RADIUS + 1));
      for (final Entry<Integer, List<Coordinate>> entry : groups.entrySet().stream()
          .sorted(Comparator.comparingInt(Entry::getKey)).collect(Collectors.toList())) {
        this.logger.debug("{} {}X {} X", String.format("%+03d", entry.getKey() % 100),
            " ".repeat(Math.abs(padding--)),
            entry.getValue().stream().sorted(Comparator.comparingInt(Coordinate::getX))
                .map(coordinate -> this.cache.getOrDefault(coordinate, " "))
                .collect(Collectors.joining(" ")));
      }
      this.logger
          .debug("{}{}", " ".repeat(Math.abs(padding) + 4), " X".repeat(Config.SIGHT_RADIUS + 1));
      this.logger.debug("   y {}", IntStream.rangeClosed(-Config.SIGHT_RADIUS, Config.SIGHT_RADIUS)
          .mapToObj(i -> String.format("%2d", Math.abs(this.camera.getY() + i) % 10))
          .collect(Collectors.joining()));
    }
  }
}

