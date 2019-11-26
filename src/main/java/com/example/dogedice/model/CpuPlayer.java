package com.example.dogedice.model;

import java.util.Random;

public class CpuPlayer extends Player {
  private final GameEngine gameEngine;
  private final int stupidity;
  private final Random r;

  public CpuPlayer(String name, GameEngine gameEngine, int stupidity) {
    super(name);
    this.gameEngine = gameEngine;
    this.stupidity = stupidity;
    this.r = new Random();
  }

  @Override
  public boolean isBot() {
    return true;
  }

  public BotAction getDesiredAction() {
    int roundsLeft = gameEngine.getRoundsLeft() - 1;
    int numDice = numD6 + numD20;
    double pointsPerTurn = (3.5 * numD6) + (10.5 * numD20) + (numModifiers * numDice);

    // Chance of the bot taking a random action
    // The bot is very good at this game, having it take a random action is a sort of handicap.
    if (r.nextInt(101) < stupidity) {
      float chance = r.nextFloat();
      if (chance < 0.2) {
        return BotAction.BUYMODIFIER;
      } else if (chance < 0.5) {
        return BotAction.BUYD20;
      } else if (chance < 0.9) {
        return BotAction.BUYD6;
      } else {
        return BotAction.PASS;
      }
    }

    // How many rounds until we can purchase any given item
    long turnsUntilD6 = getTurnsUntil(gameEngine.getD6Price(), pointsPerTurn);
    long turnsUntilD20 = getTurnsUntil(gameEngine.getD6Price(), pointsPerTurn);
    long turnsUntilModifier = getTurnsUntil(gameEngine.getModifierPrice(), pointsPerTurn);

    // Expected earnings from any given purchase.
    double d6Gain = (3.5 + numModifiers) * (roundsLeft - turnsUntilD6);
    double d20Gain = (10.5 + numModifiers) * (roundsLeft - turnsUntilD20) / gameEngine.getD20Price();
    double modifierGain = numDice * (roundsLeft - turnsUntilModifier) / gameEngine.getModifierPrice();

    double d6GainPerTurn = d6Gain / gameEngine.getD6Price();
    double d20GainPerTurn = d20Gain / gameEngine.getD6Price();
    double modifierGainPerTurn = modifierGain / gameEngine.getD6Price();

    double totalD6Gain = d6GainPerTurn * (roundsLeft - turnsUntilD6) - gameEngine.getD6Price();
    double totalD20Gain = d20GainPerTurn * (roundsLeft - turnsUntilD20) - gameEngine.getD20Price();
    double totalModifierGain = modifierGainPerTurn * (roundsLeft - turnsUntilModifier) - gameEngine.getModifierPrice();

    if (totalD6Gain > totalD20Gain && totalD6Gain > totalModifierGain && totalD6Gain > 0) {
      return gameEngine.canBuyD6() ? BotAction.BUYD6 : BotAction.PASS;
    } else if (totalD20Gain > totalModifierGain && totalModifierGain > 0) {
      return gameEngine.canBuyD20() ? BotAction.BUYD20 : BotAction.PASS;
    } else if (totalModifierGain > 0) {
      return gameEngine.canBuyModifier() ? BotAction.BUYMODIFIER : BotAction.PASS;
    } else {
      return BotAction.PASS;
    }
  }

  private long getTurnsUntil(int price, double pointsPerTurn) {
    double exactTurns = (getScore() - price) / pointsPerTurn;
    if (exactTurns < 0) {
      return Math.round(Math.ceil(exactTurns));
    } else {
      return 0;
    }
  }

  private long numberOfNSidedDice(int n) {
    return getDice()
        .stream()
        .filter(x -> x.getNumOfSides() == n)
        .count();
  }
}
