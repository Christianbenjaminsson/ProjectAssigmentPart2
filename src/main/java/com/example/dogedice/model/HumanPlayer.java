package com.example.dogedice.model;

public class HumanPlayer extends Player {

  public HumanPlayer(String name) {
    super(name);
  }

  @Override
  public boolean isBot() {
    return false;
  }
}
