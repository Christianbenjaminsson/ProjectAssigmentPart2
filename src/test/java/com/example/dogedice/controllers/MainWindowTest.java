package com.example.dogedice.controllers;

import com.example.dogedice.TestHelper;
import com.example.dogedice.model.GameEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class MainWindowTest extends ApplicationTest {
  private Scene scene;
  private Stage stage;
  private Logger logger = LoggerFactory.getLogger(MainWindowTest.class);

  @Override
  public void start(Stage stage) throws IOException {
    this.stage = stage;
    GameEngine gameEngine = new GameEngine(30, 5, 10, 15, new int[]{6,6}, new int[]{});
    FXMLLoader loader = HelperMethods.getLoader(HelperMethods.mainWindowFXML);
    Parent root = loader.load();
    this.scene = new Scene(root);
    GenericController controller = loader.getController();
    controller.setGameEngine(gameEngine);

    stage.setTitle(HelperMethods.mainWindowTitle);
    stage.setScene(this.scene);
    stage.show();
    stage.toFront();
  }

  @Test
  void checkButtons() {
    logger.info("Retrieving buttons");
    Button startButton = (Button) scene.lookup("#startButton");
    Button highscoreButton = (Button) scene.lookup("#highscoreButton");

    logger.info("Checking that button text is correct");
    assertEquals("START", startButton.getText());
    assertEquals("HIGHSCORE", highscoreButton.getText());

    logger.info("Checking that button style is correct");
    TestHelper.assertButtonStyle(this, startButton);
    TestHelper.assertButtonStyle(this, highscoreButton);
  }

  @Test
  void startButtonClicked() {
    logger.info("Clicking START button");
    clickOn("#startButton");
    logger.info("Asserting that title is {}, actual title is: {}", HelperMethods.playerSelectionWindowTitle, stage.getTitle());
    assertEquals(HelperMethods.playerSelectionWindowTitle, stage.getTitle());
    logger.info("Clicking back button to get back to main screen");
    clickOn("#backButton");
    assertEquals(HelperMethods.mainWindowTitle, stage.getTitle());
  }

  @Test
  void highscoreButtonClicked() {
    logger.info("Clicking HIGHSCORE button");
    clickOn("#highscoreButton");
    logger.info("Asserting that title is {}, actual title is: {}", HelperMethods.highscoreWindowTitle, stage.getTitle());
    assertEquals(HelperMethods.highscoreWindowTitle, stage.getTitle());
    logger.info("Clicking back button to get back to main screen");
    clickOn("#backButton");
    assertEquals(HelperMethods.mainWindowTitle, stage.getTitle());
  }

  @Test
  void helpButtonClicked() {
    logger.info("Clicking help button");
    clickOn("#helpButton");
    logger.info("Asserting that title is {}, actual title is: {}", HelperMethods.helpWindowTitle, stage.getTitle());
    assertEquals(HelperMethods.helpWindowTitle, stage.getTitle());
    logger.info("Clicking back button to get back to main screen");
    clickOn("#backButton");
    assertEquals(HelperMethods.mainWindowTitle, stage.getTitle());
  }

  @Test
  void dogecoinButtonClicked() {
    logger.info("Clicking dogecoin button");
    clickOn("#dogecoinButton");
    logger.info("Asserting that title is {}, actual title is: {}", HelperMethods.dogeCoinWindowTitle, stage.getTitle());
    assertEquals(HelperMethods.dogeCoinWindowTitle, stage.getTitle());
    logger.info("Clicking back button to get back to main screen");
    clickOn("#backButton");
    assertEquals(HelperMethods.mainWindowTitle, stage.getTitle());
  }

  @Test
  void spinningDogeClicked() {
    logger.info("Checking dogespin for #spinningDoge1");
    TestHelper.assertDogeSpin(this, scene, "#spinningDoge1");
    logger.info("Checking dogespin for #spinningDoge2");
    TestHelper.assertDogeSpin(this, scene, "#spinningDoge2");
  }
}