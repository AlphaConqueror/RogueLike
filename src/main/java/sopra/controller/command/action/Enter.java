package sopra.controller.command.action;

import java.util.Optional;
import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.Player;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Enter extends Action {

  private final Direction direction;

  public Enter(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    switch (player.getState()) {
      case COMBAT -> observers.forEach(
              observer -> observer.notifyCommandFailed("We are in combat state!"));
      case ATTACKED, DEFAULT -> {
        final Coordinate position = world.getPlayerPosition();
        final Optional<Coordinate> destination =
            world.getDestination(position.computeCoordinate(this.direction));
        if (destination.isPresent()) {
          world.move(player, destination.get());
          observers.forEach(observer -> observer.notifySetCamera(destination.get()));
          observers.forEach(
              observer -> observer
                  .notifyUpdateWorld(world.getTile(position).orElseThrow(ServerError::new)));
          observers.forEach(observer -> observer.notifyDrawWorld(world));
        }
      }
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("ENTER({})", this.direction);
  }
}
