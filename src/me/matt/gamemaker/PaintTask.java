package me.matt.gamemaker;

import me.matt.gamemaker.gui.Paint;

public class PaintTask extends Thread implements Runnable {

    private final Paint p;

    private int time = 6;

    private final Object lock = new Object();

    public PaintTask(final Paint p) {
        this.p = p;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            p.repaint();
            synchronized (lock) {
                try {
                    Thread.sleep(time);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }
    }

    public synchronized void setTickTime(final int time) {
        if (time > 0) {
            synchronized (lock) {
                this.time = time;
            }
        }
    }
}
