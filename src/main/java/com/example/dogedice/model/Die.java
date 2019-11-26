package com.example.dogedice.model;

public class Die {

  private int numOfSides;

  public Die(int numOfSides) {
    this.numOfSides = numOfSides;
  }

  public int roll() {
    return (int) (1 + Math.random() * numOfSides);
  }

  public int getNumOfSides() {
    return numOfSides;
  }
}
