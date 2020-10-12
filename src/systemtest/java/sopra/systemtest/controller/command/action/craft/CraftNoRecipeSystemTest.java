package sopra.systemtest.controller.command.action.craft;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class CraftNoRecipeSystemTest extends DefaultSystemTest {

  public CraftNoRecipeSystemTest() {
    super(CraftNoRecipeSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/CraftMenuSimpleRecipe.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendCraft(1, Direction.NORTH_WEST);
    assertCommandFailed();
    assertNextCycle(1);
    assertActNow();
  }
}
