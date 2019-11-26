package com.example.dogedice.controllers;

import com.example.dogedice.model.GameEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HelperMethods {
  // Window titles
  public static final String mainWindowTitle = "Doge Dice";
  public static final String dogeCoinWindowTitle = "Donate!";
  public static final String helpWindowTitle = "How to play";
  public static final String playerSelectionWindowTitle = "Player Selection";
  public static final String highscoreWindowTitle = "Highscore";
  public static final String namePlayersWindowTitle = "Name Players";
  public static final String playWindowTitle = "Game!";
  public static final String winnerWindowTitle = "Winner!";

  // FXML paths
  public static final String mainWindowFXML = "fxml/mainWindow.fxml";
  public static final String dogeCoinWindowFXML = "fxml/dogeCoinWindow.fxml";
  public static final String helpWindowFXML = "fxml/helpWindow.fxml";
  public static final String playerSelectionWindowFXML = "fxml/playerSelectionWindow.fxml";
  public static final String highscoreWindowFXML = "fxml/highScoreWindow.fxml";
  public static final String namePlayersWindowFXML = "fxml/namePlayersWindow.fxml";
  public static final String playWindowFXML = "fxml/playWindow.fxml";
  public static final String winnerWindowFXML = "fxml/winnerWindow.fxml";

  /**
   * SpinningDoge image is used on most (if not all) screens, and has some fairly complex click actions.
   * The onButtonClick action for any spinningDoge image can be replaced with this method.
   * @param mouseEvent The MouseEvent from which we can retrieve the SpinningDoge.
   */
  public static void spinningDogeClicked(MouseEvent mouseEvent) {
    ImageView imageView = (ImageView) mouseEvent.getSource();
    Image image = imageView.getImage();

    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      imageView.setScaleX(imageView.getScaleX() * -1);
    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      if (image.getUrl().contains("dogespin")) {
        Image newImage = new Image(getRes("images/rainbowDoge.gif").toExternalForm());
        imageView.setImage(newImage);
      } else {
        Image newImage = new Image(getRes("images/dogespin.gif").toExternalForm());
        imageView.setImage(newImage);
      }
    }
  }

  /**
   * Retrieves a given resource.
   * @param fileName The resource we're retrieving.
   * @return A URL instance for the resource.
   */
  public static URL getRes(String fileName) {
    return Thread.currentThread().getContextClassLoader().getResource(fileName);
  }

  public static InputStream getResAsStream(String fileName) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
  }

  /**
   * Retrieves an FXMLLoader instance preloaded with the desired FXML file.
   * @param fxmlPath Path to the FXML we're loading.
   * @return The FXMLLoader.
   */
  public static FXMLLoader getLoader(String fxmlPath) {
    return new FXMLLoader(getRes(fxmlPath));
  }

  /**
   * Replaces the scene in the current stage and passes on the GameEngine instance to the next controller.
   * @param fxmlPath Path to the FXML we're loading.
   * @param windowTitle The new title for the stage.
   * @param mouseEvent MouseEvent from which we can retrieve the stage.
   * @param oldController The old controller from which we can inherit gameEngine and clip
   * @throws IOException If the FXML can't be loaded.
   */
  static void replaceScene(String fxmlPath,
                           String windowTitle,
                           MouseEvent mouseEvent,
                           GenericController oldController) throws IOException {
    Stage stage = (Stage) ((Node) mouseEvent.getSource())
        .getScene()
        .getWindow();
    stage.setTitle(windowTitle);
    FXMLLoader loader = getLoader(fxmlPath);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    GenericController controller = loader.getController();
    controller.inheritSettings(oldController, scene);
    controller.postInitialization();
    stage.setScene(scene);
  }
}
