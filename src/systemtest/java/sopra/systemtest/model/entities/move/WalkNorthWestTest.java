package sopra.systemtest.model.entities.move;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.model.Coordinate;
import sopra.systemtest.other.ExampleTest;

public class WalkNorthWestTest extends ExampleTest {

  public WalkNorthWestTest() {
    super(WalkNorthWestTest.class, false);
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/peaceful.json");
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(true);

    final Coordinate playerLocation = new Coordinate(5, -19, 14);

    sendMove(Direction.NORTH_WEST);
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
