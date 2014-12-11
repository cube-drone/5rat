package jonahss.myfirstproject;

import android.content.Context;
import android.media.MediaPlayer;
import org.json.JSONException;

/**
 * Created by jonahss on 12/10/14.
 */
public class Start extends CommandHandler {

  private MediaPlayer mediaPlayer;
  private Context context;

  public Start(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    this.context = context;
  }

  @Override
  public CommandResult execute(Command command) throws JSONException {

    mediaPlayer.start();

    return new CommandResult(WDStatus.SUCCESS);
  }
}
