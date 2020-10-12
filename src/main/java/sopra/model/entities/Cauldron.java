package sopra.model.entities;

import sopra.controller.visitor.EntityVisitor;

public final class Cauldron extends Entity {

  private Cauldron() {
    super(EntityType.CAULDRON);
  }

  public static Cauldron fromJson() {
    return new Cauldron();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
