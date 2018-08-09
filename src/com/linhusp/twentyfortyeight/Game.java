package com.linhusp.twentyfortyeight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;
    // Change "Arial" to "Roboto" font if you want better exp
    public static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 44);

    private Thread thread;
    private BufferedImage baseImage;
    private Board board;
    private boolean running;

    public Game() {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        this.running = true;
        initGame();
    }

    public void initGame() {
        baseImage = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_ARGB);
        int boardIndent = (Game.WIDTH - Board.SIZE) / 2;
        board = new Board(boardIndent, Game.HEIGHT - Board.SIZE - boardIndent);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int fps = 0;
        int updates = 0;
        long timer = System.currentTimeMillis();
        double rate = 1000000000.0 / 60;
        double then = System.nanoTime();
        double unprocessed = 0;

        while (running) {
            boolean render = false;
            double now = System.nanoTime();
            unprocessed += (now - then) / rate;
            then = now;

            while (unprocessed >= 1) {
                updates++;
                update();
                unprocessed--;
                render = true;
            }

            if (render) {
                fps++;
                repaint();
                render = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (System.currentTimeMillis() - timer > 1000) {
            fps = 0;
            updates = 0;
            timer += 1000;
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gs = (Graphics2D) baseImage.getGraphics();
        gs.setColor(Palette.BOARD_BG);
        gs.fillRect(0, 0, WIDTH, HEIGHT);
        board.render(gs);
        g.drawImage(baseImage, 0, 0, this);
    }

    public void update() {
        board.update();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            board.setVector(-1, 0);
            if (!board.isStarted()) {
                board.setStarted(true);
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            board.setVector(1, 0);
            if (!board.isStarted()) {
                board.setStarted(true);
            }
        } else if (keyCode == KeyEvent.VK_UP) {
            board.setVector(0, -1);
            if (!board.isStarted()) {
                board.setStarted(true);
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            board.setVector(0, 1);
            if (!board.isStarted()) {
                board.setStarted(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
