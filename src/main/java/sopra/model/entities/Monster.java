package sopra.model.entities;

import java.util.EnumMap;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.controller.Config;
import sopra.controller.Die;
import sopra.controller.ServerError;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.Coordinate;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Weapon;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Monster extends Character {

  private static final Logger LOGGER = LoggerFactory.getLogger(Monster.class);
  private Optional<Coordinate> spawn;

  private Monster(final EntityType type, final String name, final int level,
      final int currentHealth, final Weapon weapon,
      final Optional<Armor> armor, final EnumMap<CharacterSkill, Integer> skills) {
    super(type, name, level, currentHealth, weapon, armor, skills,
        CharacterState.DEFAULT);
    this.spawn = Optional.empty();
  }

  public static Monster fromJson(final JSONObject root, final Die die) {
    final EntityType type = switch (root.getString("type")) {
      case "bug" -> EntityType.BUG;
      case "overflow" -> EntityType.OVERFLOW;
      case "tutor" -> EntityType.TUTOR;
      case "assistant" -> EntityType.ASSISTANT;
      case "professor" -> EntityType.PROFESSOR;
      default -> throw new ServerError();
    };
    final Builder builder = new Builder(type, root.getString("name"), root.getInt("level"), die);
    if (root.has("currentHealth")) {
      builder.setCurrentHealth(root.getInt("currentHealth"));
    }
    if (root.has("strength") || root.has("vitality") || root.has("agility")) {
      builder.setSkills(root.getInt("strength"), root.getInt("vitality"), root.getInt("agility"));
    }
    return builder.build();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public void setPosition(final Coordinate position) {
    super.setPosition(position);
    if (this.spawn.isEmpty()) {
      this.spawn = Optional.ofNullable(position);
    }
  }

  public Monster respawn(final int level, final Die die) {
    final Monster monster = new Builder(this.getType(), this.name, level, die).build();
    monster.setPosition(this.spawn.orElseThrow(ServerError::new));
    return monster;
  }

  @Override
  public void setState(final CharacterState state) {
    LOGGER.debug("{}", state);
    super.setState(state);
  }

  private static class Builder {

    private final Die die;
    private final int level;
    private final String name;
    private final EntityType type;
    Optional<Armor> armor;
    final EnumMap<CharacterSkill, Integer> skills;
    private Optional<Integer> currentHealth;

    private Builder(final EntityType type, final String name, final int level, final Die die) {
      this.type = type;
      this.level = level;
      this.die = die;
      this.armor = Optional.empty();
      this.name = name;
      this.currentHealth = Optional.empty();
      this.skills = new EnumMap<>(CharacterSkill.class);
    }

    Monster build() {
      if (this.type == EntityType.PROFESSOR && this.level != Config.PROF_LEVEL) {
        throw new ServerError("Professor has not required level {}!", Config.PROF_LEVEL);
      }
      switch (this.type) {
        case ASSISTANT, PROFESSOR, OVERFLOW -> this.armor =
            Optional.of(Item.ItemFactory.createArmor(this.level));
      }
      if (this.skills.isEmpty()) {
        this.setSkills(0, 0, 0);
        for (int i = 0; i < switch (this.type) {
          case BUG, OVERFLOW -> this.level;
          case TUTOR -> 2 * this.level;
          case ASSISTANT -> 5 * this.level;
          case PROFESSOR -> 10 * this.level;
          default -> throw new ServerError();
        }; i++) {
          this.skills.computeIfPresent(this.die.roll(CharacterSkill.values(this.type)),
              (k, v) -> Math.min(9, v + 1));
        }
      }
      final int maxHealth =
          Character.calculateHP(this.level, this.skills.get(CharacterSkill.VITALITY));
      return new Monster(this.type, this.name, this.level,
          Math.min(this.currentHealth.orElse(maxHealth), maxHealth),
          Item.ItemFactory.createSword(this.level), this.armor, this.skills);
    }

    void setCurrentHealth(final int currentHealth) {
      this.currentHealth = Optional.of(currentHealth);
    }

    void setSkills(final int strength, final int vitality, final int agility) {
      this.skills.putIfAbsent(CharacterSkill.STRENGTH, strength);
      this.skills.putIfAbsent(CharacterSkill.VITALITY, vitality);
      this.skills.putIfAbsent(CharacterSkill.AGILITY, agility);
    }
  }
}
