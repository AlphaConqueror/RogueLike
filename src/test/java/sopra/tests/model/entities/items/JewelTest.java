package sopra.tests.model.entities.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sopra.model.entities.items.Jewel;

class JewelTest {

  @Test
  void simpleFromJsonTest() {
    for (final Jewel jewel : Jewel.JEWELS) {
      final JSONObject jewelJson = new JSONObject()
              .put("type", jewel.getType().toJSON());
      final Jewel toTest = Jewel.fromJson(jewelJson);

      assertEquals(jewel.getType(), toTest.getType());
    }
  }

  @Test
  void fromJsonIllegalTypeTest() {
    final JSONObject jewelJson = new JSONObject()
            .put("type", "test");

    assertThrows(JSONException.class, () -> Jewel.fromJson(jewelJson));
  }

  @Test
  void checkToJSONTest() {
    for (final Jewel jewel : Jewel.JEWELS) {
      assertEquals("{\"stackSize\":1,\"type\":\""
              + jewel.getType().toJSON() + "\"}", jewel.toJSON().toString());
    }
  }
}