package sopra.comm;

import sopra.model.Coordinate;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Player;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public interface Observer {

  void notifyActNow();

  void notifyChoiceWindow(String json);

  void notifyCommandFailed(String message);

  void notifyDrawWorld(World world);

  void notifyGameEnd(boolean win);

  void notifyGameStarted();

  void notifyMessage(String message);

  void notifyNextCycle(int cycle);

  void notifyRegistrationAborted();

  void notifySetCamera(Coordinate coordinate);

  void notifyUpdatePlayer(Player player);

  void notifyUpdateWorld(Tile tile);
}
