package sopra.controller.command;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONObject;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Player;
import sopra.model.entities.items.Recipe;
import sopra.utils.Utils;

public class CraftingMenu implements Command {

  private final Direction direction;

  public CraftingMenu(final Direction direction) {
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
                = world.getTile(world.getPlayerPosition().computeCoordinate(direction));

        if (tile.isEmpty() || tile.get().getEntity().isEmpty()) {
          return;
        }

        final List<Recipe> recipes
                = player.getCraftableRecipes(tile.get().getEntity().get().getType().toJSON());

        final JSONObject root = new JSONObject();
        final JSONArray array = new JSONArray();

        recipes.forEach(
                recipe -> array.put(new JSONObject()
                  .put("objectType", "recipe")
                  .put("recipe", recipe.toJSON())));

        root.put("listType", "craft").put("craft", array);
        observers.forEach(observer -> observer.notifyChoiceWindow(root.toString()));
      }
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("CRAFTING_MENU({})", this.direction);
  }
}
