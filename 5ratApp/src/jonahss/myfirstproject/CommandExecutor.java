package jonahss.myfirstproject;

import android.content.Context;
import android.media.MediaPlayer;
import org.json.JSONException;

import java.util.HashMap;

/**
 * Command execution dispatch class. This class relays commands to the various
 * handlers.
 *
 */
class CommandExecutor {

  private HashMap<String, CommandHandler> map = new HashMap<String, CommandHandler>();

  public CommandExecutor(Context context, MediaPlayer mediaPlayer) {
    map.put("playSong", new PlaySong(mediaPlayer));
    map.put("prepare", new Prepare(mediaPlayer, context));
    map.put("start", new Start(mediaPlayer));

  }

  /**
   * Gets the handler out of the map, and executes the command.
   *
   * @param command
   *          The {@link Command}
   * @return {@link CommandResult}
   */
  public CommandResult execute(final Command command) {
    try {
      Logger.debug("Got command action: " + command.action());

      if (map.containsKey(command.action())) {
        return map.get(command.action()).execute(command);
      } else {
        return new CommandResult(WDStatus.UNKNOWN_COMMAND,
                "Unknown command: " + command.action());
      }
    } catch (final JSONException e) {
      Logger.error("Could not decode action/params of command");
      return new CommandResult(WDStatus.JSON_DECODER_ERROR,
              "Could not decode action/params of command, please check format!");
    }
  }
}

