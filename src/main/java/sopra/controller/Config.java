package sopra.controller;

import java.util.List;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Config {

  public static final int ATTACK_RADIUS = 4;
  public static final int CACHE_SIZE = 128;
  public static final int COMBAT_RADIUS = 2;
  public static final int DEFAULT_SEED = 42;
  public static final int DROP_DISTANCE = 1;
  public static final int DURATION = 10;
  public static final List<Integer> EXPERIENCE_LEVEL =
      List.of(0, 100, 500, 1_400, 3_000, 5_500, 9_100, 14_000, 20_400, 28_500);
  public static final int INDEX_OFFSET = 1;
  public static final int INVENTORY_SIZE = 9;
  public static final int ISLAND_LEVEL = 0;
  public static final int LOOT_ROLL_SIDES = 10;
  public static final int MAX_PLAYER_LEVEL = 10;
  public static final int MAX_SKILL_LEVEL = 9;
  public static final int MINIMAL_RANGE = 1;
  public static final int MINIMAL_ITEM_VALUE = 1;
  public static final int MIN_STACK = 1;
  public static final int MONSTER_ATTACK_RADIUS = 1;
  public static final int NUMBER_SWORD_PARTS = 7;
  public static final int PROF_BASE_DAMAGE = 0;
  public static final int PROF_LEVEL = 8;
  public static final int ROOMS = 9;
  public static final int SIGHT_RADIUS = 6;
  public static final int SOPRA_SWORD = 42;
  static final int MAX_CYCLE = 100_000;
}
