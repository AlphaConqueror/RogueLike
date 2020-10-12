package sopra.systemtest.model.entities.item;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class DropWrongIndex extends SystemTest {
  public DropWrongIndex() {
    super(DropWrongIndex.class, false);
  }

  @Override
  protected long createSeed() {
    return 42;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/itemTestMaps/PlayerSurroundedByItemsB.json");
  }

  @Override
  protected void run() throws TimeoutException {
    init();
    //pick Skillpotion str up
    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();
    //pick HealthPotion Lvl.5 up
    sendPickUp(Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":.}");
    assertNextCycle(2);
    assertActNow();
    //pick HealthPotion Lvl.5 up
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(3);
    assertActNow();
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"decoction\":{\"duration\":10,\"stackSize\":1,"
            + "\"skill\":\"strength\",\"name\":\"decoction of strength\"},"
            + "\"objectType\":\"decoction\"},{\"potion\":{\"stackSize\":2,"
            + "\"level\":5,\"name\":\"potion\",\"value\":25},"
            + "\"objectType\":\"potion\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}]}");
    assertNextCycle(3);
    assertActNow();
    sendDrop(1, Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":"
        + (char) 191 + "}");
    assertNextCycle(4);
    assertActNow();
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"objectType\":\"empty\"},{\"potion\":"
            + "{\"stackSize\":2,\"level\":5,\"name\":\"potion\","
            + "\"value\":25},\"objectType\":\"potion\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"}]}");
    assertNextCycle(4);
    assertActNow();
    sendDrop(2, Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":?}");
    assertNextCycle(5);
    assertActNow();
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"objectType\":\"empty\"},{\"potion\":"
            + "{\"stackSize\":1,\"level\":5,\"name\":\"potion\",\"value\":25},"
            + "\"objectType\":\"potion\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}]}");
    assertNextCycle(5);
    assertActNow();
    sendDrop(2, Direction.WEST);
    assertCommandFailed();
    assertNextCycle(6);
    assertActNow();
    sendDrop(2, Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":?}");
    assertNextCycle(7);
    assertActNow();
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\","
        + "\"items\":["
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"},"
        + "{\"objectType\":\"empty\"}]}");
    assertNextCycle(7);
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


}
