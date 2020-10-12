package sopra.controller.command.action;

import java.util.Queue;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.controller.visitor.AttackVisitor;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.Player;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Attack extends Action {

  private final Direction direction;

  public Attack(final Direction direction) {
    this.direction = direction;
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    final Coordinate position = player.getPosition().orElseThrow(ServerError::new);
    for (int i = Config.MINIMAL_RANGE; i <= player.getWeapon().getRange(); i++) {
      final Coordinate target = position.computeCoordinate(this.direction, i);
      if (world.isAttackable(target)) {
        world.getEntity(target)
            .ifPresent(
                entity -> entity.accept(new AttackVisitor(world, observers, world.getPlayer())));
        break;
      }
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("ATTACK({})", this.direction);
  }

}

