package sopra.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import sopra.controller.Config;
import sopra.controller.Die;
import sopra.controller.ServerError;
import sopra.controller.visitor.DefaultEntityVisitor;
import sopra.model.entities.Character;
import sopra.model.entities.Chest;
import sopra.model.entities.Entity;
import sopra.model.entities.EntityType;
import sopra.model.entities.Monster;
import sopra.model.entities.Player;
import sopra.model.entities.items.SwordPart;
import sopra.utils.JSONIterable;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class World {

  private final Die die;
  private final Player player;
  private final Map<Coordinate, Room> rooms;
  private int cycle;
  private boolean enabled;
  private boolean win;

  World(final Player player, final Map<Coordinate, Room> rooms, final Die die) {
    this.rooms = rooms;
    this.player = player;
    this.die = die;
    this.win = false;
    this.enabled = false;
    this.cycle = 0;
  }

  public static World fromJson(final JSONObject root, final Die die, final Schema schema) {
    schema.validate(root);
    final Builder builder = new Builder(die);
    for (final JSONObject obj : JSONIterable.from(root.getJSONArray("rooms"))) {
      builder.addRoom(Room.fromJson(obj));
    }
    for (final JSONObject obj : JSONIterable.from(root.getJSONArray("entities"))) {
      final Coordinate position =
          Objects.requireNonNull(Coordinate.fromJson(obj.getJSONObject("position")));
      if ("door".equals(obj.getString("objectType"))) {
        final JSONObject door = obj.getJSONObject("door");
        final Coordinate destination = Coordinate.fromJson(door.getJSONObject("destination"));
        builder.addDoor(position, destination);
      } else {
        final Entity entity = Objects.requireNonNull(Entity.fromJson(obj, die));
        entity.setPosition(position);
        builder.addEntity(entity, position);
      }
    }
    return builder.build();
  }

  public void addEntity(final Entity entity) {
    this.getRoom(entity.getPosition().orElseThrow(ServerError::new)).orElseThrow(ServerError::new)
        .addEntity(entity);
  }

  public void enable() {
    this.enabled = true;
  }

  public int getCycle() {
    return this.cycle;
  }

  public Optional<Coordinate> getDestination(final Coordinate coordinate) {
    return this.getRoom(coordinate).flatMap(room -> room.getDestination(coordinate));
  }

  public Optional<Entity> getEntity(final Coordinate coordinate) {
    return this.getTile(coordinate).flatMap(Tile::getEntity);
  }

  public Player getPlayer() {
    return this.player;
  }

  public Coordinate getPlayerPosition() {
    return this.player.getPosition().orElseThrow(ServerError::new);
  }

  public Room getPlayerRoom() {
    return this.getRoom(this.getPlayerPosition()).orElseThrow(ServerError::new);
  }

  public Optional<Room> getRoom(final Coordinate coordinate) {
    return Optional.ofNullable(this.rooms.get(coordinate));
  }

  public Optional<Tile> getTile(final Coordinate coordinate) {
    final Optional<Room> room = this.getRoom(coordinate);
    return room.isPresent() ? room.get().getTile(coordinate) : Optional.empty();
  }

  public void incrementCycle() {
    this.cycle++;
  }

  /**
   * Computes whether an entity can access a field at a given coordinate
   *
   * @param type       the entities type
   * @param coordinate the coordinate
   *
   * @return true if field is accessible
   */
  public boolean isAccessible(final EntityType type, final Coordinate coordinate) {
    final Optional<Room> room = this.getRoom(coordinate);
    return room.isPresent() && room.get().isAccessible(type, coordinate);
  }

  /**
   * Computes, whether a field at a certain coordinate can be attacked.
   *
   * @param coordinate the coordinate
   *
   * @return true is attack is possible
   */
  public boolean isAttackable(final Coordinate coordinate) {
    final Optional<Room> room = this.getRoom(coordinate);
    return room.isPresent() && room.get().isAttackable(coordinate);
  }

  public boolean isDisabled() {
    return !this.enabled;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public boolean isWin() {
    return this.win;
  }

  /**
   * Is called when the game is lost.
   */
  public void loose() {
    this.enabled = false;
    this.win = false;
  }

  /**
   * Moves a {@link Character} to a given {@link Coordinate}.
   * <p>
   * If we leave a dungeon and a {@link SwordPart} is destroyed, the game is lost.
   *
   * @param character  the {@link Character} to be moved
   * @param coordinate the {@link Coordinate} to which the {@link Character} should be moved
   */
  public void move(final Character character, final Coordinate coordinate) {
    final Room current = this.getPlayerRoom();
    current.removeEntity(character.getPosition().orElseThrow(ServerError::new));
    final Room room = this.rooms.get(coordinate);
    if (!Objects.equals(room, current)) {
      if (room.getLevel() == Config.ISLAND_LEVEL) {
        room.respawn(this.player.getLevel(), this.die);
      }
      if (!current.clearItems()) {
        this.loose();
      }
    }
    character.setPosition(coordinate);
    room.move(character, coordinate);
  }

  public void removeEntity(final Coordinate coordinate) {
    this.getTile(coordinate).orElseThrow(ServerError::new).removeEntity();
  }

  public int roll(final int sides) {
    return this.die.roll(sides);
  }

  public final <T> T roll(final T[] values) {
    return this.die.roll(values);
  }

  /**
   * Rolls multiple dice with a specified number of sides.
   *
   * @param dice  number of dice
   * @param sides number of sides
   *
   * @return the sum of all rolls
   */
  public int roll(final int dice, final int sides) {
    if (dice < 0 || sides <= 0) {
      throw new IllegalArgumentException("Sides can not be 0!");
    }
    return IntStream.range(0, dice).map(i -> this.roll(sides)).sum();
  }

  /**
   * This method is called when the game is won.
   */
  public void win() {
    this.enabled = false;
    this.win = true;
  }

  /**
   * Builds a {@link World} from JSON representation and validates specified criteria.
   */
  public static class Builder {

    private final Die die;
    private final Map<Coordinate, Room> rooms;
    private Optional<Player> player;

    private Builder(final Die die) {
      this.die = die;
      this.player = Optional.empty();
      this.rooms = new HashMap<>();
    }

    private static void checkDungeon(final Room room) {
      if (!room.hasChest()) {
        throw new ServerError("Room {} has no chest.", room.getName());
      }
      boolean assistent = false;
      for (final Monster monster : room) {
        if (monster.getLevel() != room.getLevel()) {
          throw new ServerError("Monster with level {} in {} with level {}.", monster.getLevel(),
              room.getName(), room.getLevel());
        }
        switch (monster.getType()) {
          case TUTOR, PROFESSOR -> throw new ServerError("Unexpected {} in {}.", monster.getType(),
              room.getName());
          case ASSISTANT -> {
            if (assistent) {
              throw new ServerError("Multiple assistants in {}.", room.getName());
            }
            assistent = true;
          }
        }
      }
      if (!assistent) {
        throw new ServerError("No assistant in {}.", room.getName());
      }
    }

    private static void checkFinale(final Room room) {
      if (room.hasChest()) {
        throw new ServerError("Unexpected chest in {}.", room.getName());
      }
      boolean professor = false;
      for (final Monster monster : room) {
        switch (monster.getType()) {
          case TUTOR -> throw new ServerError("Unexpected tutor in {}.", room.getName());
          case PROFESSOR -> {
            if (professor) {
              throw new ServerError("Multiple professors in {}.", room.getName());
            }
            professor = true;
          }
        }
      }
      if (!professor) {
        throw new ServerError("No professor in {}.", room.getName());
      }
    }

    private static void checkIsland(final Room room) {
      for (final Monster monster : room) {
        if (monster.getType() == EntityType.PROFESSOR) {
          throw new ServerError("Professor in {}", room.getName());
        }
      }
    }

    private void addChest(final Chest chest, final Coordinate position) {
      if (!this.rooms.containsKey(position)) {
        throw new ServerError("Missing tile {}", position);
      }
      final Room room = this.rooms.get(position);
      room.setChest(chest);
    }

    void addDoor(final Coordinate position, final Coordinate destination) {
      if (!this.rooms.containsKey(position)) {
        throw new ServerError("Missing tile {}", position);
      }
      if (!this.rooms.containsKey(destination)) {
        throw new ServerError("Missing tile {}", destination);
      }
      this.rooms.get(position).addOutgoing(position, destination);
      this.rooms.get(destination).addIncoming(destination);
    }

    void addEntity(final Entity entity, final Coordinate position) {
      if (!this.rooms.containsKey(position)) {
        throw new ServerError("Missing tile {}", position);
      }
      final Room room = this.rooms.get(position);
      if (room.getLevel() == 8 && entity.getType() != EntityType.PROFESSOR
          && entity.getType() != EntityType.PLAYER) {
        throw new ServerError("Level problems with dungeon!");
      }
      entity.setPosition(position);
      entity.accept(new BuilderVisitor(this, position));
      this.rooms.get(position).addEntity(entity);
    }

    void addMonster(final Monster monster, final Coordinate position) {
      monster.setPosition(position);
      if (!this.rooms.containsKey(position)) {
        throw new ServerError("Missing tile {}", position);
      }
      this.rooms.get(position).addMonster(monster);
    }

    void addRoom(final Room room) {
      room.getCoordinates().forEach(coordinate -> {
        if (this.rooms.containsKey(coordinate)) {
          throw new ServerError("Duplicate tile {}", coordinate);
        }
        this.rooms.put(coordinate, room);
      });
    }

    World build() {
      this.validate();
      return new World(this.player.orElseThrow(ServerError::new), this.rooms, this.die);
    }

    private void checkDoors() {
      this.rooms.values().stream().distinct().forEach(room -> {
        if (room.getOutgoing().isEmpty()) {
          throw new ServerError("Room {} has no outgoing door.", room.getLevel());
        }
        room.getIncoming().stream().filter(room::hasEntity).forEach(coordinate -> {
          final Entity entity = room.getTile(coordinate).orElseThrow(ServerError::new).getEntity()
              .orElseThrow(ServerError::new);
          if (entity.getType() != EntityType.PLAYER) {
            throw new ServerError("Entity on destination {}.", coordinate);
          }
        });
        room.getOutgoing().forEach(coordinate -> {
          if (room.hasEntity(coordinate)) {
            throw new ServerError("Entity on door {}.", coordinate);
          }
          final Coordinate destination =
              room.getDestination(coordinate).orElseThrow(ServerError::new);
          final Room other = this.rooms.get(destination);
          if (Objects.equals(other, room)) {
            throw new ServerError("Door {} from {} into same room {} at {}.", coordinate,
                room.getName(), other.getName(), destination);
          }
        });
      });
    }

    private void checkEntities() {
      this.rooms.values().stream().distinct().forEach(room -> {
        final int level = room.getLevel();
        switch (level) {
          case 0 -> Builder.checkIsland(room);
          case 8 -> Builder.checkFinale(room);
          default -> Builder.checkDungeon(room);
        }
      });
    }

    private void checkNumbering() {
      final List<Room> rooms =
          this.rooms.values().stream().distinct().sorted(Comparator.comparingInt(Room::getLevel))
              .collect(Collectors.toList());
      if (rooms.size() != Config.ROOMS) {
        throw new ServerError("Wrong number of rooms {}.", rooms.size());
      }
      for (int i = 0; i < Config.ROOMS; i++) {
        if (rooms.get(i).getLevel() != i) {
          throw new ServerError("Missing room with level {}.", i);
        }
      }
    }

    private void checkSpacing() {
      for (final Entry<Coordinate, Room> entry : this.rooms.entrySet()) {
        for (final Coordinate frontier : entry.getKey().cubeSpiral(Config.SIGHT_RADIUS)) {
          final Optional<Room> optional = Optional.ofNullable(this.rooms.get(frontier));
          if (optional.isPresent() && !Objects.equals(optional.get(), entry.getValue())) {
            throw new ServerError("Room {} to close to {}.", entry.getValue().getName(),
                optional.get().getName());
          }
        }
      }
    }

    void setPlayer(final Player player, final Coordinate position) {
      if (!this.rooms.containsKey(position)) {
        throw new ServerError("Missing tile {}", position);
      }
      if (this.player.isPresent()) {
        throw new ServerError("Multiple players.");
      }
      player.setPosition(position);
      this.player = Optional.of(player);
    }

    void validate() {
      this.checkNumbering();
      this.checkSpacing();
      this.checkEntities();
      this.checkDoors();
    }

    static class BuilderVisitor extends DefaultEntityVisitor {

      private final Builder builder;
      private final Coordinate position;

      BuilderVisitor(final Builder builder, final Coordinate position) {
        this.builder = builder;
        this.position = position;
      }

      @Override
      public void handle(final Monster monster) {
        this.builder.addMonster(monster, this.position);
      }

      @Override
      public void handle(final Player player) {
        this.builder.setPlayer(player, this.position);
      }

      @Override
      public void handle(final Chest chest) {
        this.builder.addChest(chest, this.position);
      }
    }
  }
}