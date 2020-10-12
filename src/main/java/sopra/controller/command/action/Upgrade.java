package sopra.controller.command.action;

import java.util.Queue;
import sopra.comm.Observer;
import sopra.controller.ServerError;
import sopra.model.World;
import sopra.model.entities.Character.CharacterSkill;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Upgrade extends Action {

  private final CharacterSkill skill;

  public Upgrade(final int index) {
    this.skill = CharacterSkill.fromIndex(EntityType.PLAYER, index);
  }

  @Override
  public void action(final Player player, final World world, final Queue<Observer> observers) {
    switch (player.getState()) {
      case ATTACKED, COMBAT -> observers
          .forEach(observer -> observer
              .notifyCommandFailed("Can not upgrade skill! We are being attacked!"));
      case DEFAULT -> {
        if (player.upgrade(this.skill)) {
          observers.forEach(observer -> observer.notifyUpdatePlayer(player));
        } else {
          observers.forEach(observer -> observer.notifyCommandFailed("Can not upgrade skill!"));
        }
      }
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  public String toString() {
    return Utils.substitute("UPGRADE({})", this.skill);
  }
}
