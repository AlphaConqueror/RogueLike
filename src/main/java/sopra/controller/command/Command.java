package sopra.controller.command;

import static sopra.model.entities.CharacterState.ATTACKED;
import static sopra.model.entities.CharacterState.COMBAT;
import static sopra.model.entities.CharacterState.DEFAULT;
import static sopra.model.entities.CharacterState.REGISTER;

import java.util.Queue;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.Character;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public interface Command {

  /**
   * This method computes the state the given {@link Character} is in.
   *
   * @param world     a {@link World}, providing access to the whole model
   * @param character the {@link Character} for which the state has to be computed
   */
  static void calculateState(final World world, final Character character) {
    switch (character.getState()) {
      case REGISTER -> character.setState(REGISTER);
      case DEFAULT, ATTACKED, COMBAT -> {
        final Coordinate position = character.getPosition().orElseThrow(ServerError::new);
        final int radius = switch (character.getType()) {
          case PLAYER -> world.getPlayerRoom().getMonsters().stream().map(monster ->
              monster.calculateDistance(position)).min(Integer::compare).orElse(Integer.MAX_VALUE);
          case ASSISTANT, BUG, OVERFLOW, TUTOR, PROFESSOR -> world.getPlayer()
              .calculateDistance(position);
          default -> throw new ServerError("Unexpected value: " + character.getType());
        };
        if (radius <= Config.COMBAT_RADIUS) {
          character.setState(COMBAT);
        } else if (radius <= Config.ATTACK_RADIUS) {
          character.setState(ATTACKED);
        } else {
          character.setState(DEFAULT);
        }
      }
    }
  }

  /**
   * This method is the entry point for every command.
   *
   * @param world     a {@link World}, providing access to the whole model
   * @param observers a list of {@link Observer}s
   */
  void execute(World world, Queue<Observer> observers);
}
