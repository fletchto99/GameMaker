package me.matt.gamemaker.game.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.matt.gamemaker.game.Game;

public class GameLoader implements GameSource {
    public static URL getJarUrl(final File file) throws IOException {
        URL url = file.toURI().toURL();
        url = new URL("jar:" + url.toExternalForm() + "!/");
        return url;
    }

    private static boolean isJar(final File file) {
        return file.getName().endsWith(".jar");
    }

    public static void load(final File file,
            final LinkedList<GameDefinition> defs) throws IOException {
        if (GameLoader.isJar(file)) {
            GameLoader.load(new GameClassLoader(GameLoader.getJarUrl(file)),
                    defs, new JarFile(file));
        } else {
            GameLoader.load(new GameClassLoader(file.getParentFile().toURI()
                    .toURL()), defs, file, "");
        }
    }

    private static void load(final GameClassLoader loader,
            final LinkedList<GameDefinition> games, final File file,
            final String prefix) {
        if (file.isDirectory()) {
            if (!file.getName().startsWith(".")) {
                for (final File f : file.listFiles()) {
                    GameLoader.load(loader, games, f, prefix + file.getName()
                            + ".");
                }
            }
        } else {
            String name = prefix + file.getName();
            final String ext = ".class";
            if (name.endsWith(ext) && !name.startsWith(".")
                    && !name.contains("!") && !name.contains("$")) {
                name = name.substring(0, name.length() - ext.length());
                GameLoader.load(loader, games, name, file.getAbsolutePath());
            }
        }
    }

    private static void load(final GameClassLoader loader,
            final LinkedList<GameDefinition> plugins, final JarFile jar) {
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry e = entries.nextElement();
            final String name = e.getName().replace('/', '.');
            final String ext = ".class";
            if (name.endsWith(ext) && !name.contains("$")) {
                GameLoader.load(loader, plugins,
                        name.substring(0, name.length() - ext.length()),
                        jar.getName());
            }
        }
    }

    private static void load(final GameClassLoader loader,
            final LinkedList<GameDefinition> games, final String name,
            final String path) {
        Class<?> clazz;
        try {
            clazz = loader.loadClass(name);
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        } catch (final VerifyError e) {
            e.printStackTrace();
            return;
        }
        final GameDefinition def = new GameDefinition();
        def.clazz = clazz;
        def.name = name;
        if (def.clazz.getSuperclass() == Game.class) {
            games.add(def);
        }
    }

    private final File[] files;

    public GameLoader(final File... file) {
        files = file;
    }

    @Override
    public LinkedList<GameDefinition> list() {
        final LinkedList<GameDefinition> defs = new LinkedList<GameDefinition>();
        for (final File file : files) {
            this.list(file, defs);
        }
        return defs;
    }

    private void list(final File file, final LinkedList<GameDefinition> defs) {
        if (file != null) {
            if (file.isDirectory()) {
                try {
                    for (final File item : file.listFiles()) {
                        GameLoader.load(item, defs);
                    }
                } catch (final IOException ignored) {
                }
            } else if (GameLoader.isJar(file)) {
                try {
                    GameLoader.load(
                            new GameClassLoader(GameLoader.getJarUrl(file)),
                            defs, new JarFile(file));
                } catch (final IOException ignored) {
                }
            }
        }
        for (final GameDefinition def : defs) {
            def.source = this;
        }
    }

    @Override
    public Game load(final GameDefinition def) throws InstantiationException,
            IllegalAccessException {
        return def.clazz.asSubclass(Game.class).newInstance();
    }

}