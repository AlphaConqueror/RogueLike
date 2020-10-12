package sopra.controller.command;

import java.util.Queue;
import sopra.comm.Observer;
import sopra.model.World;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Leave implements Command {

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    world.loose();
  }

  @Override
  public String toString() {
    return "LEAVE()";
  }
}
