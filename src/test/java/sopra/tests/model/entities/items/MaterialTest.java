package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.model.entities.items.Material;

class MaterialTest {

  @Test
  void simpleFromJsonTest() {
    for (final Material material : Material.MATERIALS) {
      final JSONObject materialJson = new JSONObject()
              .put("type", material.getType().toJSON());
      final Material toTest = Material.fromJson(materialJson);

      assertEquals(material.getType(), toTest.getType());
    }
  }

  @Test
  void fromJsonIllegalTypeTest() {
    final JSONObject materialJson = new JSONObject()
            .put("type", "test");

    assertThrows(JSONException.class, () -> Material.fromJson(materialJson));
  }

  @Test
  void checkToJSONTest() {
    for (final Material material : Material.MATERIALS) {
      assertEquals("{\"stackSize\":1,\"type\":\""
              + material.getType().toJSON() + "\"}", material.toJSON().toString());
    }
  }
}
