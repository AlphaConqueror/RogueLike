package sopra.systemtest.model.entities.equiment;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.controller.Config;
import sopra.systemtest.api.SystemTest;

public class UseSwordVitalitySystemTest extends SystemTest {

  public UseSwordVitalitySystemTest() {
    super(UseSwordVitalitySystemTest.class, false);
  }

  @Override
  protected long createSeed() {
    return Config.DEFAULT_SEED;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/SwordWithVitality.json");
  }

  @Override
  protected void run() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(5, -20, 15);
    assertDrawWorld("Island");

    for (int i = 0; i < 37; i++) {
      assertEvent();
    }

    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":6,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":2,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");
    assertNextCycle(0);
    assertActNow();

    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":\".\"}");
    assertNextCycle(1);
    assertActNow();

    sendUse(1);
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"effect\":\"vitality\","
            + "\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},\"luck\":1,"
            + "\"strength\":1,\"level\":1,\"skillPoints\":0,\"vitality\":3,"
            + "\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,\"experience\":0}");
    assertNextCycle(2);
    assertActNow();

    sendUse(1);
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":6,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":2,\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,"
            + "\"experience\":0}");
    assertNextCycle(3);
    assertActNow();

    sendLeave();
    assertGameEnd(false);
  }
}
