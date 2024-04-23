package org.schafkopf;

/**
 * Class that represents one Frontend Connection.
 */
public class SchafkopfException extends Exception {

  private SchafkopfException(String message) {
    super(message);
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public static class NotEnoughPlayersException extends SchafkopfException {

    public NotEnoughPlayersException() {
      super("Not enough players to start the game");
    }

    // You can also include additional constructors or methods if needed
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public class InvalidMoveException extends SchafkopfException {

    // Constructor with a message
    public InvalidMoveException(String message) {
      super(message);
    }

    // You can also include additional constructors or methods if needed
  }
}

