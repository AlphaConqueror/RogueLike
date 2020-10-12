package sopra.systemtest.controller.command.trunk;

import static sopra.systemtest.api.Utils.loadResource;

import sopra.comm.Direction;
import sopra.comm.TimeoutException;
import sopra.systemtest.DefaultSystemTest;

public class EmptyTrunkSystemTest extends DefaultSystemTest {

  public EmptyTrunkSystemTest() {
    super(EmptyTrunkSystemTest.class, false);
  }

  @Override
  protected String createWorld() {
    return loadResource(this.getClass(), "maps/TrunkSimpleItems.json");
  }

  @Override
  protected void execute() throws TimeoutException {
    sendTrunk(Direction.NORTH_WEST);
    assertChoiceWindow("{\"trunk\":[{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},{\"objectType\":\"empty\"},"
            + "{\"objectType\":\"empty\"}],\"listType\":\"trunk\"}");
    assertNextCycle(0);
    assertActNow();
  }
}
