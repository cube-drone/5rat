package jonahss.myfirstproject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * This proxy embodies the command that the handlers execute.
 *
 */
public class Command {

  JSONObject json;
  CommandType cmdType;

  public Command(String action) {
    this.cmdType = CommandType.ACTION;

  }

  /**
   * Return the action string for this command.
   *
   * @return String
   * @throws JSONException
   */
  public String action() throws JSONException {

    return "playSong";
  }

  public CommandType commandType() {
    return cmdType;
  }

  /**
   * Return a hash table of name, value pairs as arguments to the handlers
   * executing this command.
   *
   * @return Hashtable<String, Object>
   * @throws JSONException
   */
  public Hashtable<String, Object> params() throws JSONException {
    final JSONObject paramsObj = json.getJSONObject("params");
    final Hashtable<String, Object> newParams = new Hashtable<String, Object>();
    final Iterator<?> keys = paramsObj.keys();

    while (keys.hasNext()) {
      final String param = (String) keys.next();
      newParams.put(param, paramsObj.get(param));
    }
    return newParams;
  }

  /**
   * Set the command {@link CommandType type}
   *
   * @param stringType
   *          The string of the type (i.e. "shutdown" or "action")
   * @throws CommandTypeException
   */
  public void setType(final String stringType) throws CommandTypeException {
    if (stringType.equals("shutdown")) {
      cmdType = CommandType.SHUTDOWN;
    } else if (stringType.equals("action")) {
      cmdType = CommandType.ACTION;
    } else {
      throw new CommandTypeException("Got bad command type: " + stringType);
    }
  }
}
