package sopra.systemtest.model.entities.monstermove;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class MonsterNoMoveExit extends SystemTest {

  public MonsterNoMoveExit() {
    super(MonsterNoMoveExit.class, false);
  }

  @Override
  protected long createSeed() {
    return 42;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/monsterMoveTestMaps/MonsterNoMoveExit.json");
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    sendRegister();
    assertGameStarted();
    //setCamera
    assertEvent();
    assertDrawWorld("Island");
    for (int i = 0; i < 39; i++) {
      //updateMap
      assertEvent();
    }
    //updatePlayer
    assertEvent();
    assertNextCycle(0);
    assertActNow();
    sendUse(5);
    assertCommandFailed();
    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}
