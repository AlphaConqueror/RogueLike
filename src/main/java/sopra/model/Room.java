package sopra.model;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import sopra.controller.Die;
import sopra.controller.ServerError;
import sopra.model.entities.Character;
import sopra.model.entities.Chest;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.model.entities.Monster;
import sopra.model.entities.items.Item;
import sopra.utils.JSONIterable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Room implements Iterable<Monster> {

  private final Set<Coordinate> incoming;
  private final int level;
  private final String name;
  private final Map<Coordinate, Coordinate> outgoing;
  private final Map<Coordinate, Tile> tiles;
  private Optional<Chest> chest;
  private Queue<Monster> monsters;

  private Room(final String name, final int level, final Map<Coordinate, Tile> tiles) {
    this.name = name;
    this.level = level;
    this.tiles = tiles;
    this.monsters = new ArrayDeque<>();
    this.incoming = new HashSet<>();
    this.outgoing = new HashMap<>();
    this.chest = Optional.empty();
  }

  public static Room fromJson(final JSONObject root) {
    final Builder builder = new Builder(root.getString("name"), root.getInt("level"));
    for (final JSONObject obj : JSONIterable.from(root.getJSONArray("tiles"))) {
      builder.addCoordinate(Coordinate.fromJson(obj));
    }
    return builder.build();
  }

  /**
   * Adds an {@link Entity} to the room.
   *
   * @param entity the {@link Entity} to be added
   */
  void addEntity(final Entity entity) {
    final Coordinate position = entity.getPosition().orElseThrow(ServerError::new);
    if (!this.tiles.containsKey(position)) {
      throw new ServerError();
    } else {
      this.tiles.get(position).setEntity(entity);
    }
  }

  void addIncoming(final Coordinate coordinate) {
    this.incoming.add(coordinate);
  }

  void addMonster(final Monster monster) {
    this.monsters.add(monster);
  }

  void addOutgoing(final Coordinate position, final Coordinate destination) {
    if (this.outgoing.containsKey(position)) {
      throw new ServerError();
    }
    this.outgoing.put(position, destination);
  }

  /**
   * Removes all {@link Item}s from the room.
   *
   * @return false if S.O.P.R.A. sword is destroyed
   */
  boolean clearItems() {
    for (final Tile tile : this.tiles.values()) {
      final Optional<Entity> entity = tile.getEntity();
      if (entity.isPresent()) {
        if (entity.get().isLegendary()) {
          return false;
        }
        switch (entity.get().getType()) {
          case WEAPON, ARMOR, POTION, DECOCTION, WOOD, STEEL, LEATHER, HERBS,
                  BEETLESHELL, RUBY, AMETHYST, EMERALD, DIAMOND, RECIPE -> tile.removeEntity();
        }
      }
    }
    return true;
  }

  public void enableChest() {
    this.chest.ifPresent(Chest::enable);
  }

  public Set<Coordinate> getCoordinates() {
    return this.tiles.keySet();
  }

  Optional<Coordinate> getDestination(final Coordinate coordinate) {
    return Optional.ofNullable(this.outgoing.get(coordinate));
  }

  Set<Coordinate> getIncoming() {
    return Collections.unmodifiableSet(this.incoming);
  }

  public int getLevel() {
    return this.level;
  }

  public Queue<Monster> getMonsters() {
    return this.monsters.stream().filter(Entity::isEnabled)
        .collect(Collectors.toCollection(ArrayDeque::new));
  }

  public String getName() {
    return this.name;
  }

  Set<Coordinate> getOutgoing() {
    return Collections.unmodifiableSet(this.outgoing.keySet());
  }

  public Optional<Tile> getTile(final Coordinate coordinate) {
    return Optional.ofNullable(this.tiles.get(coordinate));
  }

  boolean hasChest() {
    return this.chest.isPresent();
  }

  boolean hasEntity(final Coordinate coordinate) {
    return this.tiles.get(coordinate).hasEntity();
  }

  @Override
  public int hashCode() {
    return this.level;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    final Room room = (Room) obj;
    return Objects.equals(this.level, room.level);
  }

  /**
   * Computes whether an {@link Entity} can access a field at a given {@link Coordinate}.
   *
   * @param type       the entities type
   * @param coordinate the {@link Coordinate}
   *
   * @return true if field is accessible
   */
  boolean isAccessible(final EntityType type, final Coordinate coordinate) {
    final Optional<Tile> tile = this.getTile(coordinate);
    return tile.isPresent() && !tile.get().hasEntity() && !this.outgoing.containsKey(coordinate)
        && switch (type) {
      case PLAYER -> true;
      case ASSISTANT, BUG, OVERFLOW, TUTOR, PROFESSOR -> !this.incoming.contains(coordinate);
      default -> false;
    };
  }

  /**
   * Computes, whether a field at a certain {@link Coordinate} can be attacked.
   *
   * @param coordinate the {@link Coordinate}
   *
   * @return true is attack is possible
   */
  boolean isAttackable(final Coordinate coordinate) {
    final Optional<Tile> tile = this.getTile(coordinate);
    return tile.isPresent() && (tile.get().hasEntity() || this.outgoing.containsKey(coordinate));
  }

  @Override
  @NotNull
  public Iterator<Monster> iterator() {
    return this.getMonsters().iterator();
  }

  /**
   * Moves a {@link Character} to a given {@link Coordinate}.
   *
   * @param character  the {@link Character} to be moved
   * @param coordinate the {@link Coordinate} to which the {@link Character} should be moved
   */
  void move(final Character character, final Coordinate coordinate) {
    this.tiles.get(character.getPosition().orElseThrow(ServerError::new)).removeEntity();
    character.setPosition(coordinate);
    this.tiles.get(coordinate).setEntity(character);
  }

  /**
   * Removes an {@link Entity} from a tile.
   *
   * @param coordinate the {@link Coordinate} of the tile
   */
  void removeEntity(final Coordinate coordinate) {
    final Tile tile = this.tiles.get(coordinate);
    if (Objects.isNull(tile)) {
      throw new ServerError();
    }
    tile.removeEntity();
  }

  /**
   * Respawns all {@link Monster}s of the room with their respective level.
   *
   * @param level the new level
   * @param die   the die
   */
  void respawn(final int level, final Die die) {
    this.getMonsters()
        .forEach(monster -> this.tiles.get(monster.getPosition().orElseThrow(ServerError::new))
            .removeEntity());
    this.monsters = this.monsters.stream().map(monster -> monster.respawn(level, die))
        .collect(Collectors.toCollection(ArrayDeque::new));
    this.monsters.forEach(this::addEntity);
  }

  void setChest(final Chest chest) {
    if (this.chest.isPresent()) {
      throw new ServerError();
    }
    this.chest = Optional.ofNullable(chest);
  }

  /**
   * Builds a {@link Room} from JSON representation.
   */
  private static class Builder {

    private final int level;
    private final String name;
    private final Map<Coordinate, Tile> tiles;

    private Builder(final String name, final int level) {
      this.name = name;
      this.level = level;
      this.tiles = new HashMap<>();
    }

    void addCoordinate(final Coordinate coordinate) {
      this.tiles.put(coordinate, new Tile(coordinate));
    }

    Room build() {
      return new Room(this.name, this.level, this.tiles);
    }
  }
}
