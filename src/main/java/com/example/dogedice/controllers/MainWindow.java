package com.example.dogedice.controllers;

import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class MainWindow extends GenericController {
  public void startButtonClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.playerSelectionWindowFXML,
        HelperMethods.playerSelectionWindowTitle,
        mouseEvent,
        this
    );
  }

  public void highscoreButtonClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.highscoreWindowFXML,
        HelperMethods.highscoreWindowTitle,
        mouseEvent,
        this
    );
  }

  public void helpButtonClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.helpWindowFXML,
        HelperMethods.helpWindowTitle,
        mouseEvent,
        this
    );
  }

  public void dogecoinButtonClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.dogeCoinWindowFXML,
        HelperMethods.dogeCoinWindowTitle,
        mouseEvent,
        this
    );
  }

  public void spinningDogeClicked(MouseEvent mouseEvent) {
    HelperMethods.spinningDogeClicked(mouseEvent);
  }
}
