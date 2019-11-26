package com.example.dogedice;

import com.example.dogedice.controllers.GenericController;
import com.example.dogedice.controllers.HelperMethods;
import com.example.dogedice.model.GameEngine;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage mainWindow) throws IOException {
    GameEngine gameEngine = new GameEngine(30, 10, 15, 20, new int[]{6,6}, new int[]{});

    // First window must be loaded manually, other windows will use replaceWindow from HelperMethods.
    FXMLLoader loader = HelperMethods.getLoader(HelperMethods.mainWindowFXML);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    GenericController controller = loader.getController();
    controller.setGameEngine(gameEngine);

    mainWindow.setTitle(HelperMethods.mainWindowTitle);
    mainWindow.setScene(scene);
    mainWindow.show();
    mainWindow.toFront();

    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(
          new BufferedInputStream(HelperMethods.getResAsStream("sounds/biggestSmile.wav"))
      );
      clip.open(inputStream);
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      clip.start();
      controller.setClip(clip, scene);
    } catch (Exception e) {
      // Audio won't play, big deal
    }
  }
}

