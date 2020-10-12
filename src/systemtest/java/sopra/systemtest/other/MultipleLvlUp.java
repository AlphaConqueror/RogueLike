package sopra.systemtest.other;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class MultipleLvlUp extends SystemTest {

  public MultipleLvlUp() {
    super(MultipleLvlUp.class, false);
  }

  @Override
  protected long createSeed() {
    return 42;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(),
        "maps/fightMaps/MultiLvlUp.json");
  }

  @Override
  protected void run() throws TimeoutException {
    init();
    sendAttack(Direction.NORTH_EAST);
    //kill assistant and Lvl 0 --> lvl3, get 540Exp
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-5,\"z\":3},\"representation\":.}");
    assertUpdatePlayer("{\"currentHealth\":180,\"weapon\":{\"damage\":14,\"level\":8,"
            + "\"name\":\"Sword\",\"range\":1},\"armor\":{\"armor\":35,\"level\":8,"
            + "\"name\":\"Shield\"},\"luck\":9,\"strength\":9,"
            + "\"level\":3,\"skillPoints\":4,\"vitality\":9,\"name\":\"Player\","
            + "\"maxHealth\":180,\"agility\":9,\"experience\":540}");

    assertEvent();
    assertEvent();
    assertEvent();
    assertNextCycle(1);
    assertActNow();
    sendLeave();
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
