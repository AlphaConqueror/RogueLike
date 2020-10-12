package sopra.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 * Contains some useful utility methods.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Utils {

  /**
   * Load resource file as {@link String}.
   *
   * @param subclass class to provide {@link ClassLoader}
   * @param name     ressource path to file
   *
   * @return file content
   *
   * @throws IOException from {@link InputStreamReader} or {@link BufferedReader}
   */
  public static String loadResource(final Class<?> subclass, final String name) throws IOException {
    LoggerFactory.getLogger(subclass)
        .trace("loading {}", subclass.getClassLoader().getResource(name));
    try (final InputStreamReader input = new InputStreamReader(
        Objects.requireNonNull(subclass.getClassLoader().getResourceAsStream(name)),
        StandardCharsets.UTF_8)) {
      try (final BufferedReader reader = new BufferedReader(input)) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    }
  }

  /**
   * Supplies {@link org.slf4j.Logger}-style format substitution.
   *
   * @param format format {@link String}
   * @param args   array of {@link Object}s
   *
   * @return formatted {@link String}
   */
  public static String substitute(final String format, final Object... args) {
    return String.format(format.replaceAll("\\{}", "%s"),
        Arrays.stream(args).map(Object::toString).toArray());
  }
}
