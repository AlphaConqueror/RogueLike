package sopra.controller;

import java.util.ArrayDeque;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.comm.ConnectionObserver;
import sopra.comm.Observer;
import sopra.comm.ServerConnection;
import sopra.comm.TimeoutException;
import sopra.controller.command.Command;
import sopra.model.World;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Game {

  private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
  private final Queue<Observer> observers;
  private final World world;

  public Game(final World world) {
    this.world = world;
    this.observers = new ArrayDeque<>();
  }

  public void addObserver(final Observer observer) {
    this.observers.add(observer);
  }

  /**
   * The servers' main loop.
   *
   * @param connection a server connection
   */
  public void run(final ServerConnection<Command> connection) {
    this.addObserver(new ConnectionObserver(connection));
    LOGGER.info("starting game");
    Command next;
    try {
      next = connection.nextCommand();
      LOGGER.debug("{} -> {}", this.world.getCycle(), next);
      next.execute(this.world, this.observers);
    } catch (final TimeoutException e) {
      return;
    }
    if (this.world.isDisabled()) {
      this.observers.forEach(Observer::notifyRegistrationAborted);
    } else {
      while (this.world.isEnabled() && this.world.getCycle() < Config.MAX_CYCLE) {
        this.observers.forEach(observer -> observer.notifyNextCycle(this.world.getCycle()));
        this.observers.forEach(Observer::notifyActNow);
        try {
          next = connection.nextCommand();
          LOGGER.debug("{} -> {}", this.world.getCycle(), next);
          next.execute(this.world, this.observers);
        } catch (final TimeoutException e) {
          return;
        }
      }
      this.observers.forEach(observer -> observer.notifyGameEnd(this.world.isWin()));
    }
  }
}
