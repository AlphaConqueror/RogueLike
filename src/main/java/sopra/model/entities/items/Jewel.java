package sopra.model.entities.items;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

public class Jewel extends Stackable implements JSONSerializable<JSONObject> {

  public static final Jewel RUBY = new Jewel(EntityType.RUBY);
  public static final Jewel AMETHYST = new Jewel(EntityType.AMETHYST);
  public static final Jewel EMERALD = new Jewel(EntityType.EMERALD);
  public static final Jewel DIAMOND = new Jewel(EntityType.DIAMOND);

  public static final List<Jewel> JEWELS = List.of(RUBY, AMETHYST, EMERALD, DIAMOND);

  Jewel(final EntityType type) {
    super(type, Config.MIN_STACK);

    switch (type) {
      case RUBY, AMETHYST, EMERALD, DIAMOND -> { }
      default -> throw new IllegalArgumentException("The type is not a jewel.");
    }
  }

  public static Jewel fromJson(final JSONObject root) {
    for (final EntityType type : EntityType.values()) {
      if (type.toJSON().equals(root.getString("type"))) {
        return new Jewel(type);
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
    return new Jewel(getType());
  }

  @Override
  public JSONObject toJSON() {
    return super.toJSON().put("type", super.getType().toJSON());
  }
}
