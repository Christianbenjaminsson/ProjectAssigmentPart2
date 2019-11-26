package com.example.dogedice;

/* As far as I understand it has something to do with jfx only being available
 * as a jar, and any class the Main class extends needs to be available as a module.
 * Since JFX isn't available as a module, this is our only solution.
 *
 * There are some weird solutions out there using jlink and manually adding the jmod
 * to the project, or something like that. This is clearly impractical and kind of
 * defeats the purpose of having gradle handle all of this for us. */

public class Launcher {
  public static void main(String[] args) {
    Main.main(args);
  }
}
