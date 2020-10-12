package sopra.systemtest;

import java.util.Objects;
import sopra.systemtest.api.SystemTestManager;
import sopra.systemtest.controller.command.action.craft.CraftMaterialFirstSystemTest;
import sopra.systemtest.controller.command.action.craft.CraftNoRecipeSystemTest;
import sopra.systemtest.controller.command.action.craft.CraftNoTableSystemTest;
import sopra.systemtest.controller.command.action.craft.CraftNonCraftableRecipeSystemTest;
import sopra.systemtest.controller.command.action.craft.SimpleCraftSystemTest;
import sopra.systemtest.controller.command.action.movetoinventory.MoveToInventoryPlayerFullSystemTest;
import sopra.systemtest.controller.command.action.movetoinventory.SimpleMoveToInventorySystemTest;
import sopra.systemtest.controller.command.action.use.UseRecipeSystemTest;
import sopra.systemtest.controller.command.craftingmenu.CraftMenuMaterialFirstSystemTest;
import sopra.systemtest.controller.command.craftingmenu.CraftMenuNoRecipeSystemTest;
import sopra.systemtest.controller.command.craftingmenu.CraftMenuNoTableSystemTest;
import sopra.systemtest.controller.command.craftingmenu.CraftMenuNonCraftableRecipeSystemTest;
import sopra.systemtest.controller.command.craftingmenu.SimpleCraftMenuSystemTest;
import sopra.systemtest.controller.command.trunk.EmptyTrunkSystemTest;
import sopra.systemtest.controller.command.trunk.SimpleTrunkSystemTest;
import sopra.systemtest.controller.command.trunk.Trunk2StackableItemsSystemTest;
import sopra.systemtest.model.entities.equiment.DefaultSwordStrengthSystemTest;
import sopra.systemtest.model.entities.equiment.DefaultSwordVitalitySystemTest;
import sopra.systemtest.model.entities.equiment.UseSwordStrengthSystemTest;
import sopra.systemtest.model.entities.equiment.UseSwordVitalitySystemTest;
import sopra.systemtest.model.entities.inventory.AssembleSopraSwordTest;
import sopra.systemtest.model.entities.inventory.InventoryFullAddStackable;
import sopra.systemtest.model.entities.inventory.InventoryFullIncompatibleStacks;
import sopra.systemtest.model.entities.inventory.InventoryFullTest;
import sopra.systemtest.model.entities.item.BasicPickupAndView;
import sopra.systemtest.model.entities.item.DecoctionDurationTest;
import sopra.systemtest.model.entities.item.DropEmptySlot;
import sopra.systemtest.model.entities.item.DropSimple;
import sopra.systemtest.model.entities.item.DropSword;
import sopra.systemtest.model.entities.item.DropWrongIndex;
import sopra.systemtest.model.entities.item.NothingToPickUp;
import sopra.systemtest.model.entities.item.PickupButAttacked;
import sopra.systemtest.model.entities.item.ScrapDecreaseStack;
import sopra.systemtest.model.entities.item.ScrapEmptySlot;
import sopra.systemtest.model.entities.item.ScrapSimple;
import sopra.systemtest.model.entities.item.ScrapSwordComponent;
import sopra.systemtest.model.entities.item.UseDecoction;
import sopra.systemtest.model.entities.item.UseEmptySlot;
import sopra.systemtest.model.entities.item.UsePotion;
import sopra.systemtest.model.entities.item.UseSword;
import sopra.systemtest.model.entities.item.UseSwordPartsUnsucessful;
import sopra.systemtest.model.entities.item.UseVitalityDecoction;
import sopra.systemtest.model.entities.loot.BugLootBeetleshellSystemTest;
import sopra.systemtest.model.entities.loot.BugLootPotionSystemTest;
import sopra.systemtest.model.entities.monstermove.ForceMonsterE;
import sopra.systemtest.model.entities.monstermove.ForceMonsterNE;
import sopra.systemtest.model.entities.monstermove.ForceMonsterNW;
import sopra.systemtest.model.entities.monstermove.ForceMonsterSE;
import sopra.systemtest.model.entities.monstermove.ForceMonsterSW;
import sopra.systemtest.model.entities.monstermove.ForceMonsterW;
import sopra.systemtest.model.entities.monstermove.MonsterEnterSightRadius;
import sopra.systemtest.model.entities.monstermove.MonsterNoMoveExit;
import sopra.systemtest.model.entities.move.WalkEastTest;
import sopra.systemtest.model.entities.move.WalkIntoOccupied;
import sopra.systemtest.model.entities.move.WalkIntoWall;
import sopra.systemtest.model.entities.move.WalkNorthEastTest;
import sopra.systemtest.model.entities.move.WalkNorthWestTest;
import sopra.systemtest.model.entities.move.WalkSouthEastTest;
import sopra.systemtest.model.entities.move.WalkSouthWestTest;
import sopra.systemtest.model.entities.move.WalkWestTest;
import sopra.systemtest.model.entities.upgrade.UpgradeWithTwoSkillpointsNotUnderAttack;
import sopra.systemtest.model.entities.upgrade.UpgradeWithTwoSkillpointsNotUnderAttacktoLuck9;
import sopra.systemtest.model.entities.upgrade.UpgradeWithoutSkillpointsNotUnderAttack;
import sopra.systemtest.other.AttackProfessorWrongSword;
import sopra.systemtest.other.DontKillProfessorWrongSword;
import sopra.systemtest.other.EnterLvLUPAndAttack;
import sopra.systemtest.other.ExampleTest;
import sopra.systemtest.other.KillProfessor;
import sopra.systemtest.other.KillTutor;
import sopra.systemtest.other.LockedChestTest;
import sopra.systemtest.other.MultipleLvlUp;
import sopra.systemtest.other.PlayerDeadTest;
import sopra.systemtest.other.PlayerKilledWithoutSOPRA;
import sopra.systemtest.other.PlayerkilledByProfessor;
import sopra.systemtest.other.ProfessorAttackedTest;
import sopra.systemtest.other.ProfessorAttacksTest;
import sopra.systemtest.other.RangedAttackTest;

