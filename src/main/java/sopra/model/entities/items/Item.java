package sopra.model.entities.items;

import java.util.Optional;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.model.entities.Character.CharacterSkill;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public abstract class Item extends Entity implements JSONSerializable<JSONObject> {

  protected Item(final EntityType type) {
    super(type);
  }

  public boolean isStackable(final Item item) {
    return false;
  }

  public boolean remove(final int amount) {
    return true;
  }

  public Item spawn() {
    return this;
  }

  public void stack(final Item item) {
    throw new UnsupportedOperationException("Normal items cannot be stacked!");
  }

  @Override
  public abstract JSONObject toJSON();

  public static final class ItemFactory {

    private ItemFactory() {
      // empty
    }

    public static Armor createArmor(final int level) {
      return new Armor("armor", level);
    }

    public static Armor createArmor(final String name, final int level,
                                    final Optional<CharacterSkill> effect) {
      return new Armor(name, level, effect);
    }

    public static Decoction createDecoction(final String name, final CharacterSkill skill) {
      return new Decoction(name, skill);
    }

    public static Decoction createDecoction(final CharacterSkill skill) {
      return new Decoction("decoction of " + skill.toJSON(), skill);
    }

    public static Potion createPotion(final int level) {
      return new Potion("potion", level);
    }

    public static Potion createPotion(final String name, final int level) {
      return new Potion(name, level);
    }

    public static Weapon createSOPRASword() {
      return new Weapon("sword", Config.SOPRA_SWORD, 1);
    }

    public static Weapon createSword(final int level) {
      return new Weapon("sword", level, 1);
    }

    public static SwordPart createSwordPart() {
      return new SwordPart();
    }

    public static Weapon createWeapon(final String name, final int level,
                                      final int range, final Optional<CharacterSkill> effect) {
      return new Weapon(name, level, range, effect);
    }
  }
}
