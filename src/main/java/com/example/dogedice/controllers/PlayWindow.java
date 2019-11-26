package com.example.dogedice.controllers;

import com.example.dogedice.model.BotAction;
import com.example.dogedice.model.Die;
import com.example.dogedice.model.Modifier;
import com.example.dogedice.model.Player;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.paint.Color.*;

public class PlayWindow extends GenericController {
  private List<Player> players;
  private final Map<Player, Label> playerNames;
  private final Map<Player, Label> playerScores;
  private final Map<Player, FlowPane> playerItems;
  private Group d6Button;
  private Group d20Button;
  private Group modifierButton;
  private Label modifierButtonText;

  @FXML
  VBox playerPaneBox, diceBox;

  @FXML
  Label roll, gameTurns;

  @FXML
  Button rollButton;

  public PlayWindow() {
    playerNames = new HashMap<>();  // the labels where we display player names
    playerScores = new HashMap<>(); // the labels where we display player scores
    playerItems = new HashMap<>();  // the flowpane where we display player dice/modifiers
  }

  public void spinningDogeClicked(MouseEvent mouseEvent) {
    HelperMethods.spinningDogeClicked(mouseEvent);
  }

  public void backButtonClicked(MouseEvent mouseEvent) throws IOException {
    gameEngine.resetPlayers();
    gameEngine.resetRounds();
    HelperMethods.replaceScene(
        HelperMethods.namePlayersWindowFXML,
        HelperMethods.namePlayersWindowTitle,
        mouseEvent,
        this
    );
  }

  public void rollButtonClicked(MouseEvent mouseEvent) {
    Player player = gameEngine.getPlayer();
    BotAction botAction = gameEngine.getBotAction();

    while (botAction != BotAction.PASS) {
      switch (botAction) {
        case BUYD6: buyD6(mouseEvent); break;
        case BUYD20: buyD20(mouseEvent); break;
        case BUYMODIFIER: buyModifier(mouseEvent); break;
      }
      botAction = gameEngine.getBotAction();
    }
    roll.setText("" + gameEngine.rollDice());
    playerScores.get(player).setText(gameEngine.getScoreAsString());

    gameEngine.incrementPlayer();
    applyButtonEffects();

    Player nextPlayer = gameEngine.getPlayer();

    setInactiveStylePlayer(player);
    setActiveStylePlayer(nextPlayer);
    if (nextPlayer.isBot()) {
      rollButtonClicked(mouseEvent);
    }

    gameTurns.setText("Rounds Left: " + gameEngine.getRoundsLeftAsString());
    if (gameEngine.getRoundsLeft() == 0) {
      gameEngine.updateHighScore();
       rollButton.setOnMousePressed(event -> {
         try {
           HelperMethods.replaceScene(
               HelperMethods.winnerWindowFXML,
               HelperMethods.winnerWindowTitle,
               mouseEvent,
               this);
         } catch (IOException e) {
           e.printStackTrace();
         }
       });
      rollButton.setText("Show Winner!");
    }
  }

  private void applyButtonEffects() {
    Effect active = null;
    Effect inactive = new MotionBlur();

    if (gameEngine.canBuyD6()) {
      d6Button.setEffect(active);
    } else {
      d6Button.setEffect(inactive);
    }

    if (gameEngine.canBuyD20()) {
      d20Button.setEffect(active);
    } else {
      d20Button.setEffect(inactive);
    }

    if (gameEngine.canBuyModifier()) {
      modifierButton.setEffect(active);
      modifierButtonText.setEffect(active);
    } else {
      modifierButton.setEffect(inactive);
      modifierButtonText.setEffect(inactive);
    }
  }

