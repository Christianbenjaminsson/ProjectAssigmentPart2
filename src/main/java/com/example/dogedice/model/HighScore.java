package com.example.dogedice.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class HighScore {
  private final Gson gson;
  private List<HumanPlayer> players;
  private final int playersToSave;
  private final String jsonPath;


  HighScore(int playersToSave) {
    this.gson = new Gson();
    this.players = new ArrayList<>();
    this.playersToSave = playersToSave;
    this.jsonPath = "highscores.json";

    try {
      String json = IOUtils.toString(
          new URL("file:" + jsonPath),
          StandardCharsets.UTF_8
      );
      Type listType = new TypeToken<ArrayList<HumanPlayer>>() {}.getType();
      List<HumanPlayer> playersFromFile = gson.fromJson(json, listType);
      players.addAll(playersFromFile);
    } catch (Exception e) {
      System.out.println("Something went wrong when trying to load json.");
    }
    while (players.size() < playersToSave) {
      players.add(new HumanPlayer("Super Doge"));
    }
  }

  void addPlayers(List<Player> newPlayers) {
    List<HumanPlayer> newHumanPlayers = newPlayers
        .stream()
        .filter(x -> !x.isBot())
        .map(x -> (HumanPlayer) x)
        .collect(Collectors.toList());
    players.addAll(newHumanPlayers);
    Collections.sort(players);
    players = players.subList(0, playersToSave);
  }

  List<HumanPlayer> getPlayers() {
    return players;
  }

  void writeJSON() {
    String json = gson.toJson(players);
    try {
      FileWriter fw = new FileWriter(jsonPath);
      IOUtils.write(json, fw);
      fw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
