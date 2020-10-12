package sopra.controller.visitor;

import sopra.controller.Config;
import sopra.model.entities.EntityType;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Potion;
import sopra.model.entities.items.Weapon;

public class ModifyVisitor extends DefaultEntityVisitor {

  private final EntityType material;
  private final int amount;
  private final String name;
  private int damage;
  private int range;
  private int resistance;
  private int value;
  private int duration;

  public ModifyVisitor(final EntityType material, final int amount, final String name) {
    this.material = material;
    this.amount = amount;
    this.name = name;
  }

  public EntityType getMaterial() {
    return material;
  }

  public int getAmount() {
    return amount;
  }

  public ModifyVisitor modifyDamage(final int damage) {
    this.damage = damage;

    return this;
  }

  public ModifyVisitor modifyRange(final int range) {
    this.range = range;

    return this;
  }

  public ModifyVisitor modifyResistance(final int resistance) {
    this.resistance = resistance;

    return this;
  }

  public ModifyVisitor modifyValue(final int value) {
    this.value = value;

    return this;
  }

  public ModifyVisitor modifyDuration(final int duration) {
    this.duration = duration;

    return this;
  }

  @Override
  protected void handle(final Decoction decoction) {
    decoction.setDuration(
            Math.max(Config.MINIMAL_ITEM_VALUE, decoction.getDuration() + this.duration));
  }

  @Override
  protected void handle(final Potion potion) {
    potion.setValue(Math.max(Config.MINIMAL_ITEM_VALUE, potion.getValue() + this.value));
  }

  @Override
  protected void handle(final Weapon weapon) {
    weapon.setDamage(Math.max(Config.MINIMAL_ITEM_VALUE, weapon.getDamage() + this.damage));
    weapon.setRange(Math.max(Config.MINIMAL_ITEM_VALUE, weapon.getRange() + this.range));
  }

  @Override
  protected void handle(final Armor armor) {
    armor.setResistance(
            Math.max(Config.MINIMAL_ITEM_VALUE, armor.getResistance() + this.resistance));
  }

  @Override
  public String toString() {
    return this.name;
  }
}
