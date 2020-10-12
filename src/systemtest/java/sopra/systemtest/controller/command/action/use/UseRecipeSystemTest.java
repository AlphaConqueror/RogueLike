package sopra.systemtest.controller.command.action.use;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class UseRecipeSystemTest extends DefaultSystemTest {

  public UseRecipeSystemTest() {
    super(UseRecipeSystemTest.class, false);
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

    sendUse(1);
    assertCommandFailed();
    assertNextCycle(2);
    assertActNow();
  }
}
