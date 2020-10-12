package sopra.systemtest.model.entities.monstermove;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.other.ExampleTest;

public class ForceMonsterNE extends ExampleTest {

  public ForceMonsterNE() {
    super(ForceMonsterNE.class, false);
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/monsterMoveTestMaps/forceMonsterNE.json");
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(false);
    sendAttack(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-19,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":å}");
    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}