  @Override
  public void postInitialization(){
    gameTurns.setText("Rounds Left: " + gameEngine.getRoundsLeftAsString());

    // Creating the images to be put in the boxes.
    SVGPath d6SVG = null;
    SVGPath d20SVG = null;
    SVGPath modifierSVG = null;
    try {
      d6SVG = getSVGIcon("svgpaths/d6");
      resizeSVG(d6SVG, 60, 60 );

      d20SVG = getSVGIcon("svgpaths/d20");
      resizeSVG(d20SVG, 60, 65);

      modifierSVG = getSVGIcon("svgpaths/modifier");
      resizeSVG(modifierSVG, 60, 60);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Putting the SVGs in a box makes sure the white space left from scaling them is cropped away.
    Group d6SVGGroup = new Group(d6SVG);
    Group d20SVGGroup = new Group(d20SVG);
    Group modifierSVGGroup = new Group(modifierSVG);
    Label modifierPlusOne = new Label("+1");
    modifierPlusOne.setId("plusOneLabel");

    // Setting the buttons to class fields so we can apply effects to them later
    d6Button = d6SVGGroup;
    d20Button = d20SVGGroup;
    modifierButton = modifierSVGGroup;
    modifierButtonText = modifierPlusOne;

    // Creating labels for each of the upgrades price.
    Label d6Price = new Label(gameEngine.getD6PriceAsString());
    Label d20Price = new Label(gameEngine.getD20PriceAsString());
    Label modifierPrice = new Label(gameEngine.getModifierPriceAsString());
    modifierPrice.setTranslateY(-50);
    d6Price.getStyleClass().add("modifierLabels");
    d20Price.getStyleClass().add("modifierLabels");
    modifierPrice.getStyleClass().add("modifierLabels");

    //Adding the SVGs and prices to VBoxes
    VBox d6Box = new VBox(d6SVGGroup, d6Price);
    VBox d20Box = new VBox(d20SVGGroup, d20Price);
    VBox modifierBox = new VBox(modifierSVGGroup, modifierPlusOne, modifierPrice);
    d6Box.getStyleClass().add("itemVBoxes");
    d20Box.getStyleClass().add("itemVBoxes");
    modifierBox.getStyleClass().add("itemVBoxes");

    // Setting onMousePressed action for items
    d6Box.setOnMousePressed(this::buyD6);
    d20Box.setOnMousePressed(this::buyD20);
    modifierBox.setOnMousePressed(this::buyModifier);

    // Adding everything to the diceBox
    diceBox.autosize();
    diceBox.getChildren().addAll(d6Box, d20Box, modifierBox);



    // Adding entries for all the players
    // First a row with the player's name, then another row with their upgrades.
    this.players = gameEngine.getPlayers();
    for (Player player : players) {
      // Adding the label with the player's name.
      // The HashMap is used when setting the active player style.
      Label playerNameLabel = new Label(player.getName());
      playerNameLabel.getStyleClass().add("playerInfo");
      playerNames.put(player, playerNameLabel);

      // Adding the label with the player's score.
      // The HashMap is used when updating the score.
      Label playerScore = new Label("" + player.getScore());
      playerScore.getStyleClass().add("playerInfo");
      playerScores.put(player, playerScore);

      // Putting the name and score in a GridPane.
      GridPane playerInfo = new GridPane();
      playerInfo.setVgap(100);
      playerInfo.addColumn(0, playerNames.get(player));
      playerInfo.addColumn(1, playerScores.get(player));
      playerScores.get(player).setText("" + player.getScore());
      playerInfo.getColumnConstraints().addAll(new ColumnConstraints(330), new ColumnConstraints(140));

      // Adding the pane with the user's items.
      // The HashMap is used when adding new items to the player's list
      FlowPane playerItemPane = new FlowPane();
      playerItemPane.setHgap(5);
      playerItemPane.setPadding(new Insets(5,0,15,0));
      playerItems.put(player, playerItemPane);

      List<Node> playerItemsList = playerItemPane.getChildren();
      player.getDice().forEach(x -> {
        try {
          playerItemsList.add(getIcon(x));
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      // Adding everything to the scrollpane in the playwindow
      playerPaneBox.getChildren().addAll(playerInfo, playerItemPane);
    }
    setActiveStylePlayer(this.players.get(0));
    applyButtonEffects();
  }

  private void buyD6(MouseEvent mouseEvent) {
    Die newDie = gameEngine.buyD6();
    if(newDie != null){
      try {
        playerItems.get(gameEngine.getPlayer()).getChildren().add(getIcon(newDie));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    playerScores.get(gameEngine.getPlayer()).setText(gameEngine.getScoreAsString());
    applyButtonEffects();
  }

  private void buyD20(MouseEvent mouseEvent) {
    Die newDie = gameEngine.buyD20();
    if(newDie != null){
      try {
        playerItems.get(gameEngine.getPlayer()).getChildren().add(getIcon(newDie));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    playerScores.get(gameEngine.getPlayer()).setText(gameEngine.getScoreAsString());
    applyButtonEffects();
  }

  private void buyModifier(MouseEvent mouseEvent) {
    Modifier newMod = gameEngine.buyModifier();
    if(newMod != null){
      try {
        playerItems.get(gameEngine.getPlayer()).getChildren().add(getIcon(newMod));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    playerScores.get(gameEngine.getPlayer()).setText(gameEngine.getScoreAsString());
    applyButtonEffects();
  }

  private void setActiveStylePlayer(Player player) {
    playerNames.get(player).setStyle("-fx-text-fill: #2196F3;");
  }

  private void setInactiveStylePlayer(Player player) {
    playerNames.get(player).setStyle("");
  }

  private Group getIcon(Die die) throws IOException {
    SVGPath icon = null;
    if (die.getNumOfSides() == 6) {
      icon = getSVGIcon("svgpaths/d6");
      icon.setFill(DARKBLUE);
      resizeSVG(icon, 35, 35);
    } else if (die.getNumOfSides() == 20) {
      icon = getSVGIcon("svgpaths/d20");
      icon.setFill(DARKRED);
      resizeSVG(icon, 35, 35);
    }
    Group group = new Group();
    group.getStylesheets().add("css/playWindow.css");
    group.getChildren().addAll(icon); // will return null pointer exception for weird dice
    return group;
  }

  // TODO modifier icon is currently all black. Should display value as well.
  private StackPane getIcon(Modifier mod) throws IOException {
    SVGPath icon = getSVGIcon("svgpaths/modifier");
    icon.setFill(PURPLE);
    resizeSVG(icon, 35, 35);
    Group group = new Group();
    group.getChildren().add(icon);

    Label plusN = new Label(String.format("+%s", mod.getValue()));
    plusN.setId("smallPlusOneLabel");
    StackPane newIcon = new StackPane(group, plusN);
    newIcon.setId("smallModifierBox");
    return newIcon;
  }

  private SVGPath getSVGIcon(String filePath) throws IOException {
    SVGPath icon = new SVGPath();
    String path = IOUtils.toString(
        HelperMethods.getResAsStream(filePath),
        StandardCharsets.UTF_8
    );
    icon.setContent(path);
    return icon;
  }

  private void resizeSVG(SVGPath svg, double width, double height) {
    double originalHeight = svg.prefHeight(-1);
    double originalWidth = svg.prefWidth(originalHeight);
    svg.setScaleX(width / originalWidth);
    svg.setScaleY(height / originalHeight);
  }
}

