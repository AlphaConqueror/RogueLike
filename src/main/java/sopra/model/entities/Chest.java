package sopra.model.entities;

import sopra.controller.visitor.EntityVisitor;

public final class Chest extends Entity {

  private Chest() {
    super(EntityType.CHEST, false);
  }

  public static Chest fromJson() {
    return new Chest();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
