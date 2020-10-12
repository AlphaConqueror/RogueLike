package sopra.controller;

import sopra.utils.Utils;

/**
 * @author Lauritz Timm (s9latimm@stud.uni-saarland.de)
 * @author Johannes Lampel (johannes.lampel@cispa.saarland)
 * @version 1.0
 */
public class ServerError extends IllegalStateException {

  private static final long serialVersionUID = 4579405871197048L;

  public ServerError(final String format, final Object... args) {
    super(Utils.substitute(format, args));
  }

  public ServerError() {
    super();
  }

  public ServerError(final Exception e) {
    super(e);
  }
}
