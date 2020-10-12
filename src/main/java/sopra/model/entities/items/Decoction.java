package sopra.model.entities.items;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.controller.Config;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.Character;
import sopra.model.entities.Character.CharacterSkill;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Decoction extends Stackable implements JSONSerializable<JSONObject> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Decoction.class);
  private final String name;
  private final CharacterSkill skill;
  private int duration;

  public Decoction(final String name, final CharacterSkill skill) {
    this(name, skill, Config.DURATION);
  }

  public Decoction(final String name, final CharacterSkill skill, final int duration) {
    super(EntityType.DECOCTION, Config.MIN_STACK);
    this.name = name;
    this.skill = skill;
    this.duration = duration;
  }

  public static Decoction fromJson(final JSONObject root) {
    return new Decoction(root.getString("name"), switch (root.getString("skill")) {
      case "strength" -> Character.CharacterSkill.STRENGTH;
      case "vitality" -> Character.CharacterSkill.VITALITY;
      case "agility" -> Character.CharacterSkill.AGILITY;
      case "luck" -> Character.CharacterSkill.LUCK;
      default -> throw new JSONException("Unexpected value: " + root.getString("skill"));
    }, root.getInt("duration"));
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public String getName() {
    return name;
  }

  public int getDuration() {
    return this.duration;
  }

  public void setDuration(final int duration) {
    if (duration < Config.MINIMAL_ITEM_VALUE) {
      throw new IllegalArgumentException(
              "The duration can not be smaller than " + Config.MINIMAL_ITEM_VALUE + ".");
    }

    this.duration = duration;
  }

  public Character.CharacterSkill getSkill() {
    return this.skill;
  }

  @Override
  public boolean canStack(final Item item) {
    final Decoction decoction = (Decoction) item;

    return decoction.getName().equals(this.name) && decoction.getSkill() == this.skill;
  }

  @Override
  public Decoction spawn() {
    return new Decoction(this.name, this.skill, this.duration);
  }

  /**
   * Reduces and disables decoction.
   */
  public void tick() {
    this.duration--;
    LOGGER.debug("tick {} -> {}", this.skill, this.duration);
    if (this.duration < 0) {
      this.disable();
    }
  }

  @Override
  public JSONObject toJSON() {
    return super.toJSON().put("name", this.name)
            .put("skill", this.skill.toJSON()).put("duration", this.duration);
  }
}