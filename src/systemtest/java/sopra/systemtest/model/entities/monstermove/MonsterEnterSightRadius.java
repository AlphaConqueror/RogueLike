package sopra.systemtest.model.entities.monstermove;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class MonsterEnterSightRadius extends SystemTest {

  public MonsterEnterSightRadius() {
    super(MonsterEnterSightRadius.class, false);
  }

  @Override
  public long createSeed() {
    return 42;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/MonsterEnterSightRadius.json");
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(false);
    sendAttack(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":11,\"y\":-20,\"z\":9},\"representation\":å}");
    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }

  protected void initializeBasicWorld(final boolean peaceful) throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(5, -20, 15);
    assertDrawWorld("Island");
    assertUpdateWorld("{\"position\":{\"x\":11,\"y\":-20,\"z\":9},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":10,\"y\":-20,\"z\":10},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":9,\"y\":-20,\"z\":11},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-17,\"z\":12},\"representation\":\"6\"}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-18,\"z\":12},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-19,\"z\":12},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-20,\"z\":12},\"representation\":\"7\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-17,\"z\":13},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-18,\"z\":13},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-19,\"z\":13},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-20,\"z\":13},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-21,\"z\":13},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-17,\"z\":14},\"representation\":\"5\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-18,\"z\":14},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-19,\"z\":14},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-20,\"z\":14},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-21,\"z\":14},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-22,\"z\":14},\"representation\":\"8\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-17,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-18,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-20,\"z\":15},\"representation\":@}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-22,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-23,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-18,\"z\":16},\"representation\":\"4\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-19,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-21,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-22,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-23,\"z\":16},\"representation\":\"1\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-19,\"z\":17},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-20,\"z\":17},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-21,\"z\":17},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-22,\"z\":17},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-23,\"z\":17},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-20,\"z\":18},\"representation\":\"3\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-21,\"z\":18},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-22,\"z\":18},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-23,\"z\":18},\"representation\":\"2\"}");
    assertEvent();
    assertNextCycle(0);
    assertActNow();
  }
}
