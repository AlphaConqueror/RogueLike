package sopra.systemtest.model.entities.item;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class UsePotion extends SystemTest {
  public UsePotion() {
    super(UsePotion.class, false);
  }

  @Override
  public long createSeed() {
    return 42;
  }

  @Override
  public String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/PlayerSurroundedByItemsLowHP.json");
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
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-19,\"z\":14},\"representation\":+}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-20,\"z\":14},\"representation\":&}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-21,\"z\":14},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-22,\"z\":14},\"representation\":\"8\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-17,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-18,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":"
            + (char) 191 + "}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-20,\"z\":15},\"representation\":@}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":?}");
    assertUpdateWorld("{\"position\":{\"x\":7,\"y\":-22,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":8,\"y\":-23,\"z\":15},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-18,\"z\":16},\"representation\":\"4\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-19,\"z\":16},\"representation\":.}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":?}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-21,\"z\":16},\"representation\":\"#\"}");
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
    //updatePlayer
    assertEvent();
    assertNextCycle(0);
    assertActNow();
  }

  @Override
  public void run() throws TimeoutException {
    init();
    // pick up surrounding items - one
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();
    // pick up surrounding items - two
    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertNextCycle(2);
    assertActNow();
    // pick up surrounding items - three
    sendPickUp(Direction.NORTH_EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-20,\"z\":14},\"representation\":.}");
    assertNextCycle(3);
    assertActNow();
    // pick up surrounding items - one
    sendPickUp(Direction.NORTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-19,\"z\":14},\"representation\":.}");
    assertNextCycle(4);
    assertActNow();
    // pick up surrounding items - one
    sendPickUp(Direction.SOUTH_EAST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-21,\"z\":16},\"representation\":.}");
    assertNextCycle(5);
    assertActNow();
    // pick up surrounding items - one
    sendPickUp(Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":.}");
    assertNextCycle(6);
    assertActNow();
    // Use the potion in slot 1
    sendUse(1);
    assertUpdatePlayer("{\"currentHealth\":20,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");
    // Check that player was updated & HP correctly increased
    assertNextCycle(7);
    assertActNow();
    //check that its stack was decreased
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"potion\":{\"stackSize\":1,\"level\":5,\"name\":\"potion\","
            + "\"value\":25},\"objectType\":\"potion\"},{\"decoction\":"
            + "{\"duration\":10,\"stackSize\":1,\"skill\":\"strength\","
            + "\"name\":\"decoction of strength\"},\"objectType\":\"decoction\"},"
            + "{\"armor\":{\"armor\":2,\"level\":2,\"name\":\"Plattenpanzer\"},"
            + "\"objectType\":\"armor\"},{\"weapon\":{\"damage\":2,\"level\":2,"
            + "\"name\":\"Laserknarre\",\"range\":2},\"objectType\":\"weapon\"},"
            + "{\"swordPart\":{\"stackSize\":1},\"objectType\":\"swordPart\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}]}");
    assertNextCycle(7);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}