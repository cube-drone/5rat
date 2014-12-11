package jonahss.myfirstproject;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class Funtime extends Activity {

    private MediaPlayer mediaPlayer;
    private CommandExecutor commandExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funtime);

        //new AsyncTaskEx().execute();

        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nut2fair);

        mediaPlayer = new MediaPlayer();

        commandExecutor = new CommandExecutor(getApplicationContext(), mediaPlayer);
        runServer(commandExecutor);


   //try {
//          commandExecutor.execute(new Command("{\"action\":\"playSong\", \"cmd\":\"action\"}"));
//        } catch (JSONException e) {
//          e.printStackTrace();
//        } catch (CommandTypeException e) {
//          e.printStackTrace();
// }

//        try {
//          commandExecutor.execute(new Command("{\"action\":\"prepare\", \"cmd\":\"action\"}"));
//        } catch (JSONException e) {
//          e.printStackTrace();
//        } catch (CommandTypeException e) {
//          e.printStackTrace();
//        }

//        try {
//          Thread.sleep(10000000);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//
//        try {
//            commandExecutor.execute(new Command("{\"action\":\"start\", \"cmd\":\"action\"}"));
//          } catch (JSONException e) {
//            e.printStackTrace();
//          } catch (CommandTypeException e) {
//            e.printStackTrace();
//          }

    }

    public void runServer(CommandExecutor commandExecutor) {

      Thread serverThread = new Thread(new ServerThread(commandExecutor));
      serverThread.start();

    }

  class ServerThread implements Runnable {

    private CommandExecutor executor;

    public ServerThread(CommandExecutor executor) {
      this.executor = executor;
    }

    public void run() {

      Log.d("serverThread", "starting socket server");

      SocketServer server;
      try {
        server = new SocketServer(4724, executor);
        server.listenForever();
      } catch (final SocketServerException e) {
        Logger.error(e.getError());
        System.exit(1);
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




