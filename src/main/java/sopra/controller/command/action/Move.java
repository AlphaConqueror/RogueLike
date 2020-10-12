package sopra.controller.command.action;

import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Move extends Action {

  private final Direction direction;

  public Move(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    final Coordinate position = world.getPlayerPosition();
    final Coordinate destination = position.computeCoordinate(this.direction);
    if (world.isAccessible(EntityType.PLAYER, destination)) {
      world.move(player, destination);
      observers.forEach(observer -> observer.notifySetCamera(destination));
      observers.forEach(observer -> observer.notifyDrawWorld(world));
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("MOVE({})", this.direction);
  }
}
