package sopra.systemtest.other;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class PlayerkilledByProfessor extends SystemTest {
  public PlayerkilledByProfessor() {
    super(PlayerkilledByProfessor.class, false);
  }

  @Override
  public long createSeed() {
    return 0;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(),
        "maps/fightMaps/PlayerKilledByProfessor.json");
  }

  @Override
  public void run() throws TimeoutException {
    init();
    sendAttack(Direction.EAST);
    assertNextCycle(1);
    assertActNow();
    sendAttack(Direction.EAST);
    assertNextCycle(2);
    assertActNow();
    sendAttack(Direction.EAST);
    assertUpdatePlayer("{\"currentHealth\":-7,\"weapon\":{\"damage\":20,"
            + "\"level\":42,\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,"
            + "\"experience\":0}");
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
