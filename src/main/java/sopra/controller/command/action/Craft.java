package sopra.controller.command.action;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Recipe;
import sopra.utils.Utils;

public class Craft extends Action {

  private final int index;
  private final Direction direction;

  public Craft(final int index, final Direction direction) {
    this.index = index - Config.INDEX_OFFSET;
    this.direction = direction;
  }

  @Override
  void action(final Player player, final World world, final Queue<Observer> observers) {
    switch (player.getState()) {
      case COMBAT -> observers.forEach(
              observer -> observer.notifyCommandFailed("We are in combat state!"));
      case ATTACKED, DEFAULT -> {
        final Optional<Tile> tile
                = world.getTile(world.getPlayerPosition().computeCoordinate(direction));

        if (tile.isEmpty() || tile.get().getEntity().isEmpty()) {
          observers.forEach(observer ->
                  observer.notifyCommandFailed("No table in this direction."));
          return;
        }

        final Optional<Item> optionalRecipe = player.getItem(this.index);

        if (optionalRecipe.isEmpty() || this.index > Config.INVENTORY_SIZE
                || !(optionalRecipe.get() instanceof Recipe)) {
          observers.forEach(observer -> observer.notifyCommandFailed("No recipe in this slot."));
          return;
        }

        final Recipe recipe = (Recipe) optionalRecipe.orElseThrow(ServerError::new);

        if (recipe.getRecipeTable()
                != tile.get().getEntity().orElseThrow(ServerError::new).getType()) {
          observers.forEach(observer ->
                  observer.notifyCommandFailed("Not the right crafting table."));
          return;
        }

        if (!recipe.canCraft(player)) {
          observers.forEach(observer -> observer.notifyCommandFailed("Not enough material."));
          return;
        }

        player.removeItem(this.index);

        for (final Map.Entry<EntityType, Integer> entry :
                recipe.getCraftingMaterials().entrySet()) {
          if (entry.getValue() == 0) {
            continue;
          }

          for (int i = 0; i < Config.INVENTORY_SIZE; i++) {
            final Optional<Item> item = player.getItem(i);

            if (item.isPresent() && item.get().getType() == entry.getKey()) {
              player.removeItem(i, entry.getValue());
            }
          }
        }

        player.addItem(recipe.getItem());
      }
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("CRAFT({}, {})", this.index, this.direction);
  }
}
