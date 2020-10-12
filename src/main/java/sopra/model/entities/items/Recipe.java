package sopra.model.entities.items;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.controller.Config;
import sopra.controller.Modifications;
import sopra.controller.ServerError;
import sopra.controller.visitor.EntityVisitor;
import sopra.controller.visitor.ModifyVisitor;
import sopra.model.World;
import sopra.model.entities.Character;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;
import sopra.utils.JSONSerializable;
import sopra.utils.Utils;

public class Recipe extends Item implements JSONSerializable<JSONObject> {

  private final RecipeType recipeType;
  private final Item item;
  private final EnumMap<EntityType, Integer> craftingMaterials
          = new EnumMap<>(EntityType.class);

  public Recipe(final RecipeType recipeType, final Item item) {
    super(EntityType.RECIPE);
    this.recipeType = recipeType;
    this.item = item;

    Material.MATERIALS.forEach(material -> craftingMaterials.put(material.getType(), 0));
    Jewel.JEWELS.forEach(jewel -> craftingMaterials.put(jewel.getType(), 0));
  }

  public static Recipe fromJson(final JSONObject root) {
    final RecipeType recipeType = switch (root.getString("recipeType")) {
      case "weapon" -> RecipeType.WEAPON;
      case "armor" -> RecipeType.ARMOR;
      case "potion" -> RecipeType.POTION;
      case "decoction" -> RecipeType.DECOCTION;
      default -> throw new JSONException("Unexpected value: " + root.getString("recipeType"));
    };

    final JSONObject object = root.getJSONObject(recipeType.toJSON());

    final Item item = switch (recipeType) {
      case WEAPON -> Weapon.fromJson(object);
      case ARMOR -> Armor.fromJson(object);
      case POTION -> Potion.fromJson(object);
      case DECOCTION -> Decoction.fromJson(object);
    };

    final Recipe recipe = new Recipe(recipeType, item);

    Arrays.stream(EntityType.values())
            .filter(material -> root.has(material.toJSON()) && material.isCraftingMaterial())
            .forEach(material ->
                    recipe.addCraftingMaterial(material, root.getInt(material.toJSON())));

    return recipe;
  }

  public boolean canCraft(final Player player) {
    for (final Map.Entry<EntityType, Integer>
            entry : craftingMaterials.entrySet()) {
      if (entry.getValue() == 0) {
        continue;
      }

      boolean craftable = false;

      for (int i = 0; i < Config.INVENTORY_SIZE; i++) {
        if (player.getItem(i).isEmpty()) {
          continue;
        }

        final Item item = player.getItem(i).get();

        if (entry.getKey() == item.getType() && entry.getValue()
                == (item instanceof Stackable ? ((Stackable) item).getStack() : 1)) {
          craftable = true;
          break;
        }
      }

      if (!craftable) {
        return false;
      }
    }

    return true;
  }

  public EnumMap<EntityType, Integer> getCraftingMaterials() {
    return craftingMaterials;
  }

  public Recipe addCraftingMaterial(final EntityType type,
                                  final int amount) {
    if (!type.isCraftingMaterial()) {
      throw new IllegalArgumentException(
              Utils.substitute("{} is not a crafting material.", type.toJSON()));
    }

    final int actualAmount = craftingMaterials.get(type) + amount;

    if (actualAmount >= 0) {
      craftingMaterials.put(type, actualAmount);
    } else {
      throw new IllegalArgumentException("The amount can not be smaller than the min amount.");
    }

    return this;
  }

  public Recipe addCraftingMaterials(final EnumMap<EntityType, Integer> craftingMaterials) {
    craftingMaterials.forEach(this::addCraftingMaterial);

    return this;
  }

  public RecipeType getRecipeType() {
    return recipeType;
  }

  public EntityType getRecipeTable() {
    return recipeType.getTable();
  }

  public Item getItem() {
    return item;
  }

