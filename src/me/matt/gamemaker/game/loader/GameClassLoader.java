package me.matt.gamemaker.game.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GameClassLoader extends ClassLoader {

    private final URL base;

    public GameClassLoader(final URL url) {
        base = url;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
                return clazz;
            } catch (final ClassNotFoundException ex) {
                clazz = null;
            }
        }
        if (clazz == null) {
            try {
                final InputStream in = getResourceAsStream(name.replace('.',
                        '/') + ".class");
                final byte[] buffer = new byte[4096];
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                int n;
                while ((n = in.read(buffer, 0, 4096)) != -1) {
                    out.write(buffer, 0, n);
                }
                final byte[] bytes = out.toByteArray();
                clazz = defineClass(name, bytes, 0, bytes.length);
            } catch (final Exception e) {
                if (clazz == null) {
                    findLoadedClass(name);
                }
                if (clazz == null) {
                    clazz = findSystemClass(name);
                }
                if (clazz == null) {
                    super.loadClass(name);
                }
            }
        }
        return clazz;
    }

    @Override
    public URL getResource(final String name) {
        try {
            return new URL(base, name);
        } catch (final MalformedURLException e) {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        try {
            return new URL(base, name).openStream();
        } catch (final IOException e) {
            return null;
        }
    }

}