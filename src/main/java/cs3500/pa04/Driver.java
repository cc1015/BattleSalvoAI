package cs3500.pa04;

import cs3500.pa04.controller.BattleSalvoController;
import cs3500.pa04.controller.Controller;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.model.AbPlayer;
import cs3500.pa04.model.AdvancedPlayer;
import cs3500.pa04.model.AutomatedPlayer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {
  public static int winCount = 0;
  public static int drawCount = 0;
  public static int loseCount = 0;
  public static String other = "";

  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      Readable input = new InputStreamReader(System.in);
      Appendable output = new PrintStream(System.out);
      Controller controller = new BattleSalvoController(input, output, new Random());
      controller.executeGame();
    } else if (args.length == 2) {
      int port = Integer.parseInt(args[1]);
      try {
        for (int i = 0; i < 200; i += 1) {
          Socket socket = new Socket(args[0], port);
          AbPlayer computerPlayer = new AdvancedPlayer(null, new Random());
          Controller controller = new ProxyController(socket, computerPlayer);
          controller.executeGame();
        }
        System.out.println("win count: " + winCount);
        System.out.println("draw count: " + drawCount);
        System.out.println("lose count: " + loseCount);
        System.out.println(other);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}