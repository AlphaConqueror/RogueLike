package sopra;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sopra.comm.CommException;
import sopra.comm.CommandFactoryImpl;
import sopra.comm.LoggingObserver;
import sopra.comm.ServerConnection;
import sopra.controller.Die;
import sopra.controller.Game;
import sopra.controller.ServerError;
import sopra.controller.command.Command;
import sopra.model.World;
import sopra.view.RenderObserver;

/**
 * Entry point.
 *
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public final class Main {

  private static final String HELP = "h";
  private static final String HELP_LONG = "help";
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final String MAP = "m";
  private static final String MAP_LONG = "map";
  private static final String PORT = "p";
  private static final String PORT_LONG = "port";
  private static final String SEED = "s";
  private static final String SEED_LONG = "seed";
  private static final String TIMEOUT = "t";
  private static final String TIMEOUT_LONG = "timeout";

  private Main() {
    // empty
  }

  private static Options getOptions() {
    final Options options = new Options();
    options.addOption(Option.builder(PORT).longOpt(PORT_LONG).required().hasArg(true).argName("int")
        .type(Integer.class).desc("socket port").build());
    options.addOption(Option.builder(SEED).longOpt(SEED_LONG).required().hasArg(true).argName("int")
        .type(Integer.class).desc("seed for random number generator").build());
    options.addOption(
        Option.builder(TIMEOUT).longOpt(TIMEOUT_LONG).required().hasArg(true).argName("int")
            .type(Integer.class).desc("socket timeout in seconds").build());
    options.addOption(Option.builder(MAP).longOpt(MAP_LONG).required().hasArg(true).argName("path")
        .type(String.class).desc("map in JSON-format (see specification)").build());
    options.addOption(Option.builder(HELP).longOpt(HELP_LONG).desc("Print this message.").build());
    return options;
  }

  public static void main(final String[] args) {
    LOGGER.trace("starting server");
    try {
      final CommandLine cli = new DefaultParser().parse(getOptions(), args);
      if (cli.hasOption(HELP)) {
        printHelp();
      } else {
        final int port = Integer.parseInt(cli.getOptionValue(PORT));
        final int timeout = Integer.parseInt(cli.getOptionValue(TIMEOUT));
        final int seed = Integer.parseInt(cli.getOptionValue(SEED));
        try {
          final File file = new File(cli.getOptionValue(MAP));
          LOGGER.info("load world {}", file.getAbsolutePath());
          final JSONObject json = new JSONObject(
              Objects.requireNonNull(Files.readString(file.toPath(), StandardCharsets.UTF_8)));
          LOGGER
              .info("load schema {}", Main.class.getClassLoader().getResource("schema/world.json"));
          final Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(Objects
              .requireNonNull(
                  Main.class.getClassLoader().getResourceAsStream("schema/world.json")))));
          final World world = World.fromJson(json, new Die(seed), schema);
          final Game game = new Game(world);
          try (final ServerConnection<Command> connection = new ServerConnection<>(port,
              1000 * timeout, new CommandFactoryImpl())) {
            game.addObserver(new LoggingObserver(game.getClass()));
            game.addObserver(new RenderObserver(game.getClass()));
            game.run(connection);
          }
        } catch (final ValidationException e) {
          e.getAllMessages().forEach(LOGGER::error);
          System.exit(1);
        } catch (final IOException | CommException | JSONException e) {
          throw new ServerError(e);
        }
      }
    } catch (final ParseException e) {
      LOGGER.error("{}", e.getMessage());
      printHelp();
    }
    System.exit(0);
  }

  private static void printHelp() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("java -jar <jar>", getOptions(), true);
  }
}
