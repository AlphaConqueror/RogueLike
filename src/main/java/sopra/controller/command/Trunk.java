package sopra.controller.command;

import java.util.Optional;
import java.util.Queue;
import org.json.JSONObject;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.utils.Utils;

public class Trunk implements Command {

  private final Direction direction;

  public Trunk(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    final Player player = world.getPlayer();

    Command.calculateState(world, player);

    switch (player.getState()) {
      case REGISTER -> observers.forEach(Observer::notifyRegistrationAborted);
      case DEFAULT, ATTACKED, COMBAT -> {
        final Optional<Tile> tile
                = world.getTile(world.getPlayerPosition().computeCoordinate(this.direction));

        if (tile.isEmpty() || tile.get().getEntity().isEmpty()) {
          return;
        }

        final Entity entity = tile.get().getEntity().get();

        if (entity.getType() != EntityType.TRUNK) {
          return;
        }

        final JSONObject root = new JSONObject();

        root.put("listType", "trunk")
                .put("trunk", ((sopra.model.entities.Trunk) entity).inventoryJSON().get("items"));

        observers.forEach(observer -> observer.notifyChoiceWindow(root.toString()));
      }
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("TRUNK({})", this.direction);
  }
}
