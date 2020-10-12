package sopra.controller.command.action;

import java.util.Optional;
import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.ServerError;
import sopra.controller.visitor.PickUpVisitor;
import sopra.model.Coordinate;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Entity;
import sopra.model.entities.Player;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class PickUp extends Action {

  private final Direction direction;

  public PickUp(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    switch (player.getState()) {
      case ATTACKED, COMBAT -> {
        for (final Observer observer : observers) {
          observer.notifyCommandFailed("We are in attacked state!");
        }
      }
      case DEFAULT -> {
        final Coordinate coordinate = world.getPlayerPosition();
        final Optional<Tile> tile = world.getTile(coordinate.computeCoordinate(this.direction));
        if (tile.isPresent() && tile.get().hasEntity()) {
          final Optional<Entity> optional = tile.get().getEntity();
          optional.ifPresent(entity -> entity.accept(new PickUpVisitor(world, observers)));
        }
      }
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("PICK_UP({})", this.direction);
  }

}
