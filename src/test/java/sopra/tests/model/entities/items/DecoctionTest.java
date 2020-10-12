package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.controller.Config;
import sopra.model.entities.Character;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Item;

class DecoctionTest {

  @Test
  void simpleFromJsonTest() {
    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      final JSONObject decoctionJson = new JSONObject()
              .put("name", "decoction of " + effect.toJSON())
              .put("skill", effect.toJSON()).put("duration", Config.DURATION);
      final Decoction decoction = Decoction.fromJson(decoctionJson);

      assertEquals("decoction of " + effect.toJSON(), decoction.getName());
      assertEquals(effect.toJSON(), decoction.getSkill().toJSON());
      assertEquals(Config.DURATION, decoction.getDuration());
    }
  }

  @Test
  void fromJsonIllegalEffectTest() {
    final JSONObject decoctionJson = new JSONObject()
            .put("name", "decoction of test")
            .put("skill", "test").put("duration", Config.DURATION);

    assertThrows(JSONException.class, () -> Decoction.fromJson(decoctionJson));

  }

  @Test
  void simpleSetDurationTest() {
    final Decoction decoction
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.STRENGTH);

    decoction.setDuration(Config.DURATION + 10);
    assertEquals(Config.DURATION + 10, decoction.getDuration());
  }

  @Test
  void setDurationBelowMinimalTest() {
    final Decoction decoction
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.STRENGTH);

    assertThrows(IllegalArgumentException.class,
            () -> decoction.setDuration(Config.MINIMAL_ITEM_VALUE - 1));
  }

  @Test
  void canStackLegalTest() {
    final Decoction decoction1
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.STRENGTH);
    final Decoction decoction2
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.STRENGTH);

    assertTrue(decoction1.canStack(decoction2));
    assertTrue(decoction2.canStack(decoction1));
  }

  @Test
  void canStackIllegalTest() {
    final Decoction decoction1
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.STRENGTH);
    final Decoction decoction2
            = Item.ItemFactory.createDecoction(Character.CharacterSkill.VITALITY);

    assertFalse(decoction1.canStack(decoction2));
    assertFalse(decoction2.canStack(decoction1));
  }

  @Test
  void checkToJSONTest() {
    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      final Decoction decoction = Item.ItemFactory.createDecoction(effect);

      assertEquals("{\"duration\":" + Config.DURATION + ",\"stackSize\":1,\"skill\":\""
              + effect.toJSON() + "\",\"name\":\"decoction of "
              + effect.toJSON() + "\"}", decoction.toJSON().toString());
    }
  }
}