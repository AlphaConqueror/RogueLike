package sopra.systemtest.controller.command.craftingmenu;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class CraftMenuNonCraftableRecipeSystemTest extends DefaultSystemTest {

  public CraftMenuNonCraftableRecipeSystemTest() {
    super(CraftMenuNonCraftableRecipeSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/CraftMenuRecipeNoMaterial.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendCraftingMenu(Direction.NORTH_WEST);
    assertChoiceWindow("{\"craft\":[],\"listType\":\"craft\"}");
    assertNextCycle(1);
    assertActNow();
  }
}
