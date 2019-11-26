package com.example.dogedice.controllers;

import com.example.dogedice.model.GameEngine;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import javax.sound.sampled.Clip;

public abstract class GenericController {
  GameEngine gameEngine;
  private Clip clip;

  GenericController() {
    gameEngine = null;
    clip = null;
  }

  public void inheritSettings(GenericController controller, Scene scene) {
    gameEngine = controller.gameEngine;
    setClip(controller.clip, scene);
  }

  public void setGameEngine(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  public void setClip(Clip clip, Scene scene) {
    this.clip = clip;

    if (this.clip == null || scene == null) {
      // Nothing we can do
      return;
    }

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.M && clip.isRunning()) {
        clip.stop();
      } else if (event.getCode() == KeyCode.M) {
        clip.start();
      }
    });
  }

  public void postInitialization() {
    // Does nothing by default
  }
}
