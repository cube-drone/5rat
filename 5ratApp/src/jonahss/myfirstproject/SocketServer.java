package jonahss.myfirstproject;


import android.util.Log;
import org.json.JSONException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

/**
 * The SocketServer class listens on a specific port for commands from Appium,
 * and then passes them on to the {@link CommandExecutor} class. It will
 * continue to listen until the command is sent to exit.
 */
class SocketServer {

  ServerSocket   server;
  Socket         client;
  BufferedReader in;
  BufferedWriter out;
  boolean        keepListening;
  private final Timer       timer    = new Timer("WatchTimer");
  private final CommandExecutor executor;

  /**
   * Constructor
   *
   * @param port
   * @throws SocketServerException
   */
  public SocketServer(final int port, final CommandExecutor commandExecutor) throws SocketServerException {
    keepListening = true;
    executor = commandExecutor;
    try {
      server = new ServerSocket(port);
      Log.d("serverThread", "Socket opened on port " + port);
    } catch (final IOException e) {
      throw new SocketServerException(
              "Could not start socket server listening on " + port);
    }

  }

  /**
   * Constructs an @{link AndroidCommand} and returns it.
   *
   * @param data
   * @return @{link AndroidCommand}
   * @throws JSONException
   * @throws CommandTypeException
   */
  private Command getCommand(final String data) throws JSONException, CommandTypeException {
    return new Command(data);
  }

  private StringBuilder input = new StringBuilder();

  /**
   * When data is available on the socket, this method is called to run the
   * command or throw an error if it can't.
   *
   * @throws SocketServerException
   */
  private void handleClientData() throws SocketServerException {
    try {
      input.setLength(0); // clear

      String res;
      int a;
      // (char) -1 is not equal to -1.
      // ready is checked to ensure the read call doesn't block.
      while ((a = in.read()) != -1 && in.ready()) {
        input.append((char) a);
      }
      String inputString = input.toString();
      Log.d("serverThread", "Got data from client: " + inputString);
      try {
        Command cmd = getCommand(inputString);
        Log.d("serverThread", "Got command of type " + cmd.commandType().toString());
        res = runCommand(cmd);
        Log.d("serverThread", "Returning result: " + res);
      } catch (final CommandTypeException e) {
        res = new CommandResult(WDStatus.UNKNOWN_ERROR, e.getMessage())
                .toString();
      } catch (final JSONException e) {
        res = new CommandResult(WDStatus.UNKNOWN_ERROR,
                "Error running and parsing command").toString();
      }
      out.write(res);
      out.flush();
    } catch (final IOException e) {
      throw new SocketServerException("Error processing data to/from socket ("
              + e.toString() + ")");
    }
  }

  /**
   * Listens on the socket for data, and calls {@link #handleClientData()} when
   * it's available.
   *
   * @throws SocketServerException
   */
  public void listenForever() throws SocketServerException {
    Log.d("serverThread", "Socket Server Ready");

    try {
      client = server.accept();
      Log.d("serverThread", "Client connected");
      in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
      out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
      while (keepListening) {
        handleClientData();
      }
      in.close();
      out.close();
      client.close();
      Log.d("serverThread", "Closed client connection");
    } catch (final IOException e) {
      throw new SocketServerException("Error when client was trying to connect");
    }
  }

  /**
   * When {@link #handleClientData()} has valid data, this method delegates the
   * command.
   *
   * @param cmd
   *     AndroidCommand
   * @return Result
   */
  private String runCommand(final Command cmd) {
    CommandResult res;
    if (cmd.commandType() == CommandType.SHUTDOWN) {
      keepListening = false;
      res = new CommandResult(WDStatus.SUCCESS, "OK, shutting down");
    } else if (cmd.commandType() == CommandType.ACTION) {
      try {
        res = executor.execute(cmd);
      } catch (final Exception e) { // Here we catch all possible exceptions and return a JSON Wire Protocol UnknownError
        // This prevents exceptions from halting the bootstrap app
        res = new CommandResult(WDStatus.UNKNOWN_ERROR, e.getMessage());
      }
    } else {
      // this code should never be executed, here for future-proofing
      res = new CommandResult(WDStatus.UNKNOWN_ERROR,
              "Unknown command type, could not execute!");
    }
    return res.toString();
  }
}
