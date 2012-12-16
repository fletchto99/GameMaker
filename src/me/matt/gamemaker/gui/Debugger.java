package me.matt.gamemaker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Debugger extends JFrame implements Runnable {

    private static final long serialVersionUID = -7273108132881476625L;

    private BufferedReader reader;

    public Debugger() throws IOException {
        init();
        PipedOutputStream pos = new PipedOutputStream();
        System.setOut(new PrintStream(pos, true));
        System.setErr(new PrintStream(pos, true));
        PipedInputStream pis = new PipedInputStream(pos);
        reader = new BufferedReader(new InputStreamReader(pis));
        new Thread(this).start();
    }

    public void run() {
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                debugTextArea.append(line + "\n");
                debugTextArea.setCaretPosition(debugTextArea.getDocument()
                        .getLength());
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error redirecting output : "
                    + ioe.getMessage());
        }
    }

    private void init() {
        debugTextArea = new JTextArea();
        scroll = new JScrollPane(debugTextArea);

        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("Debugger");

        debugTextArea.setEditable(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setViewportView(debugTextArea);
        add(scroll, BorderLayout.CENTER);
        scroll.setBounds(0, 0, 400, 400);

        setPreferredSize(new Dimension(400, 400));
        setSize(400, 400);
        setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private JScrollPane scroll;
    private JTextArea debugTextArea;

    public void clear() {
        debugTextArea.setText("");
    }
}
