package me.matt.gamemaker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            } catch (final Exception e) {
            }
            new Main();
        });
    }

    private static final long serialVersionUID = -4815895420008072546L;

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
        final Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setJMenuBar(bar);
        bar.populateGames();
        this.setResizable(false);

        panel.update(0, 0, 500, 500);
        cp.add(panel, BorderLayout.CENTER);
        this.setTitle("Games Manager");
        this.pack();
        this.setVisible(true);

        pt = new PaintTask(panel);
        pt.setPriority(Thread.MAX_PRIORITY);
        pt.setDaemon(false);
        pt.start();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
                    bar.reload();
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                panel.update(0, 0, Main.this.getWidth(), Main.this.getHeight());
            }
        });
        this.addWindowStateListener(e -> panel.update(0, 0,
                Main.this.getWidth(), Main.this.getHeight()));
    }

    public void changeFullScreenMode() {
        final GraphicsDevice device = this.getGraphicsConfiguration()
                .getDevice();
        if (!device.isFullScreenSupported()) {
            return;
        }

        fullScreenMode = !fullScreenMode;

        super.dispose();
        if (fullScreenMode) {
            this.setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            this.setUndecorated(false);
            device.setFullScreenWindow(null);
        }
        this.setVisible(true);
    }

    public void disableCurrentGame() {
        if (current != null) {
            current.onDisable();
        }
        gh.reload();
        this.reset();
    }

    @Override
    public void dispose() {
        gh.stopGames();
        pt.interrupt();
        super.dispose();
        System.exit(0);
    }

    public Game getCurrentGame() {
        return current;
    }

    public GameHandler getGamesHandler() {
        return gh;
    }

    public ToolBar getToolBar() {
        return bar;
    }

    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    public void reset() {
        bar.setFullscreenAllowed(false);
        bar.setGameInformation(null);
        current = null;
        panel.setGame(current);
        this.setTitle("Games Manager");
        this.setResizable(false);
        this.setTickTime(6);
        this.setSize(500, 500);
    }

    @Override
    public void setSize(final int width, final int height) {
        panel.update(0, 0, width, height);
        panel.revalidate();
        this.pack();
        System.out.println(panel.getSize());
    }

    public void setTickTime(final int time) {
        pt.setTickTime(time);
    }

    public void startGame(final Game g) {
        current = g;
        if (g != null) {
            panel.setGame(current);
            current.onLoad(settings);
        }
    }

}