final class SystemTestsRegistration {

  public static void registerSystemTests(final SystemTestManager manager) {
    assert Objects.nonNull(manager);

    registerCraft(manager);
    registerMoveToInventory(manager);
    registerUse(manager);
    registerCraftingMenu(manager);
    registerTrunk(manager);
    registerEquipment(manager);
    registerInventory(manager);
    registerItem(manager);
    registerLoot(manager);
    registerMonsterMove(manager);
    registerMove(manager);
    registerUpgrade(manager);
    registerOther(manager);
  }

  private static void registerCraft(final SystemTestManager manager) {
    manager.registerTest(new SimpleCraftSystemTest());
    manager.registerTest(new CraftNoRecipeSystemTest());
    manager.registerTest(new CraftNonCraftableRecipeSystemTest());
    manager.registerTest(new CraftNoTableSystemTest());
    manager.registerTest(new CraftMaterialFirstSystemTest());
  }

  private static void registerMoveToInventory(final SystemTestManager manager) {
    manager.registerTest(new SimpleMoveToInventorySystemTest());
    manager.registerTest(new MoveToInventoryPlayerFullSystemTest());
  }

  private static void registerUse(final SystemTestManager manager) {
    manager.registerTest(new UseRecipeSystemTest());
  }

  private static void registerCraftingMenu(final SystemTestManager manager) {
    manager.registerTest(new CraftMenuMaterialFirstSystemTest());
    manager.registerTest(new SimpleCraftMenuSystemTest());
    manager.registerTest(new CraftMenuNonCraftableRecipeSystemTest());
    manager.registerTest(new CraftMenuNoTableSystemTest());
    manager.registerTest(new CraftMenuNoRecipeSystemTest());
  }

  private static void registerTrunk(final SystemTestManager manager) {
    manager.registerTest(new EmptyTrunkSystemTest());
    manager.registerTest(new SimpleTrunkSystemTest());
    manager.registerTest(new Trunk2StackableItemsSystemTest());
  }

