package sopra.systemtest.other;


import sopra.comm.Direction;
import sopra.comm.TimeoutException;

public class KillTutor extends ExampleTest {

  public KillTutor() {
    super(KillTutor.class, false);
  }

  @Override
  public long createSeed() {
    return 0;
  }

  @Override
  public void run() throws TimeoutException {
    initializeBasicWorld(false);
    sendAttack(Direction.WEST);
    assertEvent();
    assertEvent();
    assertNextCycle(1);
    assertActNow();
    for (int i = 2; i < 23; i++) {
      sendAttack(Direction.WEST);
      assertNextCycle(i);
      assertActNow();
    }
    sendAttack(Direction.WEST);
    assertUpdateWorld("{\"position\":{\"x\":4,\"y\":-19,\"z\":15},\"representation\":\".\"}");
    assertUpdatePlayer("{\"currentHealth\":10,\"weapon\":"
            + "{\"damage\":1,\"level\":1,\"name\":\"Sword\",\"range\":1},"
            + "\"armor\":{\"armor\":1,\"level\":1,"
            + "\"name\":\"Shield\"},\"luck\":1,\"strength\":1,\"level\":1,"
            + "\"skillPoints\":0,\"vitality\":1,\"name\":\"Player\","
            + "\"maxHealth\":20,\"agility\":1,\"experience\":10}");
    assertEvent();
    assertNextCycle(23);
    assertActNow();
    sendLeave();
    assertGameEnd(false);
  }
}
