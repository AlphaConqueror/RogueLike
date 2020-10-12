package sopra.systemtest.model.entities.monstermove;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.other.ExampleTest;

public class ForceMonsterNW extends ExampleTest {

  public ForceMonsterNW() {
    super(ForceMonsterNW.class, false);
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/monsterMoveTestMaps/forceMonsterNW.json");
  }

  @Override
  public void run() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(6, -20, 14);
    assertDrawWorld("Island");
    for (int i = 0; i < 38; i++) {
      assertEvent();
    }
    assertNextCycle(0);
    assertActNow();
    sendMove(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-18,\"z\":14},\"representation\":å}");
    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}