package sopra.systemtest.model.entities.equiment;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.TimeoutException;
import sopra.controller.Config;
import sopra.systemtest.api.SystemTest;

public class DefaultSwordStrengthSystemTest extends SystemTest {

  public DefaultSwordStrengthSystemTest() {
    super(DefaultSwordStrengthSystemTest.class, false);
  }

  @Override
  protected long createSeed() {
    return Config.DEFAULT_SEED;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/DefaultSwordWithStrength.json");
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    sendRegister();
    assertGameStarted();
    assertSetCamera(5, -20, 15);
    assertDrawWorld("Island");

    for (int i = 0; i < 37; i++) {
      assertEvent();
    }

    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"effect\":\"strength\","
            + "\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},\"luck\":1,"
            + "\"strength\":2,\"level\":1,\"skillPoints\":0,\"vitality\":1,"
            + "\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,\"experience\":0}");
    assertNextCycle(0);
    assertActNow();

    sendLeave();
    assertGameEnd(false);
  }
}
