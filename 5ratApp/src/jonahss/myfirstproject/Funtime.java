package jonahss.myfirstproject;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

public class Funtime extends Activity {

  private MediaPlayer mediaPlayer;
  private CommandExecutor commandExecutor;

  private static final String TAG = "AudioFxDemo";

  private static final float VISUALIZER_HEIGHT_DIP = 500f;

  private Visualizer mVisualizer;

  private LinearLayout mLinearLayout;
  private VisualizerView mVisualizerView;

  private Thread serverThread;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final Funtime self = this;

      getActionBar().setTitle("Yay jQuerySF");

      mLinearLayout = new LinearLayout(this);
      mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
      mLinearLayout.setBackgroundColor(Color.rgb(245, 112, 250));

      setContentView(mLinearLayout);

      File sdDir = Environment.getExternalStorageDirectory();
      mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(sdDir, "5rat.mid")));

//      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//
//          self.finish();
//        }
//
//      });

      commandExecutor = new CommandExecutor(getApplicationContext(), mediaPlayer);
      runServer(commandExecutor, mediaPlayer);

      setupVisualizerFxAndUI();
      mVisualizer.setEnabled(true);
  }

  public void runServer(CommandExecutor commandExecutor, MediaPlayer mediaPlayer) {

    this.serverThread = new Thread(new ServerThread(commandExecutor, mediaPlayer));
    serverThread.start();

  }

  private void setupVisualizerFxAndUI() {
    // Create a VisualizerView (defined below), which will render the simplified audio
    // wave form to a Canvas.
    mVisualizerView = new VisualizerView(this);
    mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            (int)(VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));
    mLinearLayout.addView(mVisualizerView);

    // Create the Visualizer object and attach it to our media player.
    mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                        int samplingRate) {
        mVisualizerView.updateVisualizer(bytes);
      }

      public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
    }, Visualizer.getMaxCaptureRate() / 2, true, false);
  }


  class ServerThread implements Runnable {

    private CommandExecutor executor;

    public ServerThread(CommandExecutor executor, MediaPlayer mediaPlayer) {
      this.executor = executor;
    }

    public void run() {

      Log.d("serverThread", "starting socket server");

      final SocketServer server;

      try {
        server = new SocketServer(4724, executor, mediaPlayer);
        server.listenForever();
        server.close();
      } catch (final SocketServerException e) {
        Logger.error(e.getError());
        System.exit(1);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }



  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.funtime, menu);
        return true;
    }

    @Override
    public void onDestroy() {

      mediaPlayer.release();
      super.onDestroy();
    }

  private class AsyncTaskEx extends AsyncTask<Void, Void, Void> {

    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
    @Override
    protected Void doInBackground(Void... arg0) {
/*
      UiSelector selector0 = new UiSelector().clickable(true);
      UiSelector selector1 = new UiSelector().clickable(true);
      System.out.println("selector:");
      System.out.println(selector0.toString());

      UiObject ob0 = new UiObject(selector0.instance(0));
      UiObject ob0copy = new UiObject(selector1.instance(0));

      UiObject ob1 = new UiObject(selector0.instance(3));
      UiObject ob1copy = new UiObject(selector1.instance(3));

      System.out.println(ob0.equals(ob0copy));
      System.out.println(ob0.equals(ob1));
      System.out.println(ob0.hashCode());
      System.out.println(ob0.hashCode() == ob0copy.hashCode());
*/

      return null;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    @Override
    protected void onPostExecute(Void result) {
      //Write some code you want to execute on UI after doInBackground() completes
      return ;
    }

    @Override
    protected void onPreExecute() {
      //Write some code you want to execute on UI before doInBackground() starts
      return ;
    }
  }
    
}




