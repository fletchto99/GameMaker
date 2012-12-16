package me.matt.gamemaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import me.matt.gamemaker.Main;
import me.matt.gamemaker.game.Game;

public class Paint extends JPanel implements MouseMotionListener,
        MouseListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private Game game = null;

    private BufferedImage backBuffer;

    private final Main main;

    public Paint(final Main main) {
        this.main = main;
        setFocusable(true);
        setBackground(Color.BLACK);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public void update(int x, int y, int width, int height) {
        width += main.getInsets().left + main.getInsets().right;
        height += main.getInsets().bottom;
        try {
            backBuffer = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
        } catch (final Exception ex) {
        }
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paint(final Graphics g) {
        try {
            g.drawImage(backBuffer, 0, 0, null);
            if (game != null) {
                game.onRepaint(g);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseDragged(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseMoved(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseClicked(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseEntered(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseExited(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        try {
            if (game != null) {
                game.mousePressed(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        try {
            if (game != null) {
                game.mouseReleased(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        try {
            if (game != null) {
                game.keyPressed(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        try {
            if (game != null) {
                game.keyReleased(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        try {
            if (game != null) {
                game.keyTyped(e);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

}