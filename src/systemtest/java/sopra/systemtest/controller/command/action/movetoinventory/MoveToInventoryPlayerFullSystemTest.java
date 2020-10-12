package sopra.systemtest.controller.command.action.movetoinventory;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class MoveToInventoryPlayerFullSystemTest extends DefaultSystemTest {

  public MoveToInventoryPlayerFullSystemTest() {
    super(MoveToInventoryPlayerFullSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/PlayerInventoryFull.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.NORTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-19,\"z\":14},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendPickUp(Direction.SOUTH_EAST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-21,\"z\":16},\"representation\":.}");
    assertNextCycle(2);
    assertActNow();

    sendDrop(1, Direction.EAST);
    assertNextCycle(3);
    assertActNow();

    sendMove(Direction.NORTH_WEST);
    assertEvents(39);
    assertNextCycle(4);
    assertActNow();

    sendPickUp(Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertNextCycle(5);
    assertActNow();

    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-18,\"z\":14},\"representation\":.}");
    assertNextCycle(6);
    assertActNow();

    sendPickUp(Direction.NORTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":5,\"y\":-18,\"z\":13},\"representation\":.}");
    assertNextCycle(7);
    assertActNow();

    sendPickUp(Direction.NORTH_EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-19,\"z\":13},\"representation\":.}");
    assertNextCycle(8);
    assertActNow();

    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-20,\"z\":14},\"representation\":.}");
    assertNextCycle(9);
    assertActNow();

    sendMove(Direction.SOUTH_EAST);
    assertEvents(39);
    assertNextCycle(10);
    assertActNow();

    sendPickUp(Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-20,\"z\":16},\"representation\":.}");
    assertNextCycle(11);
    assertActNow();

    sendMove(Direction.SOUTH_EAST);
    assertEvents(39);
    assertNextCycle(12);
    assertActNow();

    sendPickUp(Direction.SOUTH_WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-21,\"z\":17},\"representation\":.}");
    assertNextCycle(13);
    assertActNow();

    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-22,\"z\":16},\"representation\":.}");
    assertNextCycle(14);
    assertActNow();

    sendTrunk(Direction.NORTH_EAST);
    assertChoiceWindow("{\"trunk\":[{\"jewel\":{\"stackSize\":1,\"type\":\"amethyst\"},"
            + "\"objectType\":\"jewel\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}],"
            + "\"listType\":\"trunk\"}");
    assertNextCycle(14);
    assertActNow();

    sendMoveToInventory(1, Direction.NORTH_EAST);
    assertCommandFailed();
    assertNextCycle(15);
    assertActNow();
  }
}
