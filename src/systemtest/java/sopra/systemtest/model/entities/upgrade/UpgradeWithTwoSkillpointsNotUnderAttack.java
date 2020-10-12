package sopra.systemtest.model.entities.upgrade;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;


public class UpgradeWithTwoSkillpointsNotUnderAttack extends SystemTest {
  public UpgradeWithTwoSkillpointsNotUnderAttack() {
    super(UpgradeWithTwoSkillpointsNotUnderAttack.class, false);
  }

  @Override
  public long createSeed() {
    return 42;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(),
        "maps/itemTestMaps/NoMonsterNoItemsTwoSkillpointsLuck9.json");
  }

  @Override
  public void run() throws TimeoutException {
    init();
    sendUpgrade(1);
    //skillpunkt should be used and added to str
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":{\"damage\":1,"
            + "\"level\":1,\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":9,\"strength\":2,\"level\":1,\"skillPoints\":1,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");

    assertNextCycle(1);
    assertActNow();
    sendUpgrade(3);
    //skillpunkt should be used and added to agi
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":{\"damage\":1,"
            + "\"level\":1,\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":9,\"strength\":2,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":2,\"experience\":0}");
    assertNextCycle(2);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }

  private void init() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(5, -20, 15);
    assertDrawWorld("Island");
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