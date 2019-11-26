package com.example.dogedice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HelpWindow extends GenericController {
  @FXML
  Label text;
  public void backIconClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.mainWindowFXML,
        HelperMethods.mainWindowTitle,
        mouseEvent,
        this
    );
  }

  @FXML
  public void initialize() {
    try {
      text.setText(IOUtils.toString(
          HelperMethods.getResAsStream("text/helpText"),
          StandardCharsets.UTF_8
      ).strip());
      System.out.println(text.isResizable());
    } catch (IOException e) {
      text.setText("Could not read help text");
    }
  }
  public void spinningDogeClicked(MouseEvent mouseEvent)  {
    HelperMethods.spinningDogeClicked(mouseEvent);
  }
}
