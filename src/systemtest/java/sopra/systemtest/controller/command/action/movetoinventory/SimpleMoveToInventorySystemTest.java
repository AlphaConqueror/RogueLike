package sopra.systemtest.controller.command.action.movetoinventory;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class SimpleMoveToInventorySystemTest extends DefaultSystemTest {

  public SimpleMoveToInventorySystemTest() {
    super(SimpleMoveToInventorySystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/TrunkSimpleItems.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendDrop(1, Direction.NORTH_WEST);
    assertNextCycle(2);
    assertActNow();

    sendTrunk(Direction.NORTH_WEST);
    assertChoiceWindow("{\"trunk\":[{\"material\":{\"stackSize\":1,\"type\":\"steel\"},"
            + "\"objectType\":\"material\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}],"
            + "\"listType\":\"trunk\"}");
    assertNextCycle(2);
    assertActNow();

    sendMoveToInventory(1, Direction.NORTH_WEST);
    assertNextCycle(3);
    assertActNow();
  }
}
