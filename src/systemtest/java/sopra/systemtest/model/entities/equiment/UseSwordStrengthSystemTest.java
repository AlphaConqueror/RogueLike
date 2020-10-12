package sopra.systemtest.model.entities.equiment;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class UseSwordStrengthSystemTest extends DefaultSystemTest {

  public UseSwordStrengthSystemTest() {
    super(UseSwordStrengthSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/SwordWithStrength.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":\".\"}");
    assertNextCycle(1);
    assertActNow();

    sendUse(1);
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"effect\":\"strength\","
            + "\"name\":\"Sword\",\"range\":1},\"armor\":"
            + "{\"armor\":1,\"level\":1,\"name\":\"Shield\"},\"luck\":1,"
            + "\"strength\":2,\"level\":1,\"skillPoints\":0,\"vitality\":1,"
            + "\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,\"experience\":0}");
    assertNextCycle(2);
    assertActNow();

    sendUse(1);
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":6,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,\"agility\":1,"
            + "\"experience\":0}");
    assertNextCycle(3);
    assertActNow();
  }
}
