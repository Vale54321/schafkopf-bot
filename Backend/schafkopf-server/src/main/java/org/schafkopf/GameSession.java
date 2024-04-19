package org.schafkopf;

/** The main entrypoint of the Application. */
public class GameSession {

  private Schafkopf schafkopf;

  /** The main entrypoint of the Application. */
  public GameSession(Schafkopf schafkopf) {
    this.schafkopf = schafkopf;
    System.out.println("new GameSession created.");
    startGame();
  }

  private void startGame() {
    schafkopf.startGame();
  }

}
