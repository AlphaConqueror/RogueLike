package sopra.controller.visitor;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.comm.Observer;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.Coordinate;
import sopra.model.Tile;
import sopra.model.World;
import sopra.model.entities.Character;
import sopra.model.entities.Chest;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.model.entities.Monster;
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

public class AttackVisitor extends DefaultEntityVisitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AttackVisitor.class);
  private final Character character;
  private final Queue<Observer> observers;
  private final World world;

  public AttackVisitor(final World world, final Queue<Observer> observers,
                       final Character character) {
    this.world = world;
    this.observers = observers;
    this.character = character;
  }

  /**
   * Responsible for removing entities from the grid after being attacked.
   *
   * @param entity the attacked entity
   */
  private void attack(final Entity entity) {
    final Coordinate position = entity.getPosition().orElseThrow(ServerError::new);

    entity.disable();
    this.world.removeEntity(position);
    this.observers.forEach(
        observer -> observer
        .notifyUpdateWorld(this.world.getTile(position).orElseThrow(ServerError::new)));
    if (entity.isLegendary()) {
      this.world.loose();
    }
  }

  @Override
  protected void handle(final Decoction decoction) {
    this.attack(decoction);
  }

  @Override
  protected void handle(final Monster monster) {
    LOGGER.debug("{} vs {}", this.character, monster);
    final int agility =
        this.character.getSkill(Character.CharacterSkill.AGILITY) + monster
        .getSkill(Character.CharacterSkill.AGILITY);
    final int hit = this.world.roll(agility);
    LOGGER.debug("hit D{} -> {}", agility, hit);
    if (hit <= this.character.getSkill(Character.CharacterSkill.AGILITY)) {
      final int dice = this.character.getSkill(Character.CharacterSkill.STRENGTH);
      final int sides = this.character.getWeapon().getDamage();
      final int damage = this.world.roll(dice, sides);
      LOGGER.debug("damage {} x D{} -> {}", dice, sides, damage);
      switch (monster.getType()) {
        case ASSISTANT, BUG, OVERFLOW, TUTOR -> monster.dealDamage(damage);
        case PROFESSOR -> monster.dealDamage(this.character.getWeapon().isLegendary()
          ? damage : Config.PROF_BASE_DAMAGE);
        }
      if (monster.isDisabled()) {
        final Coordinate position = monster.getPosition().orElseThrow(ServerError::new);
        this.world.removeEntity(position);
        this.observers.forEach(
            observer -> observer.notifyUpdateWorld(this.world.getTile(position)
                    .orElseThrow(ServerError::new)));
        if (monster.getType() == EntityType.ASSISTANT) {
          this.world.getRoom(position).orElseThrow(ServerError::new).enableChest();
        }
        if (monster.getType() == EntityType.PROFESSOR) {
          this.world.win();
        }
        if (this.world.isEnabled()) {
          this.world.getPlayer().addExperience(
              monster.getLevel() * monster.getLevel() * monster.getType().getFactor());
          this.observers.forEach(observer -> observer.notifyUpdatePlayer(this.world.getPlayer()));
          monster.accept(new LootVisitor(this.world, this.observers));
        }
      }
    } else {
      LOGGER.debug("miss");
    }
  }

  @Override
  protected void handle(final Player player) {
    LOGGER.debug("{} vs {}", this.character, player);
    final int agility =
        this.character.getSkill(Character.CharacterSkill.AGILITY)
              + player.getSkill(Character.CharacterSkill.AGILITY);
    final int hit = this.world.roll(agility);
    LOGGER.debug("hit D{} -> {}", agility, hit);
    if (hit <= this.character.getSkill(Character.CharacterSkill.AGILITY)) {
      final int dice = this.character.getSkill(Character.CharacterSkill.STRENGTH);
      final int sides = this.character.getWeapon().getDamage();
      final int damage = this.world.roll(dice, sides);
      LOGGER.debug("damage {} x D{} -> {}", dice, sides, damage);
      if (player.dealDamage(damage)) {
        this.observers.forEach(observer -> observer.notifyUpdatePlayer(player));
      }
      if (player.isDisabled()) {
        this.world.loose();
      }
    } else {
      LOGGER.debug("miss");
    }
  }

  @Override
  protected void handle(final Potion potion) {
    this.attack(potion);
  }

  @Override
  protected void handle(final SwordPart swordPart) {
    this.attack(swordPart);
  }

  @Override
  protected void handle(final Chest chest) {
    if (chest.isEnabled()) {
      final Coordinate position = chest.getPosition().orElseThrow(ServerError::new);
      this.world.removeEntity(position);
      final Tile tile = this.world.getTile(position).orElseThrow(ServerError::new);
      final SwordPart part = Item.ItemFactory.createSwordPart();
      part.setPosition(position);
      tile.setEntity(part);
      this.observers.forEach(observer -> observer.notifyUpdateWorld(tile));
      chest.accept(new LootVisitor(this.world, this.observers));
    } else {
      this.observers.forEach(observer -> observer.notifyCommandFailed("Tutor not dead!"));
    }
  }

  @Override
  protected void handle(final Weapon weapon) {
    this.attack(weapon);
  }

  @Override
  protected void handle(final Armor armor) {
    this.attack(armor);
  }

  @Override
  protected void handle(final Material material) {
    this.attack(material);
  }

  @Override
  protected void handle(final Jewel jewel) {
    this.attack(jewel);
  }

  @Override
  protected void handle(final Recipe recipe) {
    this.attack(recipe);
  }

  private static class LootVisitor extends DefaultEntityVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LootVisitor.class);
    private static final Jewel[] JEWELS
            = {Jewel.RUBY, Jewel.AMETHYST, Jewel.EMERALD, Jewel.DIAMOND};
    private static final Material[] MATERIALS
            = {Material.STEEL, Material.LEATHER, Material.WOOD, Material.HERBS};

    private final Queue<Observer> observers;
    private final World world;

    LootVisitor(final World world, final Queue<Observer> observers) {
      this.world = world;
      this.observers = observers;
    }

    @Override
    protected void handle(final Monster monster) {
      final Queue<Item> items = new ArrayDeque<>();
      final int luck = Math.max(1, this.world.getPlayer()
              .getSkill(Character.CharacterSkill.LUCK) - 1);
      final int count = this.world.roll(luck);
      LOGGER.debug("monster D{} -> {}", luck, count);
      final int sides = this.world.getPlayer().getLevel();
      for (int i = 0; i < count; i++) {
        final int choice = this.world.roll(Config.LOOT_ROLL_SIDES) + luck;
        LOGGER.debug("choice D{} + {} -> {}", Config.LOOT_ROLL_SIDES, luck, choice);

        if (choice >= 10) {
          if (choice % 2 == 0) {
            LOGGER.debug("recipe level {}", monster.getLevel());
            items.add(Recipe.RecipeFactory.createRecipe(monster.getLevel(), this.world));
          } else {
            final Jewel jewel = world.roll(JEWELS);

            LOGGER.debug("jewel D{} -> {}", JEWELS.length, jewel.getType().toJSON());
            items.add(jewel.spawn());
          }
        } else {
          if (choice % 2 == 0) {
            final int level = this.world.roll(sides);

            LOGGER.debug("potion D{} -> {}", sides, level);
            items.add(Item.ItemFactory.createPotion(level));
          } else {
            final Material material;

            if (monster.getType() == EntityType.BUG) {
              material = Material.BEETLESHELL;
            } else {
              material = world.roll(MATERIALS);

              LOGGER.debug("material D{} -> {}",
                      MATERIALS.length, material.getType().toJSON());
            }

            items.add(material.spawn());
          }
        }
      }
      this.loot(monster.getPosition().orElseThrow(ServerError::new), items);
    }

    @Override
    protected void handle(final Chest chest) {
      final Queue<Item> items = new ArrayDeque<>();
      final int luck = this.world.getPlayer().getSkill(Character.CharacterSkill.LUCK);
      final int count = this.world.roll(luck);
      LOGGER.debug("chest D{} -> {}", luck, count);

      for (int i = 0; i < count; i++) {
        final int choice = this.world.roll(Config.LOOT_ROLL_SIDES) + luck;
        LOGGER.debug("choice D{} + {} -> {}", Config.LOOT_ROLL_SIDES, luck, choice);

        if (choice >= 10) {
          if (choice % 2 == 0) {
            final int level = this.world.getPlayerRoom().getLevel();

            LOGGER.debug("recipe level {}", level);
            items.add(Recipe.RecipeFactory.createRecipe(level, this.world));
          } else {
            final Jewel jewel = world.roll(JEWELS);

            LOGGER.debug("jewel D{} -> {}", JEWELS.length, jewel.getType().toJSON());
            items.add(jewel.spawn());
          }
        } else {
          final Material material
                  = world.roll(MATERIALS);

          LOGGER.debug("material D{} -> {}",
                  MATERIALS.length, material.getType().toJSON());
          items.add(material.spawn());
        }
      }
      this.loot(chest.getPosition().orElseThrow(ServerError::new), items);
    }

    /**
     * Responsible for positioning loot on tiles.
     *
     * @param position position of the destroyed/killed entity
     * @param items    a list of loot
     */
    private void loot(final Coordinate position, final Queue<Item> items) {
      final Set<Coordinate> frontier = new HashSet<>(
          this.world.getRoom(position).orElseThrow(ServerError::new).getCoordinates());
      int radius = 1;
      frontier.remove(position);
      while (!items.isEmpty() && !frontier.isEmpty()) {
        final Queue<Coordinate> coordinates = position.cubeRing(radius);
        for (final Coordinate coordinate : coordinates) {
          if (frontier.remove(coordinate)
              && this.world.isAccessible(EntityType.PLAYER, coordinate)) {
            final Entity item = items.poll();
            if (Objects.nonNull(item)) {
              item.setPosition(coordinate);
              this.world.addEntity(item);
              if (this.world.getPlayer().sees(coordinate)) {
                this.observers.forEach(observer -> observer.notifyUpdateWorld(
                    this.world.getTile(coordinate).orElseThrow(ServerError::new)));
              }
            }
          }
        }
        radius++;
      }
    }
  }
}
