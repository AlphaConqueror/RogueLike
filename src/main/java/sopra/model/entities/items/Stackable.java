package sopra.model.entities.items;

import org.json.JSONObject;
import sopra.model.entities.EntityType;
import sopra.utils.JSONSerializable;

public abstract class Stackable extends Item implements JSONSerializable<JSONObject> {

  private int stack;

  public Stackable(final EntityType type, final int stack) {
    super(type);
    this.stack = stack;
  }

  public int getStack() {
    return this.stack;
  }

  protected boolean canStack(final Item item) {
    return true;
  }

  @Override
  public boolean isStackable(final Item item) {
    return getType() == item.getType() && canStack(item);
  }

  @Override
  public boolean remove(final int amount) {
    this.stack -= amount;
    return this.stack <= 0;
  }

  @Override
  public void stack(final Item item) {
    if (this.isStackable(item)) {
      this.stack++;
    }
  }

  @Override
  public JSONObject toJSON() {
    return new JSONObject().put("stackSize", this.stack);
  }
}