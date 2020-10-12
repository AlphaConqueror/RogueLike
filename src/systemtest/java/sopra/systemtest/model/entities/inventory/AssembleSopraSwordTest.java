package sopra.systemtest.model.entities.inventory;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.api.SystemTest;

public class AssembleSopraSwordTest extends SystemTest {
  public AssembleSopraSwordTest() {
    super(AssembleSopraSwordTest.class, false);
  }

  @Override
  protected long createSeed() {
    return 0;
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/inventoryTestMap.json");
  }

  protected void init() throws TimeoutException {
    sendRegister();
    assertGameStarted();
    assertSetCamera(0, -23, 23);
    assertDrawWorld("Island");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-23,\"z\":21},\"representation\":\"1\"})");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-24,\"z\":21},\"representation\":\"2\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-25,\"z\":21},\"representation\":\"3\"}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-26,\"z\":21},\"representation\":\"4\"}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-27,\"z\":21},\"representation\":\"7\"}");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-23,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-24,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-25,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-26,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-27,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-28,\"z\":22},\"representation\":\"#\"}");
    assertUpdateWorld("{\"position\":{\"x\":0,\"y\":-23,\"z\":23},\"representation\":\"@\"}");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-24,\"z\":23},\"representation\":\"?\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-25,\"z\":23},\"representation\":\"+\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-26,\"z\":23},\"representation\":\"+\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-27,\"z\":23},\"representation\":\"+\"}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-28,\"z\":23},\"representation\":\"+\"}");
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-29,\"z\":23},\"representation\":\"+\"}");
    assertUpdateWorld("{\"position\":{\"x\":0,\"y\":-24,\"z\":24},\"representation\":\".\"})");
    assertUpdateWorld("{\"position\":{\"x\":1,\"y\":-25,\"z\":24},\"representation\":\".\"}");
    assertUpdateWorld("{\"position\":{\"x\":2,\"y\":-26,\"z\":24},\"representation\":\".\"}");
    assertUpdateWorld("{\"position\":{\"x\":3,\"y\":-27,\"z\":24},\"representation\":\".\"}");
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-28,\"z\":24},\"representation\":\".\"}");
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-29,\"z\":24},\"representation\":\".\"}");
    assertUpdatePlayer("{\"currentHealth\":20,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");
    assertNextCycle(0);
    assertActNow();

  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    init();
    // pick up a sword part
    sendPickUp(Direction.NORTH_EAST);
    assertEvent();
    assertNextCycle(1);
    assertActNow();
    sendMove(Direction.NORTH_EAST);
    //redrawing map after move
    for (int j = 0; j < 30; j++) {
      assertEvent();
    }
    //Keep walking & filling inventory
    int count = 3;
    int fieldsVisible = 32;
    for (int i = 1; i <= 6; i++) {
      sendPickUp(Direction.EAST);
      //the update World when item is picked up
      assertEvent();
      assertNextCycle(count);
      count++;
      assertActNow();
      sendMove(Direction.EAST);
      if (i == 6) {
        fieldsVisible -= 2;
      }
      //redrawing map after move
      for (int j = 0; j < fieldsVisible; j++) {
        assertEvent();
      }
      if (fieldsVisible < 50) {
        fieldsVisible += 4;
      }



      assertNextCycle(count);
      count++;
      assertActNow();
    }

    // try to use them
    sendUse(1);
    // we should now have a sopra sword!
    assertUpdatePlayer("{\"currentHealth\":20,\"weapon\":"
            + "{\"damage\":20,\"level\":42,\"name\":\"sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,\"name\":\"Shield\"},"
            + "\"luck\":1,\"strength\":1,\"level\":1,\"skillPoints\":0,"
            + "\"vitality\":1,\"name\":\"Player\",\"maxHealth\":20,"
            + "\"agility\":1,\"experience\":0}");
    assertNextCycle(15);
    assertActNow();
    // sword parts should be gone from inventory
    sendInventory();
    assertChoiceWindow("{\"listType\":\"items\",\"items\":["
            + "{\"weapon\":{\"damage\":1,\"level\":1,\"name\":\"Sword\","
            + "\"range\":1},\"objectType\":\"weapon\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}]}");
    assertNextCycle(15);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}

