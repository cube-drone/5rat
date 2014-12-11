package jonahss.myfirstproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import org.json.JSONException;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by jonahss on 12/10/14.
 */
public class Prepare extends CommandHandler {

  private MediaPlayer mediaPlayer;
  private Context context;

  public Prepare(MediaPlayer mediaPlayer, Context context) {
    this.mediaPlayer = mediaPlayer;
    this.context = context;
  }

  @Override
  public CommandResult execute(Command command) throws JSONException {

    AssetFileDescriptor afd = context.getResources().openRawResourceFd(R.raw.nut2fair);
    FileDescriptor fd = afd.getFileDescriptor();


    try {

      mediaPlayer.setDataSource(fd);
      mediaPlayer.prepare();

      afd.close(); //maybe don't want to do this here

    } catch (IOException e) {
      e.printStackTrace();
    }

    return new CommandResult(WDStatus.SUCCESS);
  }
}
