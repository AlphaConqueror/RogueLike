package sopra.systemtest.model.entities.move;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.model.Coordinate;
import sopra.systemtest.other.ExampleTest;

public class WalkNorthEastTest extends ExampleTest {

  public WalkNorthEastTest() {
    super(WalkNorthEastTest.class, false);
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/peaceful.json");
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(true);

    final Coordinate playerLocation = new Coordinate(6, -20, 14);

    sendMove(Direction.NORTH_EAST);
    assertSetCamera(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
    assertDrawWorld("Island");

    for (int i = 0; i < 37; i++) {
      assertEvent();
    }

    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}
