package sopra.systemtest.controller.command.craftingmenu;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class CraftMenuMaterialFirstSystemTest extends DefaultSystemTest {

  public CraftMenuMaterialFirstSystemTest() {
    super(CraftMenuMaterialFirstSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/CraftMenuSimpleRecipe.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendPickUp(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":.}");
    assertNextCycle(1);
    assertActNow();

    sendPickUp(Direction.EAST);
    assertUpdateWorld("{\"position\":{\"x\":6,\"y\":-21,\"z\":15},\"representation\":.}");
    assertNextCycle(2);
    assertActNow();

    sendCraftingMenu(Direction.NORTH_WEST);
    assertChoiceWindow("{\"craft\":[{\"recipe\":{\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"name\":\"sword\",\"range\":1},"
            + "\"steel\":1,\"diamond\":0,\"recipeType\":\"weapon\","
            + "\"wood\":0,\"beetleshell\":0,\"amethyst\":0,\"leather\":0,"
            + "\"emerald\":0,\"herbs\":0,\"ruby\":0},\"objectType\":\"recipe\"}],"
            + "\"listType\":\"craft\"}");

    assertNextCycle(2);
    assertActNow();
  }
}
