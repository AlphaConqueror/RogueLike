package sopra.systemtest.model.entities.loot;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class BugLootPotionSystemTest extends DefaultSystemTest {

  public BugLootPotionSystemTest() {
    super(BugLootPotionSystemTest.class, false);
  }

  @Override
  protected long createSeed() {
    return 1;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/lootMaps/BugLoot.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendAttack(Direction.WEST);
    assertNextCycle(1);
    assertActNow();

    sendAttack(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},"
            + "\"representation\":\".\"}");
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":{\"damage\":6,\"level\":1,"
            + "\"name\":\"Sword\",\"range\":1},\"armor\":{\"armor\":1,\"level\":1,"
            + "\"name\":\"Shield\"},\"luck\":1,\"strength\":1,\"level\":1,"
            + "\"skillPoints\":0,\"vitality\":1,\"name\":\"Player\","
            + "\"maxHealth\":20,\"agility\":1,\"experience\":1}");
    assertEvent();
    assertNextCycle(2);
    assertActNow();

    sendPickUp(Direction.NORTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-19,\"z\":14},\"representation\":\".\"}");
    assertNextCycle(3);
    assertActNow();

    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"potion\":{\"stackSize\":1,\"level\":1,\"name\":\"potion\","
            + "\"value\":5},\"objectType\":\"potion\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"}]}");
    assertNextCycle(3);
    assertActNow();
  }
}
