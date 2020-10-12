package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.controller.Config;
import sopra.model.entities.Character;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Item;

class ArmorTest {

  @Test
  void simpleFromJsonTest() {
    final JSONObject armorJson = new JSONObject()
            .put("name", "armor").put("level", 1)
            .put("armor", 2);
    final Armor armor = Armor.fromJson(armorJson);


    assertEquals("armor", armor.getName());
    assertEquals(1, armor.getLevel());
    assertEquals(2, armor.getResistance());
  }

  @Test
  void fromJsonEffectTest() {
    final JSONObject armorJson = new JSONObject()
            .put("name", "armor").put("level", 1)
            .put("armor", 1);

    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      armorJson.put("effect", effect.toJSON());

      final Armor armor = Armor.fromJson(armorJson);


      assertEquals("armor", armor.getName());
      assertEquals(1, armor.getLevel());
      assertEquals(1, armor.getResistance());
      assertTrue(armor.getEffect().isPresent());
      assertEquals(effect.toJSON(), armor.getEffect().get().toJSON());
    }
  }

  @Test
  void fromJsonFullTest() {
    final JSONObject armorJson = new JSONObject()
            .put("name", "armor").put("level", 1)
            .put("armor", 2);

    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      armorJson.put("effect", effect.toJSON());

      final Armor armor = Armor.fromJson(armorJson);


      assertEquals("armor", armor.getName());
      assertEquals(1, armor.getLevel());
      assertEquals(2, armor.getResistance());
      assertTrue(armor.getEffect().isPresent());
      assertEquals(effect.toJSON(), armor.getEffect().get().toJSON());
    }
  }

  @Test
  void simpleSetResistanceTest() {
    final Armor armor = Item.ItemFactory.createArmor(1);

    armor.setResistance(3);
    assertEquals(3, armor.getResistance());
  }

  @Test
  void setResistanceBelowMinimalTest() {
    final Armor armor = Item.ItemFactory.createArmor(1);

    assertThrows(IllegalArgumentException.class,
            () -> armor.setResistance(Config.MINIMAL_ITEM_VALUE - 1));
  }

  @Test
  void simpleToJSONTest() {
    final Armor armor = Item.ItemFactory.createArmor(1);

    assertEquals("{\"armor\":1,\"level\":1,\"name\":\"armor\"}",
            armor.toJSON().toString());
  }

  @Test
  void checkToJSONEffectTest() {
    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      final Armor armor = Item.ItemFactory.createArmor("armor", 1, Optional.of(effect));

      assertEquals("{\"armor\":1,\"level\":1,\"effect\":\""
              + effect.toJSON() + "\",\"name\":\"armor\"}", armor.toJSON().toString());
    }
  }
}
