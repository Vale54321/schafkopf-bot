package org.schafkopf;

public class SchafkopfException extends Exception {

  private SchafkopfException(String message) {
    super(message);
  }
  
  public static class NotEnoughPlayersException extends SchafkopfException {

    public NotEnoughPlayersException() {
      super("Not enough players to start the game");
    }

    // You can also include additional constructors or methods if needed
  }

  // Specific exception class for invalid move
  public class InvalidMoveException extends SchafkopfException {

    // Constructor with a message
    public InvalidMoveException(String message) {
      super(message);
    }

    // You can also include additional constructors or methods if needed
  }
}

