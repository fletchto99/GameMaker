package me.matt.gamemaker;

public class GameSettings {

    private final Main m;

    public GameSettings(final Main m) {
        this.m = m;
    }

    public void setSize(final int width, final int height) {
        m.setSize(width, height);
    }

    public void setResizable(final boolean resizable) {
        m.setResizable(resizable);
    }

    public void setFullscreenAllowed(final boolean allowed) {
        m.getToolBar().setFullscreenAllowed(allowed);
    }

    public void setInformation(final String information) {
        m.getToolBar().setGameInformation(information);
    }

    public void setTickTime(int time) {
        m.setTickTime(time);
    }

}
