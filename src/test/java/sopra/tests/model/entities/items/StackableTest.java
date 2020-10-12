package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sopra.controller.Config;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.EntityType;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Stackable;
import sopra.utils.Utils;

class StackableTest {

  @Test
  void simpleCanStackTest() {
    final TestStackable test1 = new TestStackable(0);
    final TestStackable test2 = new TestStackable(0);

    assertTrue(test1.isStackable(test2), Utils.substitute("Tried to stack 2 items of the same type,"
            + " with the same parameters."));
    assertTrue(test2.isStackable(test1), Utils.substitute("Tried to stack 2 items of the same type,"
            + " with the same parameters."));
  }

  @Test
  void canStackDifferentParametersTest() {
    final TestStackable test1 = new TestStackable(0);
    final TestStackable test2 = new TestStackable(1);

    assertFalse(test1.isStackable(test2),
            Utils.substitute("Tried to stack 2 items of the same type,"
            + " with different parameters."));
    assertFalse(test2.isStackable(test1),
            Utils.substitute("Tried to stack 2 items of the same type,"
            + " with different parameters."));
  }

  @Test
  void canStackDifferentTypesTest() {
    final TestStackable test = new TestStackable(0);
    final Item item = Mockito.mock(Armor.class);

    assertFalse(test.isStackable(item),
            Utils.substitute("Tried to stack 2 items of different types."));
    assertFalse(item.isStackable(test),
            Utils.substitute("Tried to stack 2 items of different type."));
  }

  private static class TestStackable extends Stackable {

    private final int id;

    TestStackable(final int id) {
      super(EntityType.RECIPE, Config.MIN_STACK);
      this.id = id;
    }

    @Override
    public <T> T accept(final EntityVisitor<T> visitor) {
      return null;
    }

    @Override
    protected boolean canStack(final Item item) {
      final TestStackable testStackable = (TestStackable) item;

      return this.id == testStackable.id;
    }
  }
}
