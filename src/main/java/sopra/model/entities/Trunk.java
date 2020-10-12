package sopra.model.entities;

import java.util.Optional;
import org.json.JSONObject;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.entities.items.Item;

public final class Trunk extends Entity {

  private final Inventory inventory = new Inventory();

  public Trunk() {
    super(EntityType.TRUNK);
  }

  public static Trunk fromJson() {
    return new Trunk();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public Optional<Item> getItem(final int index) {
    return this.inventory.get(index);
  }

  public boolean addItem(final Item item) {
    return this.inventory.add(item);
  }

  public void clearIndex(final int index) {
    this.inventory.set(index, Optional.empty());
  }

  public JSONObject inventoryJSON() {
    return this.inventory.toJSON();
  }
}
