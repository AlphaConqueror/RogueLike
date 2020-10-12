package sopra.systemtest.other;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;


public class DontKillProfessorWrongSword extends SystemTest {

  public DontKillProfessorWrongSword() {
    super(DontKillProfessorWrongSword.class, false);
  }

  @Override
  public long createSeed() {
    return 42;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/fightMaps/ProfessorNotDeadWrongSword.json");
  }

  @Override
  public void run() throws TimeoutException {
    init();
    sendAttack(Direction.EAST);
    assertNextCycle(1);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }

  private void init() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(21, -24, 3);
    assertDrawWorld("Dungeon 8");
    assertUpdateWorld("{\"position\":{\"x\":21,\"y\":-23,\"z\":2},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":22,\"y\":-24,\"z\":2},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":20,\"y\":-23,\"z\":3},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":21,\"y\":-24,\"z\":3},\"representation\":@}");
    assertUpdateWorld("{\"position\":{\"x\":22,\"y\":-25,\"z\":3},\"representation\":¶}");
    assertUpdateWorld("{\"position\":{\"x\":20,\"y\":-24,\"z\":4},\"representation\":\"0\"}");
    assertUpdateWorld("{\"position\":{\"x\":21,\"y\":-25,\"z\":4},\"representation\":.}");
    assertEvent();
    assertNextCycle(0);
    assertActNow();
  }

}
