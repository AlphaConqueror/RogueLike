package sopra.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.ServerError;
import sopra.model.entities.items.Item;
import sopra.utils.JSONSerializable;

class Inventory implements JSONSerializable<JSONObject> {

  private final List<Optional<Item>> slots;

  Inventory() {
    this.slots = new ArrayList<>();
    IntStream.range(0, Config.INVENTORY_SIZE).forEach(i -> this.slots.add(i, Optional.empty()));
  }

  boolean add(final Item item) {
    for (final Optional<Item> slot : this.slots) {
      if (slot.isPresent() && slot.get().isStackable(item)) {
        slot.get().stack(item);
        return true;
      }
    }
    for (int i = 0; i < this.slots.size(); i++) {
      final Optional<Item> slot = this.slots.get(i);
      if (slot.isEmpty()) {
        this.slots.set(i, Optional.of(item));
        return true;
      }
    }
    return false;
  }

  void drop(final int index, final int amount) {
    final Optional<Item> item = this.slots.get(index);
    if (item.isPresent() && (item.get().remove(amount))) {
      this.slots.set(index, Optional.empty());
    }
  }

  public Optional<Item> get(final int index) {
    return this.slots.get(index);
  }

  void set(final int index, final Item item) {
    this.set(index, Optional.ofNullable(item));
  }

  void set(final int index, final Optional<Item> item) {
    this.slots.set(index, item);
  }

  @Override
  public JSONObject toJSON() {
    final JSONObject root = new JSONObject();
    final JSONArray items = new JSONArray();
    for (final Optional<Item> optional : this.slots) {
      final JSONObject item = new JSONObject();
      if (optional.isPresent()) {
        final String type = optional.orElseThrow().getType().getObjectType().toJSON();
        item.put("objectType", type);
        item.put(type, optional.orElseThrow(ServerError::new).toJSON());
      } else {
        item.put("objectType", "empty");
      }
      items.put(item);
    }
    return root.put("listType", "items").put("items", items);
  }
}
