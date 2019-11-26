package com.example.dogedice.controllers;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DogeCoinWindow extends GenericController {
  public void backIconClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.mainWindowFXML,
        HelperMethods.mainWindowTitle,
        mouseEvent,
        this
    );
  }
}
