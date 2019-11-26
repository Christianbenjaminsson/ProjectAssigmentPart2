package com.example.dogedice.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
  private Logger logger = LoggerFactory.getLogger(GameEngineTest.class);

  private List<Integer> playerDiceSides(GameEngine gameEngine) {
    return gameEngine
        .getPlayer()
        .getDice()
        .stream()
        .map(Die::getNumOfSides)
        .collect(Collectors.toList());
  }

  @Test
  void basicFunctionality() {
    logger.info("Creating new gameEngine.");
    int[] startingDice = new int[]{6, 6, 20, 20};
    int[] startingModifiers = new int[]{1, 2, 3, 4};
    GameEngine gameEngine = new GameEngine(30, 5, 10, 15, startingDice, startingModifiers);

    logger.info("Creating one human and one cpu player.");
    gameEngine.addCpuPlayer("Human");
    gameEngine.addHumanPlayer("CPU");

    logger.info("Retrieving players and asserting that we have the correct number of players.");
    List<Player> players = gameEngine.getPlayers();
    assertEquals(2, players.size());

    logger.info("Asserting that players have the right number of dice.");
    for (Player player : players) {
      assertEquals(4, player.getDice().size());
    }

    logger.info("Asserting that players have the right number of modifiers.");
    for (Player player : players) {
      assertEquals(4, player.getModifiers().size());
    }

    logger.info("Asserting that players have the right kind of dice.");
    for (Player player : players) {
      List<Die> dice = player.getDice();
      for (int i = 0; i < 4; i++) {
        assertEquals(startingDice[i], dice.get(i).getNumOfSides());
      }
    }

    logger.info("Asserting that players have the right kind of modifiers.");
    for (Player player : players) {
      List<Modifier> modifiers = player.getModifiers();
      for (int i = 0; i < 4; i++) {
        assertEquals(startingModifiers[i], modifiers.get(i).getValue());
      }
    }

    logger.info("Asserting that prices are set correctly.");
    assertEquals(5, gameEngine.getD6Price());
    assertEquals(10, gameEngine.getD20Price());
    assertEquals(15, gameEngine.getModifierPrice());

    logger.info("Asserting that rounds left is set correctly");
    assertEquals(30, gameEngine.getRoundsLeft());
    assertEquals("30", gameEngine.getRoundsLeftAsString());

    logger.info("Asserting that players start with 0 points and incrementing players works correctly.");
    for (int i = 0; i < 2; i++) {
      logger.info("Asserting that player {} is {}", i + 1, players.get(i).getName());
      assertEquals(players.get(i), gameEngine.getPlayer());
      logger.info("Asserting that {}'s score is 0", players.get(i).getName());
      assertEquals("0", gameEngine.getScoreAsString());
      assertEquals(0, gameEngine.getScore());
      logger.info("Asserting that playerIndex is correctly set to {}.", i);
      assertEquals(i, gameEngine.getPlayerIndex());
      gameEngine.incrementPlayer();
    }

    logger.info("Asserting that rounds left is decrementing correctly, 1 round should already have passed.");
    for (int i = 29; i >= 0; i--) {
      logger.info("Asserting that there are {} rounds left", i);
      assertEquals(i, gameEngine.getRoundsLeft());
      assertEquals("" + i, gameEngine.getRoundsLeftAsString());
      gameEngine.incrementPlayer();
      gameEngine.incrementPlayer();
    }
  }

  @Test
  void buyingAndRollingFunctionality() {
    logger.info("Creating new gameEngine with four one-sided dice and four value 1 modifiers.");
    int[] startingDice = new int[]{1, 1, 1, 1};
    int[] startingModifiers = new int[]{1, 1, 1, 1};
    GameEngine gameEngine = new GameEngine(30, 5, 10, 15, startingDice, startingModifiers);

    logger.info("Creating one CPU and one Human player");
    gameEngine.addHumanPlayer("Human");
    gameEngine.addCpuPlayer("CPU");

    logger.info("Asserting that players have 4 points after rolling dice");
    assertEquals(4, gameEngine.rollDice());
    assertEquals("4", gameEngine.getScoreAsString());
    assertEquals(4, gameEngine.getScore());
    gameEngine.incrementPlayer();
    assertEquals(4, gameEngine.rollDice());
    assertEquals("4", gameEngine.getScoreAsString());
    assertEquals(4, gameEngine.getScore());
    gameEngine.incrementPlayer();

    logger.info("With prices 5/10/15 for d6/d20/modifier, asserting that canBuy-methods return false.");
    for (int i = 0; i < 2; i++) {
      assertFalse(gameEngine.canBuyD6());
      assertFalse(gameEngine.canBuyD20());
      assertFalse(gameEngine.canBuyModifier());
      gameEngine.incrementPlayer();
    }

    logger.info("Asserting that buy-methods don't do anything.");
    for (int i = 0; i < 2; i++) {
      logger.info("Asserting buy methods for {}.", gameEngine.getPlayerName());
      gameEngine.buyD6();
      assertEquals(4, gameEngine.getPlayer().getDice().size());
      assertEquals("4", gameEngine.getScoreAsString());
      assertEquals(4, gameEngine.getScore());
      gameEngine.buyD20();
      assertEquals(4, gameEngine.getPlayer().getDice().size());
      assertEquals("4", gameEngine.getScoreAsString());
      assertEquals(4, gameEngine.getScore());
      gameEngine.buyModifier();
      assertEquals(4, gameEngine.getPlayer().getModifiers().size());
      assertEquals("4", gameEngine.getScoreAsString());
      assertEquals(4, gameEngine.getScore());
      gameEngine.incrementPlayer();
    }

    for (int i = 0; i < 2; i++) {
      logger.info("Asserting that sumModifiers give {} 16 points.", gameEngine.getPlayerName());
      assertEquals(16, gameEngine.sumModifiers());
      logger.info("Asserting that {}'s current score is 20.", gameEngine.getPlayerName());
      assertEquals("20", gameEngine.getScoreAsString());
      assertEquals(20, gameEngine.getScore());
      gameEngine.incrementPlayer();
    }

    for (int i = 0; i < 2; i++) {
      logger.info("Asserting that {} is now able to buy anything.", gameEngine.getPlayerName());
      assertTrue(gameEngine.canBuyD6());
      assertTrue(gameEngine.canBuyD20());
      assertTrue(gameEngine.canBuyModifier());

      logger.info("Asserting that buying a D6 works for {}.", gameEngine.getPlayerName());
      logger.info("d6Price is {} and {} has {} points",
          gameEngine.getD6Price(),
          gameEngine.getPlayerName(),
          gameEngine.getScoreAsString()
      );
      gameEngine.buyD6();
      assertEquals("15", gameEngine.getScoreAsString());
      assertEquals(15, gameEngine.getScore());
      logger.info("Asserting that {} now has a d6", gameEngine.getPlayerName());
      assertTrue(playerDiceSides(gameEngine).contains(6));

      logger.info("Asserting that buying a modifier works for {}", gameEngine.getPlayerName());
      logger.info("modifierPrice is {} and {} has {} points",
          gameEngine.getModifierPrice(),
          gameEngine.getPlayerName(),
          gameEngine.getScoreAsString()
      );
      gameEngine.buyModifier();
      assertEquals("0", gameEngine.getScoreAsString());
      assertEquals(0, gameEngine.getScore());
      logger.info("Asserting that {} now has another value 1 modifier", gameEngine.getPlayerName());
      assertEquals(5, gameEngine.getPlayer().getModifiers().size());


      logger.info("Rolling dice and summing modifiers to get {} above 20 points again.", gameEngine.getPlayerName());
      gameEngine.rollDice();
      gameEngine.sumModifiers();
      logger.info("Buying {} a 20-sided die for 15 points.", gameEngine.getPlayerName());
      gameEngine.buyD20();
      logger.info("Asserting that {} has a d20 and a d6 now.", gameEngine.getPlayerName());
      assertTrue(playerDiceSides(gameEngine).contains(20));

      logger.info("Asserting that {} has a total of 6 dice now.", gameEngine.getPlayerName());
      assertEquals(6, gameEngine.getPlayer().getDice().size());

      gameEngine.incrementPlayer();
    }

    logger.info("Testing the gameEngine.resetPlayers() method.");
    gameEngine.resetPlayers();
    logger.info("gameEngine.getPlayers().size() should be 0, is {}.", gameEngine.getPlayers().size());
    assertEquals(0, gameEngine.getPlayers().size());

    logger.info("Testing the gameEngine.reset");
    gameEngine.resetRounds();
    assertEquals(30, gameEngine.getRoundsLeft());
    assertEquals("30", gameEngine.getRoundsLeftAsString());
  }
}