  @Override
  public <T> T accept(final EntityVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public JSONObject toJSON() {
    final JSONObject jsonObject = new JSONObject();

    jsonObject.put("recipeType", recipeType.toJSON());
    jsonObject.put(recipeType.toJSON(), this.item.toJSON());
    craftingMaterials.forEach((key, value) -> jsonObject.put(key.toJSON(), value));

    return jsonObject;
  }

  public enum RecipeType implements JSONSerializable<String> {
    WEAPON("weapon", EntityType.WORKBENCH),
    ARMOR("armor", EntityType.WORKBENCH),
    POTION("potion", EntityType.CAULDRON),
    DECOCTION("decoction", EntityType.CAULDRON);

    private final String json;
    private final EntityType table;

    RecipeType(final String json, final EntityType table) {
      this.json = json;
      this.table = table;
    }

    public EntityType getTable() {
      return table;
    }

    @Override
    public String toJSON() {
      return this.json;
    }
  }

  private enum RecipeContainer {
    SWORD(RecipeType.WEAPON, "sword", Map.of(EntityType.WOOD, 1, EntityType.STEEL, 2),
            Modifications.SWORD),
    BOW(RecipeType.WEAPON, "bow", Map.of(EntityType.WOOD, 2, EntityType.LEATHER, 1),
            Modifications.BOW),
    ARMOR(RecipeType.ARMOR, "armor", Map.of(EntityType.LEATHER, 2, EntityType.STEEL, 1),
            Modifications.ARMOR),
    POTION(RecipeType.POTION, "potion", Map.of(EntityType.HERBS, 3), Modifications.POTION),
    DECOCTION(RecipeType.DECOCTION, "decoction", Map.of(EntityType.BEETLESHELL, 3),
            Modifications.DECOCTION);

    private final RecipeType recipeType;
    private final String name;
    private final Map<EntityType, Integer> craftingMaterials;
    private final List<ModifyVisitor> modifications;

    RecipeContainer(final RecipeType recipeType, final String name,
                    final Map<EntityType, Integer> craftingMaterials,
                    final List<ModifyVisitor> modifications) {
      this.recipeType = recipeType;
      this.name = name;
      this.craftingMaterials = craftingMaterials;
      this.modifications = modifications;
    }

    public RecipeType getRecipeType() {
      return recipeType;
    }

    public String getName() {
      return name;
    }

    public EnumMap<EntityType, Integer> getCraftingMaterials() {
      return new EnumMap<>(craftingMaterials);
    }

    public List<ModifyVisitor> getModifications() {
      return modifications;
    }
  }

  public static final class RecipeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeFactory.class);

    public static Recipe createRecipe(final int level, final World world) {
      final RecipeContainer recipeContainer = world.roll(RecipeContainer.values());
      final int luck = world.getPlayer().getSkill(Character.CharacterSkill.LUCK);

      LOGGER.debug("recipe D{} -> {}", RecipeContainer.values().length, recipeContainer.name());

      final boolean hasModification = world.roll(luck) % 2 == 0;

      LOGGER.debug("has modification D{} -> {}", luck, hasModification);

      ModifyVisitor modification = null;

      if (hasModification) {
        modification
                = world.roll(recipeContainer.getModifications().toArray(new ModifyVisitor[0]));

        LOGGER.debug("modification D{} -> {}",
                recipeContainer.getModifications().size(), modification);
      }

      boolean hasEffect = false;

      switch (recipeContainer) {
        case SWORD, BOW, ARMOR -> {
          hasEffect = world.roll(10) + luck >= 10;
          LOGGER.debug("has effect D{} -> {}", 10, hasEffect);
        }
        case DECOCTION -> hasEffect = true;
      }

      RecipeEffect effect = null;

      if (hasEffect) {
        effect = world.roll(RecipeEffect.values());
        LOGGER.debug("effect D{} -> {}", RecipeEffect.values().length, effect.getEffect().toJSON());
      }

      final String name = (hasModification ? modification.toString() + " " : "")
              + recipeContainer.getName() + (hasEffect ? " of " + effect.getEffect().toJSON() : "");

      final Optional<Character.CharacterSkill> skill
              = hasEffect ? Optional.of(effect.getEffect()) : Optional.empty();

      final Item item = switch (recipeContainer) {
        case SWORD -> ItemFactory.createWeapon(name, level, 1, skill);
        case BOW -> ItemFactory.createWeapon(name, level, 3, skill);
        case ARMOR -> ItemFactory.createArmor(name, level, skill);
        case POTION -> ItemFactory.createPotion(name, level);
        case DECOCTION -> ItemFactory.createDecoction(name, skill.orElseThrow(ServerError::new));
      };

      final Recipe recipe = new Recipe(recipeContainer.getRecipeType(), item)
              .addCraftingMaterials(recipeContainer.getCraftingMaterials());

      if (hasModification) {
        item.accept(modification);
        recipe.addCraftingMaterial(modification.getMaterial(), modification.getAmount());
      }

      if (hasEffect) {
        recipe.addCraftingMaterial(effect.getMaterial(), effect.getAmount());
      }

      return recipe;
    }

    private enum RecipeEffect {
      STRENGTH(Character.CharacterSkill.STRENGTH, EntityType.RUBY, 1),
      VITALITY(Character.CharacterSkill.VITALITY, EntityType.AMETHYST, 1),
      AGILITY(Character.CharacterSkill.AGILITY, EntityType.EMERALD, 1),
      LUCK(Character.CharacterSkill.LUCK, EntityType.DIAMOND, 1);

      private final Character.CharacterSkill effect;
      private final EntityType material;
      private final int amount;

      RecipeEffect(final Character.CharacterSkill effect, final EntityType material,
                   final int amount) {
        this.effect = effect;
        this.material = material;
        this.amount = amount;
      }

      public Character.CharacterSkill getEffect() {
        return effect;
      }

      public EntityType getMaterial() {
        return material;
      }

      public int getAmount() {
        return amount;
      }
    }
  }
}
