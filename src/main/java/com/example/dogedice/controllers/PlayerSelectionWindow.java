package com.example.dogedice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class PlayerSelectionWindow extends GenericController {
  @FXML
  Spinner<Integer> humPlayersSpinner, cpuPlayersSpinner;

  @Override
  public void postInitialization() {
    humPlayersSpinner
        .getValueFactory()
        .setValue(gameEngine.getNumberOfHumans());
    cpuPlayersSpinner
        .getValueFactory()
        .setValue(gameEngine.getNumberOfCPUs());
  }

  public void confirmButtonClicked(MouseEvent mouseEvent) throws IOException {
    int numHum = humPlayersSpinner.getValue();
    int numCpu = cpuPlayersSpinner.getValue();

    if (numCpu + numHum < 2) {
      Alert alert = new Alert(AlertType.NONE, "Total number of players must be at least 2.", ButtonType.OK);
      alert.setTitle("Too few players!");
      alert.show();
    } else {
      gameEngine.setNumberOfPlayers(numHum, numCpu);
      HelperMethods.replaceScene(
          HelperMethods.namePlayersWindowFXML,
          HelperMethods.namePlayersWindowTitle,
          mouseEvent,
          this
      );
    }
  }

  public void spinningDogeClicked(MouseEvent mouseEvent)  {
    HelperMethods.spinningDogeClicked(mouseEvent);
  }

  public void backIconClicked(MouseEvent mouseEvent) throws IOException {
    HelperMethods.replaceScene(
        HelperMethods.mainWindowFXML,
        HelperMethods.mainWindowTitle,
        mouseEvent,
        this
    );
  }
}
