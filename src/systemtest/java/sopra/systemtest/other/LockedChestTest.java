package sopra.systemtest.other;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class LockedChestTest extends SystemTest {

  public LockedChestTest() {
    super(LockedChestTest.class, false);
  }

  @Override
  protected long createSeed() {
    return 0;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/fightMaps/AttackChestGuardianAlive.json");
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    sendRegister();
    assertGameStarted();
    for (int i = 0; i < 10; i++) {
      assertEvent();
    }
    assertNextCycle(0);
    assertActNow();
    sendAttack(Direction.EAST);
    assertCommandFailed();
    assertNextCycle(1);
    assertActNow();
    sendAttack(Direction.NORTH_EAST);
    assertNextCycle(2);
    assertActNow();
    sendAttack(Direction.SOUTH_EAST);
    assertEvent();
    assertEvent();
    assertEvent();
    assertNextCycle(3);
    assertActNow();
    sendAttack(Direction.EAST);
    assertEvent();
    assertEvent();
    assertNextCycle(4);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}
