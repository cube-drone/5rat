package jonahss.myfirstproject;

import org.json.JSONException;

/**
 * Base class for all handlers.
 *
 */
public abstract class CommandHandler {

  /**
   * Abstract method that handlers must implement.
   *
   * @param command
   *          A {@link Command}
   * @return {@link CommandResult}
   * @throws org.json.JSONException
   */
  public abstract CommandResult execute(final Command command)
          throws JSONException;

  /**
   * Returns a generic unknown error message along with your own message.
   *
   * @param msg
   * @return {@link CommandResult}
   */
  protected CommandResult getErrorResult(final String msg) {
    return new CommandResult(WDStatus.UNKNOWN_ERROR, msg);
  }

  /**
   * Returns success along with the payload.
   *
   * @param value
   * @return {@link CommandResult}
   */
  protected CommandResult getSuccessResult(final Object value) {
    return new CommandResult(WDStatus.SUCCESS, value);
  }
}

