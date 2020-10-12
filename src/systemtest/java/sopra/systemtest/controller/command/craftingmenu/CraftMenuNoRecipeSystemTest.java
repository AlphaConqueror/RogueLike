package sopra.systemtest.controller.command.craftingmenu;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class CraftMenuNoRecipeSystemTest extends DefaultSystemTest {

  public CraftMenuNoRecipeSystemTest() {
    super(CraftMenuNoRecipeSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/CraftMenuRecipeNoMaterial.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendCraftingMenu(Direction.NORTH_WEST);
    assertChoiceWindow("{\"craft\":[],\"listType\":\"craft\"}");
    assertNextCycle(0);
    assertActNow();
  }
}
