package sopra.controller.command.action;

import java.util.Optional;
import java.util.Queue;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.model.World;
import sopra.model.entities.Player;
import sopra.model.entities.items.Item;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Scrap extends Action {

  private final int index;

  public Scrap(final int index) {
    this.index = index - Config.INDEX_OFFSET;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    final Optional<Item> item = player.getItem(this.index);
    if (item.isPresent()) {
      if (item.get().isLegendary()) {
        world.loose();
      }
      player.removeItem(this.index);
    } else {
      observers.forEach(observer -> observer.notifyCommandFailed("No item in this slot!"));
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("SCRAP({})", this.index);
  }
}
