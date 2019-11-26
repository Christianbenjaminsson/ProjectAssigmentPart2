package com.example.dogedice.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Comparable<Player> {
  private String name;
  private int score;
  private List<Die> dice;
  private List<Modifier> modifiers;
  Integer numD6;
  Integer numD20;
  Integer numModifiers;

  Player(String name) {
    this.name = name;
    this.score = 0;
    this.dice = new ArrayList<>();
    this.modifiers = new ArrayList<>();
    this.numD6 = 0;
    this.numD20 = 0;
    this.numModifiers = 0;
  }

  public abstract boolean isBot();

  public int getScore() {
    return this.score;
  }

  public String getName() {
    return this.name;
  }

  public List<Die> getDice() {
    return this.dice;
  }

  public List<Modifier> getModifiers() {
    return this.modifiers;
  }

  public void addDie(Die die) {
    if (die.getNumOfSides() == 6) {
      numD6++;
    } else {
      numD20++;
    }
    this.dice.add(die);
  }

  public void addModifier(Modifier modifier) {
    numModifiers++;
    this.modifiers.add(modifier);
  }

  public boolean removePoints(int toRemove) {
    if (this.score >= toRemove) {
      this.score -= toRemove;
      return true;
    } else {
      return false;
    }
  }

  public int rollAllDice() {
    int sum = 0;
    for (Die die : this.dice) {
      int outcome = die.roll();
      sum += outcome;
    }
    score += sum;
    return sum;
  }

  public int sumAllModifiers() {
    int sum = 0;
    for (Modifier modifier : this.modifiers) {
      sum += modifier.getValue();
    }
    sum *= this.dice.size();
    this.score += sum;
    return sum;
  }

  public int compareTo(Player comparePlayer){
    int compareScore = comparePlayer.getScore() - score;
    if (compareScore != 0) {
      return compareScore;
    } else {
      int items = dice.size() + modifiers.size();
      int otherItems = comparePlayer.getDice().size() + comparePlayer.getModifiers().size();
      return otherItems - items;
    }
  }

  @Override
  public String toString() {
    return String.format("[<%s> %s (%sp)]",
        this.getClass().getSimpleName(),
        name,
        score
    );
  }

  public void resetScore() {
    this.score = 0;
    this.dice = new ArrayList<>();
  }
}
