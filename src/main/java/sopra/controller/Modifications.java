package sopra.controller;

import java.util.List;
import sopra.controller.visitor.ModifyVisitor;
import sopra.model.entities.EntityType;

public class Modifications {

  public static final List<ModifyVisitor> SWORD = List.of(
          new ModifyVisitor(EntityType.STEEL, 1, "sharp").modifyDamage(1),
          new ModifyVisitor(EntityType.STEEL, -1, "blunt").modifyDamage(-1),
          new ModifyVisitor(EntityType.WOOD, 1, "long").modifyRange(1));

  public static final List<ModifyVisitor> BOW = List.of(
          new ModifyVisitor(EntityType.WOOD, 1, "long").modifyRange(1),
          new ModifyVisitor(EntityType.WOOD, -1, "short").modifyRange(-1),
          new ModifyVisitor(EntityType.WOOD, 1, "reinforced").modifyDamage(1));

  public static final List<ModifyVisitor> ARMOR = List.of(
          new ModifyVisitor(EntityType.STEEL, 2, "ironclad").modifyResistance(4),
          new ModifyVisitor(EntityType.LEATHER, 1, "improved").modifyResistance(2),
          new ModifyVisitor(EntityType.LEATHER, -1, "light").modifyResistance(-2));

  public static final List<ModifyVisitor> POTION = List.of(
          new ModifyVisitor(EntityType.HERBS, 1, "great").modifyValue(3),
          new ModifyVisitor(EntityType.HERBS, -1, "small").modifyValue(-3));

  public static final List<ModifyVisitor> DECOCTION = List.of(
          new ModifyVisitor(EntityType.BEETLESHELL, 1, "strong").modifyDuration(5),
          new ModifyVisitor(EntityType.BEETLESHELL, -1, "weak").modifyDuration(-5));
}
