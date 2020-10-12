package sopra.controller;

import java.util.Random;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Die {

  private final Random random;

  public Die(final int seed) {
    this.random = new Random(seed);
  }

  /**
   * Selects an element form a list of values at random.
   *
   * @param values the list to chose from
   * @param <T>    the value type
   *
   * @return the selected value
   */
  public final <T> T roll(final T[] values) {
    return values[this.random.nextInt(values.length)];
  }

  /**
   * Rolls a die with the specified number of sides
   *
   * @param sides the number of sides
   *
   * @return the result of the die roll
   */
  public int roll(final int sides) {
    if (sides < 1) {
      throw new IllegalArgumentException();
    }
    return this.random.nextInt(sides) + 1;
  }
}