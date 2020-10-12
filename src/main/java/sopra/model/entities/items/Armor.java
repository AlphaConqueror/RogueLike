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
public class Armor extends Item implements JSONSerializable<JSONObject> {

  private final String name;
  private final int level;
  private int resistance;
  private final Optional<Character.CharacterSkill> effect;

  Armor(final String name, final int level) {
    this(name, level, getBaseResistance(level), Optional.empty());
  }

  Armor(final String name, final int level, final Optional<Character.CharacterSkill> effect) {
    this(name, level, getBaseResistance(level), effect);
  }

  Armor(final String name, final int level, final int resistance,
        final Optional<Character.CharacterSkill> effect) {
    super(EntityType.ARMOR);
    this.level = level;
    this.name = name;
    this.resistance = resistance;
    this.effect = effect;
  }

  public static Armor fromJson(final JSONObject root) {
    final int level = root.getInt("level");

    return new Armor(root.getString("name"), level,
            root.getInt("armor"),
            root.has("effect") ? Optional.of(Character.CharacterSkill
                    .valueOfKey(root.getString("effect"))) : Optional.empty());
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public int getLevel() {
    return this.level;
  }

  public String getName() {
    return this.name;
  }

  public int getResistance() {
    return this.resistance;
  }

  public void setResistance(final int resistance) {
    if (resistance < Config.MINIMAL_ITEM_VALUE) {
      throw new IllegalArgumentException(
              "The value of the resistance can not be smaller than "
                      + Config.MINIMAL_ITEM_VALUE + ".");
    }

    this.resistance = resistance;
  }

  public Optional<Character.CharacterSkill> getEffect() {
    return effect;
  }

  /**
   * Computes the armor value.
   *
   * @return the armor value
   */
  private static int getBaseResistance(final int level) {
    return switch (level) {
      case 1 -> 1;
      case 2 -> 2;
      case 3 -> 5;
      case 4 -> 9;
      case 5 -> 14;
      case 6 -> 20;
      case 7 -> 27;
      case 8 -> 35;
      case 9 -> 40;
      case 10 -> 45;
      default -> throw new ServerError();
    };
  }

  @Override
  public JSONObject toJSON() {
    final JSONObject armor = new JSONObject().put("level", this.level).put("name", this.name)
            .put("armor", this.resistance);

    effect.ifPresent(characterSkill -> armor.put("effect", characterSkill.toJSON()));

    return armor;
  }
}
