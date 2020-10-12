package sopra.controller.visitor;

import sopra.model.entities.Cauldron;
import sopra.model.entities.Chest;
import sopra.model.entities.Monster;
import sopra.model.entities.Player;
import sopra.model.entities.Trunk;
import sopra.model.entities.Workbench;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Jewel;
import sopra.model.entities.items.Material;
import sopra.model.entities.items.Potion;
import sopra.model.entities.items.Recipe;
import sopra.model.entities.items.SwordPart;
import sopra.model.entities.items.Weapon;

/**
 * Provides a base class for {@link EntityVisitor} which only operate on a subset and default to
 * doing nothing.
 *
 * <p>{@link Void} is a placeholder for methods with no return value.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public abstract class DefaultEntityVisitor implements EntityVisitor<Void> {

  protected void handle(final Decoction decoction) {
    // empty
  }

  protected void handle(final Monster monster) {
    // empty
  }

  protected void handle(final Player player) {
    // empty
  }

  protected void handle(final Potion potion) {
    // empty
  }

  protected void handle(final SwordPart swordPart) {
    // empty
  }

  protected void handle(final Chest chest) {
    // empty
  }

  protected void handle(final Weapon weapon) {
    // empty
  }

  protected void handle(final Armor armor) {
    // empty
  }

  protected void handle(final Material material) {
    // empty
  }

  protected void handle(final Jewel jewel) {
    // empty
  }

  protected void handle(final Recipe recipe) {
    // empty
  }

  @Override
  public Void visit(final Armor armor) {
    this.handle(armor);
    return null;
  }

  @Override
  public Void visit(final Player player) {
    this.handle(player);
    return null;
  }

  @Override
  public Void visit(final Decoction decoction) {
    this.handle(decoction);
    return null;
  }

  @Override
  public Void visit(final Monster monster) {
    this.handle(monster);
    return null;
  }

  @Override
  public Void visit(final Potion potion) {
    this.handle(potion);
    return null;
  }

  @Override
  public Void visit(final SwordPart swordPart) {
    this.handle(swordPart);
    return null;
  }

  @Override
  public Void visit(final Chest chest) {
    this.handle(chest);
    return null;
  }

  @Override
  public Void visit(final Weapon weapon) {
    this.handle(weapon);
    return null;
  }

  @Override
  public Void visit(final Material material) {
    this.handle(material);
    return null;
  }

  @Override
  public Void visit(final Jewel jewel) {
    this.handle(jewel);
    return null;
  }

  @Override
  public Void visit(final Recipe recipe) {
    this.handle(recipe);
    return null;
  }

  @Override
  public Void visit(final Trunk trunk) {
    return null;
  }

  @Override
  public Void visit(final Workbench workbench) {
    return null;
  }

  @Override
  public Void visit(final Cauldron cauldron) {
    return null;
  }
}
