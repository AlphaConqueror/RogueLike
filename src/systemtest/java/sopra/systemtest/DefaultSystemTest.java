package sopra.systemtest;

import sopra.comm.TimeoutException;
import sopra.controller.Config;
import sopra.systemtest.api.SystemTest;

public abstract class DefaultSystemTest extends SystemTest {

  protected DefaultSystemTest(final Class<?> subclass, final boolean fail) {
    super(subclass, fail);
  }

  @Override
  protected long createSeed() {
    return Config.DEFAULT_SEED;
  }

  @Override
  protected void run() throws TimeoutException, AssertionError {
    sendRegister();
    assertGameStarted();
    assertSetCamera(5, -20, 15);
    assertDrawWorld("Island");

    assertEvents(38);

    assertNextCycle(0);
    assertActNow();

    execute();
    end();
  }

  protected abstract void execute() throws TimeoutException;

  protected void assertEvents(final int amount) throws TimeoutException {
    for (int i = 0; i < amount; i++) {
      assertEvent();
    }
  }

  private void end() throws TimeoutException {
    sendLeave();
    assertGameEnd(false);
  }
}
