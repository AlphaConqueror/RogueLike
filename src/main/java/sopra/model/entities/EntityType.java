package sopra.model.entities;

import org.jetbrains.annotations.Contract;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public enum EntityType implements JSONSerializable<String> {
  PLAYER((char) 64, ObjectType.PLAYER),
  ASSISTANT((char) 181, ObjectType.MONSTER, "assistant", 15),
  BUG((char) 164, ObjectType.MONSTER, "bug", 1),
  OVERFLOW((char) 33, ObjectType.MONSTER, "overflow", 5),
  TUTOR((char) 229, ObjectType.MONSTER, "tutor", 10),
  PROFESSOR((char) 182, ObjectType.MONSTER, "professor", 20),
  WEAPON((char) 43, ObjectType.WEAPON),
  ARMOR((char) 38, ObjectType.ARMOR),
  POTION((char) 63, ObjectType.POTION),
  DECOCTION((char) 191, ObjectType.DECOCTION),
  CHEST((char) 61, ObjectType.CHEST),
  SWORD_PART((char) 35, ObjectType.SWORD_PART),

  WOOD((char) 124, ObjectType.MATERIAL, "wood"),
  STEEL((char) 230, ObjectType.MATERIAL, "steel"),
  LEATHER((char) 152, ObjectType.MATERIAL, "leather"),
  HERBS((char) 255, ObjectType.MATERIAL, "herbs"),
  BEETLESHELL((char) 248, ObjectType.MATERIAL, "beetleshell"),

  RUBY((char) 242, ObjectType.JEWEL, "ruby"),
  AMETHYST((char) 243, ObjectType.JEWEL, "amethyst"),
  EMERALD((char) 244, ObjectType.JEWEL, "emerald"),
  DIAMOND((char) 245, ObjectType.JEWEL, "diamond"),

  WORKBENCH((char) 110, ObjectType.WORKBENCH),
  CAULDRON((char) 117, ObjectType.CAULDRON),
  TRUNK((char) 215, ObjectType.TRUNK),
  RECIPE((char) 174, ObjectType.RECIPE);

  private final char representation;
  private final ObjectType objectType;
  private final String json;
  private final int factor;

  EntityType(final char representation, final ObjectType objectType) {
    this(representation, objectType, objectType.toJSON(), 0);
  }

  EntityType(final char representation, final ObjectType objectType, final String json) {
    this(representation, objectType, json, 0);
  }

  EntityType(final char representation, final ObjectType objectType, final String json,
      final int factor) {
    this.representation = representation;
    this.objectType = objectType;
    this.json = json;
    this.factor = factor;
  }

  public ObjectType getObjectType() {
    return objectType;
  }

  public int getFactor() {
    return this.factor;
  }

  public String getRepresentation() {
    return String.valueOf(this.representation);
  }

  public boolean isCraftingMaterial() {
    return switch (this.objectType) {
      case MATERIAL, JEWEL -> true;
      default -> false;
    };
  }

  @Override
  public String toJSON() {
    return this.json;
  }

  protected enum ObjectType implements JSONSerializable<String> {
    PLAYER("player"),
    MONSTER("monster"),
    WEAPON("weapon"),
    ARMOR("armor"),
    POTION("potion"),
    DECOCTION("decoction"),
    CHEST("chest"),
    SWORD_PART("swordPart"),
    MATERIAL("material"),
    JEWEL("jewel"),
    WORKBENCH("table"),
    CAULDRON("cauldron"),
    TRUNK("trunk"),
    RECIPE("recipe");

    private final String json;

    @Contract(pure = true)
    ObjectType(final String json) {
      this.json = json;
    }

    @Override
    public String toJSON() {
      return this.json;
    }
  }
}
