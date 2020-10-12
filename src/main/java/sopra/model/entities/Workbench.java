package sopra.model.entities;

import sopra.controller.visitor.EntityVisitor;

public final class Workbench extends Entity {

  private Workbench() {
    super(EntityType.WORKBENCH);
  }

  public static Workbench fromJson() {
    return new Workbench();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
