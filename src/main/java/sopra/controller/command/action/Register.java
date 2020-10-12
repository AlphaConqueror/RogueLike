package sopra.controller.command.action;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.comm.Observer;
import sopra.controller.command.Command;
import sopra.model.World;
import sopra.model.entities.CharacterState;
import sopra.model.entities.Monster;
import sopra.model.entities.Player;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Register extends Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(Register.class);

  @Override
  void action(final Player player, final World world, final Queue<Observer> observers) {
    world.enable();
    player.setState(CharacterState.DEFAULT);
    observers.forEach(Observer::notifyGameStarted);
    observers.forEach(observer -> observer.notifySetCamera(world.getPlayerPosition()));
    observers.forEach(observer -> observer.notifyDrawWorld(world));
    observers.forEach(observer -> observer.notifyUpdatePlayer(player));
  }

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    final Player player = world.getPlayer();
    Command.calculateState(world, player);
    switch (player.getState()) {
      case REGISTER -> this.action(player, world, observers);
      case DEFAULT, ATTACKED, COMBAT -> {
        observers.forEach(observer -> observer.notifyCommandFailed("Unexpected Register."));
        for (final Monster monster : world.getPlayerRoom()) {
          if (world.isEnabled() && monster.isEnabled()) {
            super.action(monster, world, observers);
          }
        }
        player.getDecoctions()
            .forEach(decoction -> LOGGER
                .debug("({}, {})", decoction.getSkill(), decoction.getDuration()));
        if (player.tickDecoctions()) {
          observers.forEach(observer -> observer.notifyUpdatePlayer(player));
        }
        world.incrementCycle();
      }
    }
  }

  @Override
  public String toString() {
    return "REGISTER({})";
  }
}
