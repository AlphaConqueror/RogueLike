package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.model.entities.Character;
import sopra.model.entities.EntityType;
import sopra.model.entities.items.Item;
import sopra.model.entities.items.Recipe;

class RecipeTest {

  @Test
  void simpleFromJsonTest() {
    final Item item = Item.ItemFactory.createWeapon("long sword of strength",
            1, 1, Optional.of(Character.CharacterSkill.STRENGTH));
    final JSONObject recipeObject = new JSONObject()
            .put("recipeType", "weapon")
            .put("weapon", item.toJSON())
            .put("steel", 3).put("ruby", 1);
    final Recipe recipe = Recipe.fromJson(recipeObject);

    assertEquals("weapon", recipe.getRecipeType().toJSON());
    assertEquals(item.toJSON().toString(), recipe.getItem().toJSON().toString());
    assertEquals(3, recipe.getCraftingMaterials().get(EntityType.STEEL));
    assertEquals(1, recipe.getCraftingMaterials().get(EntityType.RUBY));
  }

  @Test
  void simpleAddCraftingMaterialTest() {
    final Recipe recipe = new Recipe(null, null);

    assertEquals(recipe, recipe.addCraftingMaterial(EntityType.STEEL, 3));
    assertEquals(3, recipe.getCraftingMaterials().get(EntityType.STEEL));
  }

  @Test
  void addCraftingMaterialIllegalMaterialTest() {
    final Recipe recipe = new Recipe(null, null);

    assertThrows(IllegalArgumentException.class,
            () -> recipe.addCraftingMaterial(EntityType.CHEST, 3));
  }

  @Test
  void addCraftingMaterialBelowMinimalTest() {
    final Recipe recipe = new Recipe(null, null);

    assertThrows(IllegalArgumentException.class,
            () -> recipe.addCraftingMaterial(EntityType.STEEL, -1));
  }

  @Test
  void simpleAddCraftingMaterialsTest() {
    final Recipe recipe = new Recipe(null, null);
    final EnumMap<EntityType, Integer> materials
            = new EnumMap<>(Map.of(EntityType.STEEL, 2, EntityType.RUBY, 3));

    assertEquals(recipe, recipe.addCraftingMaterials(materials));
    assertEquals(2, recipe.getCraftingMaterials().get(EntityType.STEEL));
    assertEquals(3, recipe.getCraftingMaterials().get(EntityType.RUBY));
  }

  @Test
  void checkToJSONTest() {
    final Recipe recipe = new Recipe(Recipe.RecipeType.WEAPON,
            Item.ItemFactory.createWeapon("sword of strength", 1,
                    1, Optional.of(Character.CharacterSkill.STRENGTH)));

    recipe.addCraftingMaterials(new EnumMap<>(
            Map.of(EntityType.STEEL, 1, EntityType.RUBY, 2)));

    assertEquals("{\"weapon\":{\"damage\":1,\"level\":1,"
            + "\"effect\":\"strength\",\"name\":\"sword of strength\",\"range\":1},"
            + "\"steel\":1,\"diamond\":0,\"recipeType\":\"weapon\",\"wood\":0,"
            + "\"beetleshell\":0,\"amethyst\":0,\"leather\":0,\"emerald\":0,"
            + "\"herbs\":0,\"ruby\":2}", recipe.toJSON().toString());
  }
}