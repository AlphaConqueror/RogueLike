package sopra.systemtest.other;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class PlayerDeadTest extends SystemTest {
  public PlayerDeadTest() {
    super(PlayerDeadTest.class, false);
  }

  @Override
  public long createSeed() {
    return 0;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(),
        "maps/fightMaps/PlayerDead.json");
  }

  @Override
  public void run() throws TimeoutException {
    init();
    sendAttack(Direction.NORTH_EAST);
    assertNextCycle(1);
    assertActNow();
    sendAttack(Direction.NORTH_EAST);
    assertNextCycle(2);
    assertActNow();
    sendAttack(Direction.NORTH_EAST);
    assertUpdatePlayer("{\"currentHealth\":-8,\"weapon\":{\"damage\":1,"
            + "\"level\":1,\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");
    assertGameEnd(false);

  }

  private void init() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(1, -5, 4);
    assertDrawWorld("Dungeon 6");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-3,\"z\":2},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-4,\"z\":2},\"representation\":\"=\"}");
    assertUpdateWorld("{\"position\":{\"x\":0,\"y\":-3,\"z\":3},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-4,\"z\":3},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-5,\"z\":3},\"representation\":µ}");
    assertUpdateWorld("{\"position\":{\"x\":0,\"y\":-4,\"z\":4},\"representation\":\"0\"}");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-5,\"z\":4},\"representation\":@}");
    assertEvent();
    assertNextCycle(0);
    assertActNow();
  }

}
