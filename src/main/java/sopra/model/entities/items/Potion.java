package sopra.model.entities.items;

import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Potion extends Stackable implements JSONSerializable<JSONObject> {

  private final String name;
  private final int level;
  private int value;

  Potion(final String name, final int level) {
    this(name, level, getBaseValue(level));
  }

  Potion(final String name, final int level, final int value) {
    super(EntityType.POTION, Config.MIN_STACK);
    this.name = name;
    this.level = level;
    this.value = value;
  }

  public static Potion fromJson(final JSONObject root) {
    return new Potion(root.getString("name"), root.getInt("level"), root.getInt("value"));
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public String getName() {
    return name;
  }

  public int getLevel() {
    return this.level;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(final int value) {
    if (value < Config.MINIMAL_ITEM_VALUE) {
      throw new IllegalArgumentException(
              "The value can not be smaller than " + Config.MINIMAL_ITEM_VALUE + ".");
    }

    this.value = value;
  }

  private static int getBaseValue(final int level) {
    return level * 5;
  }

  @Override
  public boolean canStack(final Item item) {
    final Potion potion = (Potion) item;

    return potion.getName().equals(this.name) && potion.getLevel() == this.level
            && potion.getValue() == this.value;
  }

  @Override
  public Potion spawn() {
    return new Potion(name, this.level, this.value);
  }

  @Override
  public JSONObject toJSON() {
    return super.toJSON().put("name", this.name).put("level", this.level).put("value", this.value);
  }
}
