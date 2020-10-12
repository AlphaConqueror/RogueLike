package sopra.systemtest.controller.command.trunk;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class Trunk2StackableItemsSystemTest extends DefaultSystemTest {

  public Trunk2StackableItemsSystemTest() {
    super(Trunk2StackableItemsSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/Trunk2StackableItems.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertNextCycle(2);
    assertActNow();

    sendDrop(1, Direction.NORTH_WEST);
    assertNextCycle(3);
    assertActNow();

    sendTrunk(Direction.NORTH_WEST);
    assertChoiceWindow("{\"trunk\":[{\"material\":{\"stackSize\":2,\"type\":\"steel\"},"
            + "\"objectType\":\"material\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"}],"
            + "\"listType\":\"trunk\"}");
    assertNextCycle(3);
    assertActNow();
  }
}
