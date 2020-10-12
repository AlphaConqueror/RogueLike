package sopra.controller.visitor;

import java.util.Queue;
import sopra.comm.Observer;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.World;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Jewel;
import sopra.model.entities.items.Material;
import sopra.model.entities.items.Potion;
import sopra.model.entities.items.Recipe;
import sopra.model.entities.items.SwordPart;
import sopra.model.entities.items.Weapon;


public class PickUpVisitor extends DefaultEntityVisitor {

  private final Queue<Observer> observers;
  private final World world;

  public PickUpVisitor(final World world, final Queue<Observer> observers) {
    this.world = world;
    this.observers = observers;
  }

  @Override
  protected void handle(final Decoction decoction) {
    this.pickUp(decoction);
  }

  @Override
  protected void handle(final Potion potion) {
    this.pickUp(potion);
  }

  @Override
  protected void handle(final SwordPart swordPart) {
    this.pickUp(swordPart);
  }

  @Override
  protected void handle(final Weapon weapon) {
    this.pickUp(weapon);
  }

  @Override
  protected void handle(final Armor armor) {
    this.pickUp(armor);
  }

  @Override
  protected void handle(final Material material) {
    this.pickUp(material);
  }

  @Override
  protected void handle(final Jewel jewel) {
    this.pickUp(jewel);
  }

  @Override
  protected void handle(final Recipe recipe) {
    this.pickUp(recipe);
  }

  /**
   * Removes the item to be picked up from its position and adds it to the players inventory.
   *
   * @param item the item
   */
  private void pickUp(final Item item) {
    if (this.world.getPlayer().addItem(item)) {
      final Coordinate position = item.getPosition().orElseThrow(ServerError::new);
      this.world.removeEntity(position);
      this.observers.forEach(
          observer ->
              observer.notifyUpdateWorld(
                  this.world.getTile(position).orElseThrow(IllegalThreadStateException::new)));
      item.setPosition(null);
    } else {
      this.observers.forEach(observer -> observer.notifyCommandFailed("Inventory full."));
    }
  }
}
