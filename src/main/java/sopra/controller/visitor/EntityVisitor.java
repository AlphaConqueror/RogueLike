package sopra.controller.visitor;

import sopra.controller.command.Command;
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
 * {@link EntityVisitor}s are used to help the {@link Command}s in executing their task.
 *
 * <p>They encapsulate {@link Command} related code for different entities.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public interface EntityVisitor<T> {

  T visit(Armor armor);

  T visit(Player player);

  T visit(Decoction decoction);

  T visit(Monster monster);

  T visit(Potion potion);

  T visit(SwordPart swordPart);

  T visit(Chest chest);

  T visit(Weapon weapon);

  T visit(Material material);

  T visit(Jewel jewel);

  T visit(Recipe recipe);

  T visit(Trunk trunk);

  T visit(Workbench workbench);

  T visit(Cauldron cauldron);
}
