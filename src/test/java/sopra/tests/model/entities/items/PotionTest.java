package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.controller.Config;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Potion;

class PotionTest {

  @Test
  void fromJsonTest() {
    final JSONObject potionObject = new JSONObject()
            .put("name", "potion").put("level", 1)
            .put("value", 5);
    final Potion potion = Potion.fromJson(potionObject);

    assertEquals("potion", potion.getName());
    assertEquals(1, potion.getLevel());
    assertEquals(5, potion.getValue());
  }

  @Test
  void simpleSetValueTest() {
    final Potion potion = Item.ItemFactory.createPotion(1);

    potion.setValue(10);
    assertEquals(10, potion.getValue());
  }

  @Test
  void setValueBelowMinimalTest() {
    final Potion potion = Item.ItemFactory.createPotion(1);

    assertThrows(IllegalArgumentException.class,
            () -> potion.setValue(Config.MINIMAL_ITEM_VALUE - 1));
  }

  @Test
  void simpleCanStackTest() {
    final Potion potion1 = Item.ItemFactory.createPotion(1);
    final Potion potion2 = Item.ItemFactory.createPotion(1);

    assertTrue(potion1.canStack(potion2));
    assertTrue(potion2.canStack(potion1));
  }

  @Test
  void canStackDifferentNameTest() {
    final Potion potion1 = Item.ItemFactory.createPotion("potion", 1);
    final Potion potion2 = Item.ItemFactory.createPotion("test", 1);

    assertFalse(potion1.canStack(potion2));
    assertFalse(potion2.canStack(potion1));
  }

  @Test
  void canStackDifferentLevelTest() {
    final Potion potion1 = Item.ItemFactory.createPotion(1);
    final Potion potion2 = Item.ItemFactory.createPotion(2);

    assertFalse(potion1.canStack(potion2));
    assertFalse(potion2.canStack(potion1));
  }

  @Test
  void canStackDifferentValueTest() {
    final Potion potion1 = Item.ItemFactory.createPotion(1);
    final Potion potion2 = Item.ItemFactory.createPotion(1);

    potion2.setValue(10);

    assertFalse(potion1.canStack(potion2));
    assertFalse(potion2.canStack(potion1));
  }

  @Test
  void checkToJSONTest() {
    final Potion potion = Item.ItemFactory.createPotion(1);

    assertEquals("{\"stackSize\":1,\"level\":1,\"name\":\"potion\",\"value\":5}",
            potion.toJSON().toString());
  }
}