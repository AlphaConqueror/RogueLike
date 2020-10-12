package sopra.systemtest.controller.command.action.craft;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class CraftNonCraftableRecipeSystemTest extends DefaultSystemTest {

  public CraftNonCraftableRecipeSystemTest() {
    super(CraftNonCraftableRecipeSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/CraftMenuSimpleRecipe.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendCraft(1, Direction.NORTH_WEST);
    assertCommandFailed();
    assertNextCycle(2);
    assertActNow();
  }
}
