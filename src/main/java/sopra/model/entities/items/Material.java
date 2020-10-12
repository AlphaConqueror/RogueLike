package sopra.model.entities.items;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

public class Material extends Stackable implements JSONSerializable<JSONObject> {

  public static final Material STEEL = new Material(EntityType.STEEL);
  public static final Material LEATHER = new Material(EntityType.LEATHER);
  public static final Material WOOD = new Material(EntityType.WOOD);
  public static final Material HERBS = new Material(EntityType.HERBS);
  public static final Material BEETLESHELL = new Material(EntityType.BEETLESHELL);

  public static final List<Material> NON_SPECIFIC_MATERIALS = List.of(STEEL, LEATHER, WOOD, HERBS);
  public static final List<Material> MATERIALS = List.of(STEEL, LEATHER, WOOD, HERBS, BEETLESHELL);

  Material(final EntityType type) {
    super(type, Config.MIN_STACK);

    switch (type) {
      case STEEL, LEATHER, WOOD, HERBS, BEETLESHELL -> { }
      default -> throw new IllegalArgumentException("The type is not a material.");
    }
  }

  public static Material fromJson(final JSONObject root) {
    for (final EntityType type : EntityType.values()) {
      if (type.toJSON().equals(root.getString("type"))) {
        return new Material(type);
      }
    }

    throw new JSONException("Unexpected value: " + root.getString("type"));
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public Item spawn() {
    return new Material(getType());
  }

  @Override
  public JSONObject toJSON() {
    return super.toJSON().put("type", super.getType().toJSON());
  }
}
