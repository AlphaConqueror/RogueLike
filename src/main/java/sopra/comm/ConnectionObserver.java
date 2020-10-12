package sopra.comm;

import java.util.Objects;
import java.util.Optional;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import sopra.Main;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.controller.command.Command;
import sopra.model.Coordinate;
import sopra.model.Coordinate.ReadingComparator;
import sopra.model.Room;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Player;

/**
 * We use a observer instead of the {@link ServerConnection}.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class ConnectionObserver implements Observer {

  private final Schema choiceSchema;
  private final ServerConnection<Command> connection;
  private final Schema playerSchema;
  private final Schema updateSchema;

  public ConnectionObserver(final ServerConnection<Command> connection) {
    this.connection = connection;
    this.choiceSchema = SchemaLoader.load(new JSONObject(
        new JSONTokener(Objects
            .requireNonNull(
                ConnectionObserver.class.getClassLoader()
                    .getResourceAsStream("schema/choice.json")))));
    this.playerSchema = SchemaLoader.load(new JSONObject(
        new JSONTokener(Objects
            .requireNonNull(
                ConnectionObserver.class.getClassLoader()
                    .getResourceAsStream("schema/player.json")))));
    this.updateSchema = SchemaLoader.load(new JSONObject(
        new JSONTokener(Objects
            .requireNonNull(
                ConnectionObserver.class.getClassLoader()
                    .getResourceAsStream("schema/update.json")))));
  }

  @Override
  public void notifyActNow() {
    this.connection.sendActNow();
  }

  @Override
  public void notifyChoiceWindow(final String json) {
    this.choiceSchema.validate(new JSONObject(json));
    this.connection.sendChoiceWindow(json);
  }

  @Override
  public void notifyCommandFailed(final String message) {
    this.connection.sendCommandFailed(message);
  }

  @Override
  public void notifyDrawWorld(final World world) {
    final Room room = world.getPlayerRoom();
    this.connection.sendDrawWorld(room.getName());
    world.getPlayerPosition().cubeSpiral(Config.SIGHT_RADIUS)
        .stream().sorted(new ReadingComparator()).forEach(
        coordinate -> world.getTile(coordinate)
            .ifPresent(tile -> this.notifyUpdateWorld(tile, world)));
  }

  @Override
  public void notifyGameEnd(final boolean win) {
    this.connection.sendGameEnd(win);
  }

  @Override
  public void notifyGameStarted() {
    this.connection.sendGameStarted();
  }

  @Override
  public void notifyMessage(final String message) {
    this.connection.sendMessage(message);
  }

  @Override
  public void notifyNextCycle(final int cycle) {
    this.connection.sendNextCycle(cycle);
  }

  @Override
  public void notifyRegistrationAborted() {
    this.connection.sendRegistrationAborted();
  }

  @Override
  public void notifySetCamera(final Coordinate coordinate) {
    this.connection.sendSetCamera(coordinate.getX(), coordinate.getY(), coordinate.getZ());
  }

  @Override
  public void notifyUpdatePlayer(final Player player) {
    final JSONObject root = player.toJSON();
    this.playerSchema.validate(root);
    this.connection.sendUpdatePlayer(root.toString());
  }

  @Override
  public void notifyUpdateWorld(final Tile tile) {
    final JSONObject root = tile.toJSON();
    this.updateSchema.validate(root);
    this.connection.sendUpdateWorld(root.toString());
  }

  private void notifyUpdateWorld(final Tile tile, final World world) {
    final Optional<Coordinate> destination = world.getDestination(tile.getPosition());
    if (destination.isPresent()) {
      final JSONObject root = new JSONObject().put("position", tile.getPosition().toJSON())
          .put("representation", String
              .valueOf(world.getRoom(destination.get()).orElseThrow(ServerError::new).getLevel()));
      final JSONObject schema = new JSONObject(new JSONTokener(Objects
          .requireNonNull(Main.class.getClassLoader().getResourceAsStream("schema/update.json"))));
      SchemaLoader.load(schema).validate(root);
      this.connection.sendUpdateWorld(root.toString());
    } else {
      this.notifyUpdateWorld(tile);
    }
  }
}
