package sopra.controller.command;

import java.util.Queue;
import org.json.JSONObject;
import sopra.comm.Observer;
import sopra.model.World;
import sopra.model.entities.Player;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Inventory implements Command {

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    final Player player = world.getPlayer();
    Command.calculateState(world, player);
    switch (player.getState()) {
      case REGISTER -> observers.forEach(Observer::notifyRegistrationAborted);
      case DEFAULT, ATTACKED, COMBAT -> {
        final JSONObject json = world.getPlayer().inventoryJSON();
        observers.forEach(observer -> observer.notifyChoiceWindow(json.toString()));
      }
    }
  }

  @Override
  public String toString() {
    return "INVENTORY()";
  }
}
