package me.matt.gamemaker;

import me.matt.gamemaker.gui.Paint;

public class PaintTask extends Thread implements Runnable {

    private final Paint p;

    private int time = 6;

    private Object lock = new Object();

    public PaintTask(final Paint p) {
        this.p = p;
    }

    public synchronized void setTickTime(int time) {
        if (time > 0) {
            synchronized (lock) {
                this.time = time;
            }
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
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
}
