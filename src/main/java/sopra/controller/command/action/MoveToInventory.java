package sopra.controller.command.action;

import java.util.Optional;
import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.model.entities.Trunk;
import sopra.model.entities.items.Item;
import sopra.utils.Utils;

public class MoveToInventory extends Action {

  private final int index;
  private final Direction direction;

  public MoveToInventory(final int index, final Direction direction) {
    this.index = index - Config.INDEX_OFFSET;
    this.direction = direction;
  }

  @Override
  void action(final Player player, final World world, final Queue<Observer> observers) {
    final Optional<Tile> tile
            = world.getTile(world.getPlayerPosition().computeCoordinate(this.direction));

    if (tile.isEmpty() || tile.get().getEntity().isEmpty()
            || tile.get().getEntity().get().getType() != EntityType.TRUNK) {
      observers.forEach(observer -> observer.notifyCommandFailed("No trunk in that direction."));
      return;
    }

    final Trunk trunk = (Trunk) tile.get().getEntity().get();
    final Optional<Item> optionalItem = trunk.getItem(this.index);

    if (optionalItem.isPresent()) {
      final Item item = optionalItem.get();

      //TODO: Maybe change order of adding/removing items.
      if (player.addItem(item)) {
        trunk.clearIndex(this.index);
      } else {
        observers.forEach(observer -> observer.notifyCommandFailed("Inventory is full."));
      }
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("MOVETOINVENTORY({}, {})", this.index, this.direction);
  }
}
