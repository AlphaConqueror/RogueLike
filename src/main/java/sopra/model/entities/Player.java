package sopra.model.entities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.controller.visitor.EntityVisitor;
import sopra.model.Coordinate;
import sopra.model.entities.items.Armor;
import sopra.model.entities.items.Decoction;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Recipe;
import sopra.model.entities.items.Weapon;
import sopra.utils.JSONSerializable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Player extends Character implements JSONSerializable<JSONObject> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);
  private final Queue<Decoction> decoctions;
  private final Inventory inventory;
  private int experience;
  private int skillPoints;

  private Player(final String name, final int level, final int experience, final int skillPoints,
      final int currentHealth, final EnumMap<CharacterSkill, Integer> skills,
      final Weapon weapon, final Optional<Armor> armor) {
    super(EntityType.PLAYER, name, level, currentHealth, weapon, armor, skills,
        CharacterState.REGISTER);
    this.experience = experience;
    this.skillPoints = skillPoints;
    this.inventory = new Inventory();
    this.decoctions = new ArrayDeque<>();
  }

  public static Player fromJson(final JSONObject root) {
    final Builder builder = new Builder(root.getString("name"));
    if (root.has("experience")) {
      builder.setExperience(root.getInt("experience"));
    }
    if (root.has("skillPoints")) {
      builder.setSkillPoints(root.getInt("skillPoints"));
    }
    if (root.has("currentHealth")) {
      builder.setCurrentHealth(root.getInt("currentHealth"));
    }
    if (root.has("strength")) {
      builder.setSkill(CharacterSkill.STRENGTH, root.getInt("strength"));
    }
    if (root.has("vitality")) {
      builder.setSkill(CharacterSkill.VITALITY, root.getInt("vitality"));
    }
    if (root.has("agility")) {
      builder.setSkill(CharacterSkill.AGILITY, root.getInt("agility"));
    }
    if (root.has("luck")) {
      builder.setSkill(CharacterSkill.LUCK, root.getInt("luck"));
    }
    if (root.has("weapon")) {
      builder.setWeapon(Weapon.fromJson(root.getJSONObject("weapon")));
    }
    if (root.has("armor")) {
      builder.setArmor(Armor.fromJson(root.getJSONObject("armor")));
    }
    return builder.build();
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public void addDecoction(final Decoction decoction) {
    this.decoctions.add(decoction);
  }

  /**
   * Adds experience points to the player.
   *
   * @param experience the experience to be added
   */
  public void addExperience(final int experience) {
    this.experience += experience;
    LOGGER.debug("+{} XP -> {}", experience, this.experience);
    while (this.getLevel() < Config.MAX_PLAYER_LEVEL && this.experience >= Config.EXPERIENCE_LEVEL
        .get(this.getLevel())) {
      this.levelUp();
      this.skillPoints += 2;
    }
  }

  public boolean addItem(final Item item) {
    return this.inventory.add(item);
  }

  public void addItem(final int index, final Item item) {
    this.inventory.set(index, item);
  }

  public Queue<Decoction> getDecoctions() {
    return this.decoctions;
  }

  public Optional<Item> getItem(final int index) {
    return this.inventory.get(index);
  }

  @Override
  public int getSkill(final CharacterSkill skill) {
    return (super.getSkill(skill) + (int) this.decoctions.stream()
        .filter(decoction -> decoction.getSkill() == skill).count());
  }

  @Override
  public void setState(final CharacterState state) {
    LOGGER.debug("{}", state);
    super.setState(state);
  }

  @Override
  public JSONObject toJSON() {
    final JSONObject root = super.toJSON();
    root.put("luck", this.getSkill(CharacterSkill.LUCK));
    root.put("experience", this.experience);
    root.put("skillPoints", this.skillPoints);
    return root;
  }

  @Override
  public boolean upgrade(final CharacterSkill skill) {
    if (this.skillPoints > 0 && super.upgrade(skill)) {
      LOGGER.debug("{} SP -> {}", this.skillPoints, skill);
      this.skillPoints--;
      return true;
    }
    return false;
  }

  public JSONObject inventoryJSON() {
    return this.inventory.toJSON();
  }

  public List<Recipe> getCraftableRecipes(final String recipeTableJson) {
    final List<Recipe> recipes = new ArrayList<>();

    for (int i = 0; i < Config.INVENTORY_SIZE; i++) {
      if (this.getItem(i).isEmpty() || !(this.getItem(i).get() instanceof Recipe)) {
        continue;
      }

      final Recipe recipe = (Recipe) this.getItem(i).get();

      if (!recipe.canCraft(this)
              || !recipeTableJson.equals(recipe.getRecipeTable().toJSON())) {
        continue;
      }

      recipes.add(recipe);
    }

    return recipes;
  }

  public void removeItem(final int index) {
    this.removeItem(index, 1);
  }

  public void removeItem(final int index, final int amount) {
    this.inventory.drop(index, amount);
  }

  public void clearIndex(final int index) {
    this.inventory.set(index, Optional.empty());
  }

  /**
   * Computes, whether the player can see a certain coordinate.
   *
   * @param coordinate the coordinate
   *
   * @return true if the player sees the coordinate
   */
  public boolean sees(final Coordinate coordinate) {
    return this.getPosition().orElseThrow(ServerError::new).distance(coordinate)
        <= Config.SIGHT_RADIUS;
  }

  /**
   * Tick and remove all active decoctions.
   *
   * @return true if a decoction was removed
   */
  public boolean tickDecoctions() {
    this.decoctions.forEach(Decoction::tick);
    return this.decoctions.removeIf(Entity::isDisabled);
  }

  private static class Builder {

    private final Optional<Integer> maxHealth;
    private final String name;
    Optional<Armor> armor;
    int skillPoints;
    final EnumMap<CharacterSkill, Integer> skills;
    Weapon weapon;
    private Optional<Integer> currentHealth;
    private int experience;
    private int level;

    private Builder(final String name) {
      this.name = name;
      this.level = 1;
      this.experience = 0;
      this.skillPoints = 0;
      this.skills = new EnumMap<>(CharacterSkill.class);
      this.skills.put(CharacterSkill.STRENGTH, 1);
      this.skills.put(CharacterSkill.VITALITY, 1);
      this.skills.put(CharacterSkill.AGILITY, 1);
      this.skills.put(CharacterSkill.LUCK, 1);
      this.maxHealth = Optional.empty();
      this.currentHealth = Optional.empty();
      this.weapon = Item.ItemFactory.createSword(1);
      this.armor = Optional.empty();
    }

    Player build() {
      final int maxHealth = this.maxHealth.orElseGet(
          () -> Character.calculateHP(this.level, this.skills.get(CharacterSkill.VITALITY)));
      return new Player(this.name, this.level, this.experience, this.skillPoints,
          Math.min(this.currentHealth.orElse(maxHealth), maxHealth), this.skills, this.weapon,
          this.armor);
    }

    public void setArmor(final Armor armor) {
      this.armor = Optional.ofNullable(armor);
    }

    void setCurrentHealth(final int currentHealth) {
      this.currentHealth = Optional.of(currentHealth);
    }

    public void setExperience(final int experience) {
      this.experience = experience;
      while (this.level < 10 && experience >= Config.EXPERIENCE_LEVEL.get(this.level)) {
        this.level++;
      }
    }

    void setSkill(final CharacterSkill skill, final int points) {
      this.skills.put(skill, points);
    }

    void setSkillPoints(final int skillPoints) {
      this.skillPoints = skillPoints;
    }

    public void setWeapon(final Weapon weapon) {
      this.weapon = weapon;
    }
  }

}
