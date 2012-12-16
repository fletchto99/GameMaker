package me.matt.gamemaker.game;

import java.io.File;
import java.util.ArrayList;

import me.matt.gamemaker.Main;
import me.matt.gamemaker.game.loader.GameDefinition;
import me.matt.gamemaker.game.loader.GameLoader;

public class GameHandler {

    private final ArrayList<Game> games = new ArrayList<Game>();
    private final ArrayList<GameDefinition> defs = new ArrayList<GameDefinition>();

    public GameHandler() {
        init();
    }

    private void init() {
        defs.addAll(new GameLoader(GameHandler.getPluginsFolder()).list());
        for (final GameDefinition def : defs) {
            try {
                System.out.println("Loading: " + def.name);
                games.add(def.source.load(def));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static File getPluginsFolder() {
        final File f = new File(new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getParent().replace(
                "%20", " ")
                + File.separator + "games");
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;

    }

    public void stopGames() {
        games.clear();
        defs.clear();
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void reload() {
        stopGames();
        System.gc();
        init();
    }

}
