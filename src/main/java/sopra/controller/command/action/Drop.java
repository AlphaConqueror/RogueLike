package sopra.controller.command.action;

import java.util.Optional;
import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.model.entities.Trunk;
import sopra.model.entities.items.Item;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Drop extends Action {

  private final Direction direction;
  private final int index;

  public Drop(final int index, final Direction direction) {
    this.index = index - Config.INDEX_OFFSET;
    this.direction = direction;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    final Optional<Item> slot = player.getItem(this.index);

    if (slot.isPresent()) {
      final Coordinate coordinate = world.getPlayerPosition().computeCoordinate(this.direction,
          Config.DROP_DISTANCE);
      final Optional<Tile> tileOptional = world.getTile(coordinate);

      if (tileOptional.isEmpty()) {
        observers.forEach(observer -> observer.notifyCommandFailed("The field is not available."));
        return;
      }

      final Tile tile = tileOptional.orElseThrow(ServerError::new);
      Item item = slot.get();

      if (tile.getEntity().isPresent()) {
        final Entity entity = tile.getEntity().get();

        if (entity.getType() == EntityType.TRUNK) {
          final Trunk trunk = (Trunk) entity;

          if (trunk.addItem(item)) {
            player.clearIndex(this.index);
          } else {
            observers.forEach(observer -> observer.notifyCommandFailed("Trunk is full."));
          }
        } else {
          observers.forEach(observer -> observer.notifyCommandFailed("The field is blocked."));
        }
      } else {
        item = item.spawn();

        item.setPosition(coordinate);
        tile.setEntity(item);
        player.removeItem(this.index);
        observers.forEach(observer -> observer.notifyUpdateWorld(tile));
      }
    } else {
      observers.forEach(observer -> observer.notifyCommandFailed("No item in this slot!"));
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("DROP({}, {})", this.index, this.direction);
  }
}
