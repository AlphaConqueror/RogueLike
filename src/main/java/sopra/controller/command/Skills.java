package sopra.controller.command;

import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import sopra.comm.Observer;
import sopra.model.World;
import sopra.model.entities.Character.CharacterSkill;
import sopra.model.entities.EntityType;
import sopra.model.entities.Player;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class Skills implements Command {

  @Override
  public void execute(final World world, final Queue<Observer> observers) {
    final Player player = world.getPlayer();
    Command.calculateState(world, player);
    switch (player.getState()) {
      case REGISTER -> observers.forEach(Observer::notifyRegistrationAborted);
      case DEFAULT, ATTACKED, COMBAT -> {
        final JSONObject root = new JSONObject();
        root.put("listType", "skills");
        root.put("skills", new JSONArray(
            Arrays.stream(CharacterSkill.values(EntityType.PLAYER)).map(CharacterSkill::toJSON)
                .collect(Collectors.toList())));
        observers.forEach(observer -> observer.notifyChoiceWindow(root.toString()));
      }
    }
  }

  @Override
  public String toString() {
    return "SKILLS()";
  }
}
