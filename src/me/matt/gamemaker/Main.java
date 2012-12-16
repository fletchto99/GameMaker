package me.matt.gamemaker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import me.matt.gamemaker.game.Game;
import me.matt.gamemaker.game.GameHandler;
import me.matt.gamemaker.gui.Paint;
import me.matt.gamemaker.gui.ToolBar;

public class Main extends JFrame {

    private static final long serialVersionUID = -4815895420008072546L;

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (final Exception e) {
                }
                new Main();
            }
        });
    }

    private final PaintTask pt;

    private final GameHandler gh;

    private final GameSettings settings;

    private final Paint panel;

    private final ToolBar bar;

    private boolean fullScreenMode = false;

    private Game current;

    public Main() {
        gh = new GameHandler();
        panel = new Paint(this);
        bar = new ToolBar(this);
        settings = new GameSettings(this);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setJMenuBar(bar);
        bar.populateGames();
        setResizable(false);

        panel.update(0, 0, 500, 500);
        cp.add(panel, BorderLayout.CENTER);
        setTitle("Games Manager");
        pack();
        setVisible(true);

        pt = new PaintTask(panel);
        pt.setPriority(Thread.MAX_PRIORITY);
        pt.setDaemon(false);
        pt.start();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                panel.update(0, 0, getWidth(), getHeight());
            }
        });
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(final WindowEvent e) {
                panel.update(0, 0, getWidth(), getHeight());
            }

        });
    }

    public ToolBar getToolBar() {
        return bar;
    }

    public GameHandler getGamesHandler() {
        return gh;
    }

    public Game getCurrentGame() {
        return current;
    }

    public void disableCurrentGame() {
        if (current != null) {
            current.onDisable();
        }
        gh.reload();
        reset();
    }

    @Override
    public void setSize(final int width, final int height) {
        panel.update(0, 0, width, height);
        panel.revalidate();
        pack();
        System.out.println(panel.getSize());
    }

    public void startGame(final Game g) {
        current = g;
        if (g != null) {
            panel.setGame(current);
            current.onLoad(settings);
        }
    }

    public void reset() {
        bar.setFullscreenAllowed(false);
        bar.setGameInformation(null);
        current = null;
        panel.setGame(current);
        setTitle("Games Manager");
        setResizable(false);
        setTickTime(6);
        setSize(500, 500);
    }

    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    public void changeFullScreenMode() {
        final GraphicsDevice device = getGraphicsConfiguration().getDevice();
        if (!device.isFullScreenSupported()) {
            return;
        }

        fullScreenMode = !fullScreenMode;

        super.dispose();
        if (fullScreenMode) {
            setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            setUndecorated(false);
            device.setFullScreenWindow(null);
        }
        setVisible(true);
    }

    @Override
    public void dispose() {
        gh.stopGames();
        pt.interrupt();
        super.dispose();
        System.exit(0);
    }

    public void setTickTime(int time) {
        pt.setTickTime(time);
    }

}
