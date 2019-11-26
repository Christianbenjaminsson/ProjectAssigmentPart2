package com.example.dogedice.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
  private Logger logger = LoggerFactory.getLogger(DieTest.class);

  private void rollArbitraryDie(int sides) {
    logger.info(String.format("Testing with %s-sided die", sides));
    Die die = new Die(sides);
    Set<Integer> rolls = new HashSet<>();
    for (int i = 0; i <= sides * 100; i++) {
      rolls.add(die.roll());
    }
    assertTrue(rolls.contains(1));
    assertTrue(rolls.contains(sides));
    assertEquals(sides, rolls.size());
  }

  @Test
  void oneSidedDie() { rollArbitraryDie(1); }

  @Test
  void sixSidedDie() { rollArbitraryDie(6); }

  @Test
  void twelveSidedDie() { rollArbitraryDie(12); }

  @Test
  void twentySidedDie() { rollArbitraryDie(20); }
}