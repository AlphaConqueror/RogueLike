package sopra.comm;

import sopra.controller.command.Command;
import sopra.controller.command.CraftingMenu;
import sopra.controller.command.Inventory;
import sopra.controller.command.Leave;
import sopra.controller.command.Skills;
import sopra.controller.command.Trunk;
import sopra.controller.command.action.Attack;
import sopra.controller.command.action.Craft;
import sopra.controller.command.action.Drop;
import sopra.controller.command.action.Enter;
import sopra.controller.command.action.Move;
import sopra.controller.command.action.MoveToInventory;
import sopra.controller.command.action.PickUp;
import sopra.controller.command.action.Register;
import sopra.controller.command.action.Scrap;
import sopra.controller.command.action.Upgrade;
import sopra.controller.command.action.Use;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.1
 */
public class CommandFactoryImpl implements CommandFactory<Command> {

  @Override
  public Command createAttack(final Direction direction) {
    return new Attack(direction);
  }

  @Override
  public Command createCraft(final int index, final Direction direction) {
    return new Craft(index, direction);
  }

  @Override
  public Command createCraftingMenu(final Direction direction) {
    return new CraftingMenu(direction);
  }

  @Override
  public Command createDrop(final int index, final Direction direction) {
    return new Drop(index, direction);
  }

  @Override
  public Command createEnter(final Direction direction) {
    return new Enter(direction);
  }

  @Override
  public Command createInventory() {
    return new Inventory();
  }

  @Override
  public Command createLeave() {
    return new Leave();
  }

  @Override
  public Command createMove(final Direction direction) {
    return new Move(direction);
  }

  @Override
  public Command createMoveToInventory(final int index, final Direction direction) {
    return new MoveToInventory(index, direction);
  }

  @Override
  public Command createPickUp(final Direction direction) {
    return new PickUp(direction);
  }

  @Override
  public Command createRegister() {
    return new Register();
  }

  @Override
  public Command createScrap(final int index) {
    return new Scrap(index);
  }

  @Override
  public Command createSkills() {
    return new Skills();
  }

  @Override
  public Command createTrunk(final Direction direction) {
    return new Trunk(direction);
  }

  @Override
  public Command createUpgrade(final int index) {
    return new Upgrade(index);
  }

  @Override
  public Command createUse(final int index) {
    return new Use(index);
  }
}
