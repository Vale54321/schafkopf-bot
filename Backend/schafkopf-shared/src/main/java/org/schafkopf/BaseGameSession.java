package org.schafkopf;

import java.util.List;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The main entrypoint of the Application.
 */
public abstract class BaseGameSession implements MessageSender {

  protected static final Logger logger = LoggerFactory.getLogger(BaseGameSession.class);
  protected Thread spielThread;
  protected Schafkopf schafkopf;
  protected List<Player> players;

  void startGame(List<Player> players) {
    logger.info("Starting game");
    for (int i = players.size(); i < 4; i++) {
      players.add(new BotPlayer("Bot " + i));
    }

    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.GAME_START_READY));

    //wait for 5 seconds
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    spielThread = new Thread(() -> {
      try {
        schafkopf = new Schafkopf(players.toArray(Player[]::new), this);
        schafkopf.startGame();
      } catch (NotEnoughPlayersException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    spielThread.start();
  }
}
