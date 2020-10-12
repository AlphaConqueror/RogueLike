package sopra.systemtest;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.other.ExampleTest;

public class EnterTest extends ExampleTest {
  public EnterTest() {
    super(EnterTest.class, false);
  }

  @Override
  public long createSeed() {
    return 0;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/peaceful.json");
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(true);

    for (int i = 0; i < 3; i++) {
      sendMove(Direction.EAST);
      for (int j = 0; j < 41; j++) {
        assertEvent();
      }
    }
    sendMove(Direction.EAST); // wall walking
    //assertCommandFailed(); // now just does nothing on wall walking
    assertNextCycle(4);
    assertActNow();
    sendMove(Direction.SOUTH_WEST); // moving into door
    //assertCommandFailed();// now just does nothing whe walking into door
    assertNextCycle(5);
    assertActNow();
    sendEnter(Direction.SOUTH_WEST);
    for (int j = 0; j < 10; j++) {
      assertEvent();
    }
    assertNextCycle(6);
    assertActNow();

    sendLeave();
    assertGameEnd(false);
  }
}
