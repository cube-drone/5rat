package jonahss.myfirstproject;


import android.media.MediaPlayer;
import org.json.JSONException;

public class PlaySong extends CommandHandler {

  private MediaPlayer mediaPlayer;

  public PlaySong(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  @Override
  public CommandResult execute(Command command) throws JSONException {
    mediaPlayer.start();

    return new CommandResult(WDStatus.SUCCESS);
  }
}
