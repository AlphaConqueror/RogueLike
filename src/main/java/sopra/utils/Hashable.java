package sopra.utils;

/**
 * Enforces, that required methods are overridden by implementation.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public interface Hashable {

  @Override
  int hashCode();

  @Override
  boolean equals(Object obj);
}
