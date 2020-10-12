package sopra.model.entities.items;

import java.util.Optional;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.Character;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Weapon extends Item implements JSONSerializable<JSONObject> {

  private final String name;
  private final int level;
  private int range;
  private int damage;
  private final Optional<Character.CharacterSkill> effect;

  Weapon(final String name, final int level, final int range) {
    this(name, level, range, getBaseDamage(level), Optional.empty());
  }

  Weapon(final String name, final int level, final int range,
         final Optional<Character.CharacterSkill> effect) {
    this(name, level, range, getBaseDamage(level), effect);
  }

  Weapon(final String name, final int level, final int range,
         final int damage, final Optional<Character.CharacterSkill> effect) {
    super(EntityType.WEAPON);
    this.name = name;
    this.level = level;
    this.range = range;
    this.damage = damage;
    this.effect = effect;
  }

  public static Weapon fromJson(final JSONObject root) {
    final int level = root.getInt("level");

    return new Weapon(root.getString("name"), level, root.getInt("range"),
            root.getInt("damage"),
            root.has("effect") ? Optional.of(Character.CharacterSkill
                    .valueOfKey(root.getString("effect"))) : Optional.empty());
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean isLegendary() {
    return this.level == Config.SOPRA_SWORD;
  }

  public int getLevel() {
    return this.level;
  }

  public String getName() {
    return this.name;
  }

  public int getRange() {
    return this.range;
  }

  public void setRange(final int range) {
    if (range < Config.MINIMAL_ITEM_VALUE) {
      throw new IllegalArgumentException(
              "The range can not be smaller than " + Config.MINIMAL_ITEM_VALUE + ".");
    }

    this.range = range;
  }

  public int getDamage() {
    return this.damage;
  }

  public void setDamage(final int damage) {
    if (damage < Config.MINIMAL_ITEM_VALUE) {
      throw new IllegalArgumentException(
              "The damage can not be smaller than " + Config.MINIMAL_ITEM_VALUE + ".");
    }

    this.damage = damage;
  }

  public Optional<Character.CharacterSkill> getEffect() {
    return effect;
  }

  /**
   * Computes the value of the weapon.
   *
   * @return the value
   */
  private static int getBaseDamage(final int level) {
    return switch (level) {
      case 1 -> 1;
      case 2 -> 2;
      case 3 -> 4;
      case 4 -> 6;
      case 5 -> 8;
      case 6 -> 10;
      case 7 -> 12;
      case 8 -> 14;
      case 9 -> 16;
      case 10 -> 18;
      case 42 -> 20;
      default -> throw new ServerError();
    };
  }

  @Override
  public JSONObject toJSON() {
    final JSONObject weapon = new JSONObject().put("level", this.level).put("name", this.name)
        .put("range", this.range).put("damage", this.damage);

    if (this.effect.isPresent()) {
      weapon.put("effect", effect.get().toJSON());
    }

    return weapon;
  }
}
