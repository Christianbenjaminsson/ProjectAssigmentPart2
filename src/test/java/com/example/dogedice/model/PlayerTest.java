// This class is supposed to test both HumanPlayer and CPU Player
package com.example.dogedice.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
  private Random r = new Random();
  private Logger logger = LoggerFactory.getLogger(DieTest.class);

  private String randomString() {
    String characters = "abcdefghijklmnopqrstuvwxyzåäöæœþđłçô" +
        "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖÆŒÞĐŁÇÔ" +
        "1234567890 {}[]$\"'/^|\\#:=@!%`≠";
    StringBuilder randomString = new StringBuilder();

    for (int i = 0; i < 30; i++) {
      randomString.append(characters.charAt(r.nextInt(characters.length())));
    }
    return randomString.toString();
  }

  @Test
  void setName() {
    logger.info("Creating 100 human players and 100 CPU players with random names.");
    for (int i = 0; i < 100; i++) {
      String str = randomString();
      Player human = new HumanPlayer(str);
      Player cpu = new CpuPlayer(str, null);
      assertEquals(str, human.getName());
      assertEquals(str, cpu.getName());
    }
  }

  @Test
  void rollDice() {
    logger.info("Creating a human player and a cpu player");
    Player human = new HumanPlayer("Human");
    Player cpu = new CpuPlayer("CPU", null);

    logger.info("Testing that dice are added properly.");
    for (int i = 1; i <= 100; i++) {
      Die die = new Die(1);
      human.addDie(die);
      cpu.addDie(die);

      assertEquals(i, human.getDice().size());
      assertEquals(i, cpu.getDice().size());
    }
  }

  @Test
  void removePoints() {
    logger.info("Setting up a human and a cpu player with a one-sided die.");
    Player human = new HumanPlayer("Human");
    Player cpu = new CpuPlayer("CPU", null);
    Die die = new Die(1);
    human.addDie(die);
    cpu.addDie(die);

    logger.info("Rolling the die, giving each 1 point.");
    human.rollAllDice();
    cpu.rollAllDice();

    logger.info("Asserting that removePoints(2) returns false.");
    assertFalse(human.removePoints(2));
    assertFalse(cpu.removePoints(2));

    logger.info("Asserting that no points were removed.");
    assertEquals(1, human.getScore());
    assertEquals(1, cpu.getScore());

    logger.info("Asserting that removePoints(1) returns true.");
    assertTrue(human.removePoints(1));
    assertTrue(cpu.removePoints(1));

    logger.info("Asserting that 1 point was removed.");
    assertEquals(0, human.getScore());
    assertEquals(0, cpu.getScore());

    logger.info("Rolling die three times for both players.");
    human.rollAllDice();
    human.rollAllDice();
    human.rollAllDice();
    cpu.rollAllDice();
    cpu.rollAllDice();
    cpu.rollAllDice();

    logger.info("Asserting that removePoints(2) returns true.");
    assertTrue(human.removePoints(2));
    assertTrue(cpu.removePoints(2));

    logger.info("Asserting that 2 points were removed.");
    assertEquals(1, human.getScore());
    assertEquals(1, cpu.getScore());
  }

  @Test
  void isBot() {
    logger.info("Testing that HumanPlayer isn't a bot and that CpuPlayer is a bot.");
    assertFalse(new HumanPlayer("").isBot());
    assertTrue(new CpuPlayer("", null).isBot());
  }

  @Test
  void getPoints() {
    logger.info("Testing that points are added and summed up correctly.");
    Player human = new HumanPlayer("");
    Player cpu = new CpuPlayer("", null);
    for (int i = 0; i < 10; i++) {
      Die die = new Die(6);
      human.addDie(die);
      cpu.addDie(die);
    }

    int humanScore = 0;
    int cpuScore = 0;
    int minimumRoll = 10; // All ones
    int maximumRoll = 60; // All sixes
    for (int j = 1; j <= 10; j++) {
      int humanRoll = human.rollAllDice();
      int cpuRoll= cpu.rollAllDice();
      humanScore += humanRoll;
      cpuScore += cpuRoll;

      assertTrue(humanRoll >= minimumRoll && humanRoll <= maximumRoll);
      assertTrue(cpuRoll >= minimumRoll && cpuRoll <= maximumRoll);

      assertEquals(humanScore, human.getScore());
      assertEquals(cpuScore, cpu.getScore());
    }
  }

  @Test
  void addModifiers() {
    logger.info("Creating human and cpu player.");
    Player human = new HumanPlayer("");
    Player cpu = new CpuPlayer("", null);

    logger.info("Adding a single die to each player.");
    Die exampleDie = new Die(6);
    human.addDie(exampleDie);
    cpu.addDie(exampleDie);

    int modifierSum = 0;
    int totalScore = 0;
    logger.info("Adding modifiers +1, +2 .. +10 to each of the players while doing some checks.");
    for (int i = 1; i <= 10; i++) {
      modifierSum += i;
      totalScore += modifierSum;
      Modifier mod = new Modifier(i);
      logger.info("Adding modifier with value {} to human and cpu", i);
      human.addModifier(mod);
      cpu.addModifier(mod);

      logger.info("Asserting that the modifier sums are {}", modifierSum);
      assertEquals(modifierSum, human.sumAllModifiers());
      assertEquals(modifierSum, cpu.sumAllModifiers());
      logger.info("Asserting that the players have {} modifiers each", i);
      assertEquals(i, human.getModifiers().size());
      assertEquals(i, cpu.getModifiers().size());
      logger.info("Asserting that both players now have {} points", totalScore);
      assertEquals(totalScore, human.getScore());
      assertEquals(totalScore, human.getScore());
    }
  }
}