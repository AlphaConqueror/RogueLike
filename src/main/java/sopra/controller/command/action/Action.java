package sopra.controller.command.action;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import sopra.comm.Direction;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.controller.command.Command;
import sopra.controller.visitor.AttackVisitor;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.EntityType;
import sopra.model.entities.Monster;
import sopra.model.entities.Player;

/**
 * All action commands are commands which result in monsters moving afterwards.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public abstract class Action implements Command {

  /**
   * This method computes the action a monster does.
   *
   * @param monster   the monster for which the action should be computed
   * @param world     a world object, providing access to the whole model
   * @param observers a list of observers
   */
  protected void action(final Monster monster, final World world, final Queue<Observer> observers) {
    if (world.isEnabled()) {
      final Coordinate position = monster.getPosition().orElseThrow(ServerError::new);
      if (position.distance(world.getPlayerPosition()) <= Config.MONSTER_ATTACK_RADIUS) {
        world.getPlayer().accept(new AttackVisitor(world, observers, monster));
      } else {
        Optional<Coordinate> choice = Optional.empty();
        Command.calculateState(world, monster);
        switch (monster.getState()) {
          case DEFAULT -> {
            final List<Coordinate> coordinates =
                position.cubeRing(1).stream()
                    .filter(coordinate -> world.isAccessible(EntityType.BUG, coordinate))
                    .collect(Collectors.toList());
            if (!coordinates.isEmpty()) {
              choice = Optional.ofNullable(world.roll(coordinates.toArray(Coordinate[]::new)));
            }
          }
          case ATTACKED, COMBAT -> {
            final Coordinate target = world.getPlayerPosition();
            int minimum = Integer.MAX_VALUE;
            for (final Direction direction : Direction.values()) {
              final Coordinate coordinate = position.computeCoordinate(direction);
              if (world.isAccessible(EntityType.BUG, coordinate)) {
                final int distance = coordinate.distance(target);
                if (distance < minimum) {
                  minimum = distance;
                  choice = Optional.of(coordinate);
                }
              }
            }
          }
        }
        if (choice.isPresent()) {
          world.move(monster, choice.get());
          if (world.getPlayer().sees(position)) {
            observers.forEach(observer -> observer
                .notifyUpdateWorld(world.getTile(position).orElseThrow(ServerError::new)));
          }
          if (world.getPlayer().sees(choice.get())) {
            for (final Observer observer : observers) {
              observer.notifyUpdateWorld(world.getTile(choice.get()).orElseThrow(ServerError::new));
            }
          }
        }
      }
    }
  }

  /**
   * This method is called by the execute method of an action command. It takes care of the main
   * logic for each command.
   *
   * @param player    a player object
   * @param world     a world object, providing access to the whole model
   * @param observers a list of observers
   */
  abstract void action(Player player, World world, Queue<Observer> observers);

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    final Player player = world.getPlayer();
    Command.calculateState(world, player);
    switch (player.getState()) {
      case REGISTER -> observers.forEach(Observer::notifyRegistrationAborted);
      case DEFAULT, ATTACKED, COMBAT -> {
        this.action(player, world, observers);
        if (world.isEnabled()) {
          for (final Monster monster : world.getPlayerRoom()) {
            if (world.isEnabled() && monster.isEnabled()) {
              this.action(monster, world, observers);
            }
          }
          if (player.tickDecoctions()) {
            observers.forEach(observer -> observer.notifyUpdatePlayer(player));
          }
          world.incrementCycle();
        }
      }
    }
  }
}
