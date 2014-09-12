package me.matt.gamemaker.game.loader;

import java.util.List;

import me.matt.gamemaker.game.Game;

public interface GameSource {

	List<GameDefinition> list();

	Game load(GameDefinition def) throws InstantiationException,
	IllegalAccessException;

}
