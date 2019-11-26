package com.example.dogedice.controllers;

import com.example.dogedice.model.GameEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the main flow of the application, starting at the main screen
 * and moving through the entire program. Also tests that the spinners
 * in the PlayerSelectionWindow works properly.
 */
@ExtendWith(ApplicationExtension.class)
class FlowTest extends ApplicationTest {
  private Stage stage;
  private Logger logger = LoggerFactory.getLogger(FlowTest.class);
  private final String incremetHuman = "#humPlayersSpinner .increment-arrow-button";
  private final String decremetHuman = "#humPlayersSpinner .decrement-arrow-button";
  private final String incremetCPU = "#cpuPlayersSpinner .increment-arrow-button";
  private final String decremetCPU = "#cpuPlayersSpinner .decrement-arrow-button";

  @Override
  public void start(Stage stage) throws IOException {
    this.stage = stage;
    GameEngine gameEngine = new GameEngine(30, 5, 10, 15, new int[]{6,6}, new int[]{});
    FXMLLoader loader = HelperMethods.getLoader(HelperMethods.mainWindowFXML);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    GenericController controller = loader.getController();
    controller.setGameEngine(gameEngine);

    stage.setTitle(HelperMethods.mainWindowTitle);
    stage.setScene(scene);
    stage.show();
    stage.toFront();
  }

  /**
   * Tests that spinners in PlayerSelectionScreen works like they're supposed to.
   * TODO Add test for typing into the spinner text field.
   */
  @Test
  void setSpinnerValues() {
    logger.info("Clicking start button in main window");
    clickOn("#startButton");
    Scene scene = stage.getScene();

    logger.info("Locating #humPlayersSpinner");
    Spinner<Integer> humPlayerSpinner = (Spinner<Integer>) scene.lookup("#humPlayersSpinner");
    logger.info("Locating #humPlayersSpinner");
    Spinner<Integer> cpuPlayerSpinner = (Spinner<Integer>) scene.lookup("#cpuPlayersSpinner");

    logger.info("Asserting correct start values on spinners");
    assertEquals(1, (int) humPlayerSpinner.getValue());
    assertEquals(0, (int) cpuPlayerSpinner.getValue());

    logger.info("Asserting that values can not decrease from minimum values");
    clickOn(decremetHuman);
    clickOn(decremetCPU);
    assertEquals(1, (int) humPlayerSpinner.getValue());
    assertEquals(0, (int) cpuPlayerSpinner.getValue());

    logger.info("Asserting that we can increment and decrement humPlayerSpinner once.");
    clickOn(incremetHuman);
    assertEquals(2, (int) humPlayerSpinner.getValue());
    clickOn(decremetHuman);
    assertEquals(1, (int) humPlayerSpinner.getValue());

    logger.info("Asserting that humPlayerSpinner can be incremented to 10");
    for (int i = 2; i <= 10; i++) {
      logger.info(" - Asserting that humPlayerSpinner can be incremented to {}", i);
      clickOn(incremetHuman);
      assertEquals(i, (int) humPlayerSpinner.getValue());
    }

    logger.info("Asserting that humPlayerSpinner can be decremented to 1");
    for (int i = 9; i >= 1; i--) {
      logger.info(" - Asserting that humPlayerSpinner can be decremented to {}", i);
      clickOn(decremetHuman);
      assertEquals(i, (int) humPlayerSpinner.getValue());
    }

    logger.info("Asserting that we can increment and decrement cpuPlayerSpinner once.");
    clickOn(incremetCPU);
    assertEquals(1, (int) cpuPlayerSpinner.getValue());
    clickOn(decremetCPU);
    assertEquals(0, (int) cpuPlayerSpinner.getValue());

    logger.info("Asserting that cpuPlayerSpinner can be incremented to 10");
    for (int i = 1; i <= 10; i++) {
      logger.info(" - Asserting that cpuPlayerSpinner can be incremented to {}", i);
      clickOn(incremetCPU);
      assertEquals(i, (int) cpuPlayerSpinner.getValue());
    }

    logger.info("Asserting that cpuPlayerSpinner can be decremented to 0");
    for (int i = 9; i >= 0; i--) {
      logger.info(" - Asserting that cpuPlayerSpinner can be decremented to {}", i);
      clickOn(decremetCPU);
      assertEquals(i, (int) cpuPlayerSpinner.getValue());
    }
  }
}