  private static void registerEquipment(final SystemTestManager manager) {
    manager.registerTest(new DefaultSwordStrengthSystemTest());
    manager.registerTest(new DefaultSwordVitalitySystemTest());
    manager.registerTest(new UseSwordStrengthSystemTest());
    manager.registerTest(new UseSwordVitalitySystemTest());
  }

  private static void registerInventory(final SystemTestManager manager) {
    manager.registerTest(new AssembleSopraSwordTest());
    manager.registerTest(new InventoryFullAddStackable());
    manager.registerTest(new InventoryFullIncompatibleStacks());
    manager.registerTest(new InventoryFullTest());
  }

  private static void registerItem(final SystemTestManager manager) {
    manager.registerTest(new BasicPickupAndView());
    manager.registerTest(new DecoctionDurationTest());
    manager.registerTest(new DropEmptySlot());
    manager.registerTest(new DropSimple());
    manager.registerTest(new DropSword());
    manager.registerTest(new DropWrongIndex());
    manager.registerTest(new NothingToPickUp());
    manager.registerTest(new PickupButAttacked());
    manager.registerTest(new ScrapDecreaseStack());
    manager.registerTest(new ScrapEmptySlot());
    manager.registerTest(new ScrapSimple());
    manager.registerTest(new ScrapSwordComponent());
    manager.registerTest(new UseDecoction());
    manager.registerTest(new UseEmptySlot());
    manager.registerTest(new UsePotion());
    manager.registerTest(new UseSword());
    manager.registerTest(new UseSwordPartsUnsucessful());
    manager.registerTest(new UseVitalityDecoction());
  }

  private static void registerMonsterMove(final SystemTestManager manager) {
    manager.registerTest(new ForceMonsterE());
    manager.registerTest(new ForceMonsterNE());
    manager.registerTest(new ForceMonsterNW());
    manager.registerTest(new ForceMonsterSE());
    manager.registerTest(new ForceMonsterSW());
    manager.registerTest(new ForceMonsterW());
    manager.registerTest(new MonsterEnterSightRadius());
    manager.registerTest(new MonsterNoMoveExit());
    registerLoot(manager);
  }

  private static void registerMove(final SystemTestManager manager) {
    manager.registerTest(new WalkEastTest());
    manager.registerTest(new WalkIntoOccupied());
    manager.registerTest(new WalkIntoWall());
    manager.registerTest(new WalkNorthEastTest());
    manager.registerTest(new WalkNorthWestTest());
    manager.registerTest(new WalkSouthEastTest());
    manager.registerTest(new WalkSouthWestTest());
    manager.registerTest(new WalkWestTest());
  }

  private static void registerUpgrade(final SystemTestManager manager) {
    manager.registerTest(new UpgradeWithoutSkillpointsNotUnderAttack());
    manager.registerTest(new UpgradeWithTwoSkillpointsNotUnderAttack());
    manager.registerTest(new UpgradeWithTwoSkillpointsNotUnderAttacktoLuck9());
  }

  private static void registerOther(final SystemTestManager manager) {
    manager.registerTest(new AttackProfessorWrongSword());
    manager.registerTest(new DontKillProfessorWrongSword());
    manager.registerTest(new EnterLvLUPAndAttack());
    manager.registerTest(new ExampleTest());
    manager.registerTest(new KillProfessor());
    manager.registerTest(new KillTutor());
    manager.registerTest(new LockedChestTest());
    manager.registerTest(new MultipleLvlUp());
    manager.registerTest(new PlayerDeadTest());
    manager.registerTest(new PlayerkilledByProfessor());
    manager.registerTest(new PlayerKilledWithoutSOPRA());
    manager.registerTest(new ProfessorAttackedTest());
    manager.registerTest(new ProfessorAttacksTest());
    manager.registerTest(new RangedAttackTest());
  }

  private static void registerLoot(final SystemTestManager manager) {
    manager.registerTest(new BugLootBeetleshellSystemTest());
    manager.registerTest(new BugLootPotionSystemTest());
  }
}
