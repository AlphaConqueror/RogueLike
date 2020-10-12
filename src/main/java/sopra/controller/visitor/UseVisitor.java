package sopra.controller.visitor;

import java.util.Queue;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.entities.Player;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Jewel;
import sopra.model.entities.items.Material;
import sopra.model.entities.items.Potion;
import sopra.model.entities.items.Recipe;
import sopra.model.entities.items.SwordPart;
import sopra.model.entities.items.Weapon;


public class UseVisitor extends DefaultEntityVisitor {

  private final int index;
  private final Queue<Observer> observers;
  private final Player player;

  public UseVisitor(final Player player, final int index, final Queue<Observer> observers) {
    this.player = player;
    this.index = index;
    this.observers = observers;
  }

  @Override
  protected void handle(final Decoction decoction) {
    this.player.removeItem(this.index);
    this.player.addDecoction(decoction.spawn());
    this.observers.forEach(observer -> observer.notifyUpdatePlayer(this.player));
  }

  @Override
  protected void handle(final Potion potion) {
    this.player.removeItem(this.index);
    if (this.player.heal(potion.getValue())) {
      this.observers.forEach(observer -> observer.notifyUpdatePlayer(this.player));
    }
  }

  @Override
  protected void handle(final SwordPart swordPart) {
    switch (this.player.getState()) {
      case DEFAULT, ATTACKED -> {
        if (swordPart.getStack() >= Config.NUMBER_SWORD_PARTS) {
          this.player.removeItem(this.index);
          final Weapon sword = Item.ItemFactory.createSOPRASword();
          sword.accept(this);
        }
      }
      case COMBAT -> this.observers
        .forEach(observer -> observer.notifyCommandFailed("Use not possible in combat."));
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  protected void handle(final Weapon weapon) {
    switch (this.player.getState()) {
      case DEFAULT, ATTACKED -> {
        this.player.removeItem(this.index);
        final Weapon current = this.player.getWeapon();
        this.player.addItem(this.index, current);
        this.player.setWeapon(weapon);
        this.observers.forEach(observer -> observer.notifyUpdatePlayer(this.player));
      }
      case COMBAT -> this.observers
        .forEach(observer -> observer.notifyCommandFailed("Use not possible in combat."));
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  protected void handle(final Armor armor) {
    switch (this.player.getState()) {
      case DEFAULT, ATTACKED -> {
        this.player.removeItem(this.index);
        this.player.getArmor().ifPresent(i -> this.player.addItem(this.index, i));
        this.player.setArmor(armor);
        this.observers.forEach(observer -> observer.notifyUpdatePlayer(this.player));
      }
      case COMBAT -> this.observers
        .forEach(observer -> observer.notifyCommandFailed("Use not possible in combat."));
      case REGISTER -> throw new ServerError();
    }
  }

  @Override
  protected void handle(final Material material) {
    sendNotUsable();
  }

  @Override
  protected void handle(final Jewel jewel) {
    sendNotUsable();
  }

  @Override
  protected void handle(final Recipe recipe) {
    sendNotUsable();
  }

  private void sendNotUsable() {
    this.observers.forEach(observer -> observer.notifyCommandFailed("Not usable."));
  }
}
