package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.controller.Config;
import sopra.model.entities.Character;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Weapon;

class WeaponTest {

  @Test
  void simpleFromJsonTest() {
    final JSONObject weaponObject = new JSONObject()
            .put("name", "sword").put("level", 1)
            .put("range", 1).put("damage", 2);
    final Weapon weapon = Weapon.fromJson(weaponObject);

    assertEquals("sword", weapon.getName());
    assertEquals(1, weapon.getLevel());
    assertEquals(1, weapon.getRange());
    assertEquals(2, weapon.getDamage());
    assertEquals(Optional.empty(), weapon.getEffect());
  }

  @Test
  void fromJsonEffectTest() {
    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      final JSONObject weaponObject = new JSONObject()
              .put("name", "sword").put("level", 1)
              .put("range", 1).put("damage", 1)
              .put("effect", effect.toJSON());
      final Weapon weapon = Weapon.fromJson(weaponObject);

      assertEquals("sword", weapon.getName());
      assertEquals(1, getLevel(weapon));
      assertEquals(1, getRange(weapon));
      assertEquals(1, weapon.getDamage());
      assertTrue(weapon.getEffect().isPresent());
      assertEquals(effect, weapon.getEffect().get());
    }
  }

  @Test
  void fromJsonFullTest() {
    for (final Character.CharacterSkill effect
            : Character.CharacterSkill.values()) {
      final JSONObject weaponObject = new JSONObject()
              .put("name", "sword").put("level", 1)
              .put("range", 1).put("damage", 2)
              .put("effect", effect.toJSON());
      final Weapon weapon = Weapon.fromJson(weaponObject);

      assertEquals("sword", weapon.getName());
      assertEquals(1, getLevel(weapon));
      assertEquals(1, getRange(weapon));
      assertEquals(2, weapon.getDamage());
      assertTrue(weapon.getEffect().isPresent());
      assertEquals(effect, weapon.getEffect().get());
    }
  }

  private int getLevel(final Weapon weapon) {
    return weapon.getLevel();
  }

  private int getRange(final Weapon weapon) {
    return weapon.getRange();
  }

  @Test
  void simpleSetRangeTest() {
    final Weapon weapon = Item.ItemFactory.createSword(1);

    weapon.setRange(5);
    assertEquals(5, weapon.getRange());
  }

  @Test
  void setRangeBelowMinimalTest() {
    final Weapon weapon = Item.ItemFactory.createSword(1);

    assertThrows(IllegalArgumentException.class,
            () -> weapon.setRange(Config.MINIMAL_ITEM_VALUE - 1));
  }

  @Test
  void simpleSetDamageTest() {
    final Weapon weapon = Item.ItemFactory.createSword(1);

    weapon.setDamage(5);
    assertEquals(5, weapon.getDamage());
  }

  @Test
  void setDamageBelowMinimalTest() {
    final Weapon weapon = Item.ItemFactory.createSword(1);

    assertThrows(IllegalArgumentException.class,
            () -> weapon.setDamage(Config.MINIMAL_ITEM_VALUE - 1));
  }

  @Test
  void simpleToJSONTest() {
    final Weapon weapon = Item.ItemFactory.createSword(1);

    assertEquals("{\"damage\":1,\"level\":1,\""
            + "name\":\"sword\",\"range\":1}",
            weapon.toJSON().toString());
  }

  @Test
  void checkToJSONEffectTest() {
    final Weapon weapon = Item.ItemFactory.createWeapon("sword", 1,
            1, Optional.of(Character.CharacterSkill.STRENGTH));

    assertEquals("{\"damage\":1,\"level\":1,"
            + "\"effect\":\"strength\",\"name\":\"sword\",\"range\":1}",
            weapon.toJSON().toString());
  }
}