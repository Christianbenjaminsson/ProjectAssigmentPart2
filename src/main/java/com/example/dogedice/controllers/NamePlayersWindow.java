package com.example.dogedice.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NamePlayersWindow extends GenericController {
  private int numCpu = 0;
  private int numHum = 0;
  private List<TextField> humans = new ArrayList<>();
  private List<TextField> cpus = new ArrayList<>();

  @FXML
  VBox namePlayersBox;

  @Override
  public void postInitialization() {
    for (int i = 1; i <= gameEngine.getNumberOfHumans(); i++) {
      addPlayer("Human", i, false, humans);
    }
    for (int i = 1; i <= gameEngine.getNumberOfCPUs(); i++) {
      addPlayer("Robo Doge", i, true, cpus);
    }
  }

  private void addPlayer(String playerType, int num, boolean isDisabled, List<TextField> list) {
    Label label = new Label(playerType + ": ");
    label.setLayoutY(5);
    TextField textField = new TextField(String.format("%s #%s", playerType, num));
    textField.prefWidth(50);
    textField.setLayoutX(90);
    textField.setDisable(isDisabled);
    list.add(textField);
    namePlayersBox
        .getChildren()
        .add(new Group(label, textField));
  }

  public void confirmButtonClicked(MouseEvent mouseEvent) throws IOException {
    humans.forEach(x -> gameEngine.addHumanPlayer(x.getText()));
    cpus.forEach(x -> gameEngine.addCpuPlayer(x.getText()));
    HelperMethods.replaceScene(
        HelperMethods.playWindowFXML,
        HelperMethods.playWindowTitle,
        mouseEvent,
        this
    );
  }

  public void spinningDogeClicked(MouseEvent mouseEvent)  {
    HelperMethods.spinningDogeClicked(mouseEvent);
  }

  public void backButtonClicked(MouseEvent mouseEvent) throws IOException {
    System.out.println(gameEngine.getNumberOfHumans());
    HelperMethods.replaceScene(
        HelperMethods.playerSelectionWindowFXML,
        HelperMethods.playerSelectionWindowTitle,
        mouseEvent,
        this
    );
  }
}
