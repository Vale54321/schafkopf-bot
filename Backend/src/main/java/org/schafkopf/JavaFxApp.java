package org.schafkopf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/** Frontend Window. */
public class JavaFxApp extends Application {

  private static final String FRONTEND_URL =
      "http://localhost:8081"; // Replace with your frontend URL

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // Create a WebView
    WebView webView = new WebView();

    // Load the frontend URL
    webView.getEngine().load(FRONTEND_URL);

    // Create a Scene with the WebView
    Scene scene = new Scene(webView, 800, 600);

    // Set up the Stage
    primaryStage.setTitle("Schafkopfen");
    primaryStage.setScene(scene);

    primaryStage.setFullScreenExitHint("");

    // Set the stage to fullscreen
    primaryStage.setFullScreen(true);

    // Add event handler for the Escape key to toggle fullscreen
    scene.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.F11) {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
          }
        });

    // Show the Stage
    primaryStage.show();
  }
}
