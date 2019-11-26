package com.example.dogedice;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHelper {
  private static Logger logger = LoggerFactory.getLogger(TestHelper.class);

  public static void assertButtonStyle(ApplicationTest origin ,Button button) {
    // Neutral color: #FFCB7A
    // Hover color:   #2196F3
    // Pressed color: #FFCB7A
  }

  public static void assertDogeSpin(ApplicationTest origin, Scene scene, String id) {
    logger.info("assertDogeSpin for {}", id);
    assertImageViewUrlContains(scene, id, "dogespin.gif");
    assertEquals(1.0,scene.lookup(id).getScaleX());
    origin.clickOn(id);
    assertEquals(-1.0,scene.lookup(id).getScaleX());
    origin.clickOn(id);
    assertEquals(1.0,scene.lookup(id).getScaleX());
    origin.rightClickOn(id);
    assertImageViewUrlContains(scene, id, "rainbowDoge.gif");
    origin.rightClickOn(id);
    assertImageViewUrlContains(scene, id, "dogespin.gif");
  }

  public static void assertImageViewUrlContains(Scene scene, String id, String shouldContain) {
    assertTrue(((ImageView) scene.lookup(id)).getImage().getUrl().contains(shouldContain));

  }
}
