package sopra.model.entities;

import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import sopra.controller.Die;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.Coordinate;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Jewel;
import sopra.model.entities.items.Material;
import sopra.model.entities.items.Potion;
import sopra.model.entities.items.Recipe;
import sopra.model.entities.items.SwordPart;
import sopra.model.entities.items.Weapon;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.1
 */
public abstract class Entity {

  private final EntityType type;
  private boolean enabled;
  private Optional<Coordinate> position;

  protected Entity(final EntityType type, final boolean enabled) {
    this.type = type;
    this.position = Optional.empty();
    this.enabled = enabled;
  }

  protected Entity(final EntityType type) {
    this(type, true);
  }

  public static Entity fromJson(final JSONObject root, final Die die) {
    return switch (root.getString("objectType")) {
      case "chest" -> Chest.fromJson();
      case "player" -> Player.fromJson(root.getJSONObject("player"));
      case "monster" -> Monster.fromJson(root.getJSONObject("monster"), die);
      case "weapon" -> Weapon.fromJson(root.getJSONObject("weapon"));
      case "armor" -> Armor.fromJson(root.getJSONObject("armor"));
      case "potion" -> Potion.fromJson(root.getJSONObject("potion"));
      case "decoction" -> Decoction.fromJson(root.getJSONObject("decoction"));
      case "swordPart" -> SwordPart.fromJson();
      case "material" -> Material.fromJson(root.getJSONObject("material"));
      case "jewel" -> Jewel.fromJson(root.getJSONObject("jewel"));
      case "table" -> Workbench.fromJson();
      case "cauldron" -> Cauldron.fromJson();
      case "recipe" -> Recipe.fromJson(root.getJSONObject("recipe"));
      case "trunk" -> Trunk.fromJson();
      default -> throw new JSONException("Unexpected value: " + root.getString("objectType"));
    };
  }

  public abstract <T> T accept(final EntityVisitor<T> visitor);

  public void disable() {
    this.enabled = false;
  }

  public void enable() {
    this.enabled = true;
  }

  public Optional<Coordinate> getPosition() {
    return this.position;
  }

  public void setPosition(final Coordinate position) {
    this.position = Optional.ofNullable(position);
  }

  public String getRepresentation() {
    return this.getType().getRepresentation();
  }

  public EntityType getType() {
    return this.type;
  }

  public boolean isDisabled() {
    return !this.enabled;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public boolean isLegendary() {
    return false;
  }
}
