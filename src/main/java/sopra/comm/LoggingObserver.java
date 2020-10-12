package sopra.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.model.Coordinate;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Player;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class LoggingObserver implements Observer {

  private final Logger logger;

  public LoggingObserver(final Class<?> subclass) {
    this.logger = LoggerFactory.getLogger(subclass);
  }

  @Override
  public void notifyActNow() {
    this.logger.trace("send ACT_NOW()");
  }

  @Override
  public void notifyChoiceWindow(final String json) {
    this.logger.trace("send CHOICE_WINDOW({})", json);
  }

  @Override
  public void notifyCommandFailed(final String message) {
    this.logger.trace("send COMMAND_FAILED({})", message);
  }

  @Override
  public void notifyDrawWorld(final World world) {
    this.logger.trace("send DRAW_WORLD({})", world.getPlayerRoom().getName());
  }

  @Override
  public void notifyGameEnd(final boolean win) {
    this.logger.trace("send GAME_END({})", win);
  }

  @Override
  public void notifyGameStarted() {
    this.logger.trace("send GAME_STARTED()");
  }

  @Override
  public void notifyMessage(final String message) {
    this.logger.trace("send MESSAGE({})", message);
  }

  @Override
  public void notifyNextCycle(final int cycle) {
    this.logger.trace("send NEXT_CYCLE({})", cycle);
  }

  @Override
  public void notifyRegistrationAborted() {
    this.logger.trace("send REGISTRATION_ABORTED()");
  }

  @Override
  public void notifySetCamera(final Coordinate coordinate) {
    this.logger.trace("send UPDATE_CAMERA{}", coordinate);
  }

  @Override
  public void notifyUpdatePlayer(final Player player) {
    this.logger.trace("send UPDATE_PLAYER({})", player.toJSON());
  }

  @Override
  public void notifyUpdateWorld(final Tile tile) {
    this.logger.trace("send UPDATE_WORLD({})", tile.toJSON());
  }
}

