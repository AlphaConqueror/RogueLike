package sopra.systemtest.model.entities.equiment;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.TimeoutException;
import sopra.controller.Config;
import sopra.systemtest.api.SystemTest;

public class DefaultSwordVitalitySystemTest extends SystemTest {

  public DefaultSwordVitalitySystemTest() {
    super(DefaultSwordVitalitySystemTest.class, false);
  }

  @Override
  protected long createSeed() {
    return Config.DEFAULT_SEED;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/DefaultSwordWithVitality.json");
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
            + "{\"damage\":1,\"level\":1,\"effect\":\"vitality\","
            + "\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},\"luck\":1,"
            + "\"strength\":1,\"level\":1,\"skillPoints\":0,\"vitality\":3,"
            + "\"name\":\"Player\",\"maxHealth\":30,\"agility\":1,\"experience\":0}");
    assertNextCycle(0);
    assertActNow();

    sendLeave();
    assertGameEnd(false);
  }
}
