package com.linhusp.twentyfortyeight;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Board {

    public static final int LIMIT = 4;
    public static final int SPACING = 13;
    public static final int SIZE = (LIMIT + 1) * SPACING + LIMIT * Tile.SIZE;
    public static final int ARC = 8;

    private int indentX, indentY;
    private BufferedImage background;
    private BufferedImage boardImg;
    // private Random random;
    private Tile[][] board;

    private boolean started;
    private int xd, yd;

    public Board(int indentX, int indentY) {
        this.indentX = indentX;
        this.indentY = indentY;
        background = new BufferedImage(Board.SIZE, Board.SIZE,
                BufferedImage.TYPE_INT_RGB);
        boardImg = new BufferedImage(Board.SIZE, Board.SIZE,
                BufferedImage.TYPE_INT_RGB);
        // random = new Random();
        board = new Tile[Board.LIMIT][Board.LIMIT];
        init();
    }

    public void init() {
        getRandomTile();
        getRandomTile();

        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Palette.BOARD_BG);
        g.fillRect(0, 0, Board.SIZE, Board.SIZE);
        g.setColor(Palette.GRID_BORDER);
        g.fillRoundRect(0, 0, Board.SIZE, Board.SIZE, ARC, ARC);
        g.setColor(Palette.GRID_BG);

        for (int x = 0; x < LIMIT; x++) {
            for (int y = 0; y < LIMIT; y++) {
                g.fillRoundRect(x * (Tile.SIZE + SPACING) + SPACING,
                        y * (Tile.SIZE + SPACING) + SPACING, Tile.SIZE,
                        Tile.SIZE, ARC, ARC);
            }
        }
    }

    public void render(Graphics2D g) {
        Graphics2D gs = (Graphics2D) boardImg.getGraphics();
        gs.drawImage(background, 0, 0, null);

        // render tiles
        for (int y = 0; y < LIMIT; y++) {
            for (int x = 0; x < LIMIT; x++) {
                Tile currTile = board[x][y];
                if (currTile != null) {
                    currTile.render(gs);
                }
            }
        }

        g.drawImage(boardImg, this.indentX, this.indentY, null);
        gs.dispose();
    }

    public void update() {
        if (this.started) {
            move(this.xd, this.yd);
            this.started = false;
        }
            
        for (int x = 0; x < LIMIT; x++) {
            for (int y = 0; y < LIMIT; y++) {
                Tile currTile = board[x][y];
                if (currTile != null) {
                    currTile.update();
                    updatePosition(currTile, x, y);
                }
            }
        }
    }

    public void move(int xd, int yd) {
        boolean check = false;

        // reset canCombine
        for (int x = 0; x < LIMIT; x++) {
            for (int y = 0; y < LIMIT; y++) {
                Tile currTile = board[x][y];
                if (currTile != null) {
                    currTile.setCanCombine(true);
                }
            }
        }

        if (xd == -1) {
            // go left
            for (int y = 0; y < LIMIT; y++) {
                for (int x = 0; x < LIMIT; x++) {
                    if (!check) {
                        check = checkMove(x, y, xd, yd);
                    } else {
                        checkMove(x, y, xd, yd);
                    }
                }
            }
        } else if (xd == 1) {
            // go right
            for (int y = 0; y < LIMIT; y++) {
                for (int x = LIMIT - 1; x >= 0; x--) {
                    if (!check) {
                        check = checkMove(x, y, xd, yd);
                    } else {
                        checkMove(x, y, xd, yd);
                    }
                }
            }
        } else if (yd == -1) {
            // go up
            for (int y = 0; y < LIMIT; y++) {
                for (int x = 0; x < LIMIT; x++) {
                    if (!check) {
                        check = checkMove(x, y, xd, yd);
                    } else {
                        checkMove(x, y, xd, yd);
                    }
                }
            }
        } else if (yd == 1) {
            // go down
            for (int y = LIMIT - 1; y >= 0; y--) {
                for (int x = 0; x < LIMIT; x++) {
                    if (!check) {
                        check = checkMove(x, y, xd, yd);
                    } else {
                        checkMove(x, y, xd, yd);
                    }
                }
            }
        }

        if (check) {
            getRandomTile();
        }
    }

    public boolean checkMove(int x, int y, int xd, int yd) {
        Tile currTile = board[x][y];

        if (currTile == null) {
            return false;
        }

        boolean move = true;
        boolean canMove = false;
        int preX = x;
        int preY = y;

        while (move) {
            preX += xd;
            preY += yd;

            if (!isValidDirection(preX, preY, xd, yd)) {
                break;
            }

            if (board[preX][preY] == null) {
                board[preX][preY] = currTile;
                board[preX - xd][preY - yd] = null;
                canMove = true;
            } else if (board[preX][preY].getValue() == currTile.getValue()
                    && board[preX][preY].isCanCombine()) {
                board[preX][preY].setCanCombine(false);
                board[preX][preY].setValue(currTile.getValue() * 2);
                board[preX - xd][preY - yd] = null;
                canMove = true;
            } else {
                move = false;
            }
        }

        return canMove;
    }

    public boolean isValidDirection(int x, int y, int xd, int yd) {
        return xd == -1 && x >= 0 
                || xd == 1 && x <= LIMIT - 1 
                || yd == -1 && y >= 0 
                || yd == 1 && y <= LIMIT - 1;
    }

    public void updatePosition(Tile currTile, int x, int y) {
        int resetX = getTileMargin(x);
        int resetY = getTileMargin(y);
        int distX = currTile.getX() - resetX;
        int distY = currTile.getY() - resetY;

        if (Math.abs(distX) < Tile.SLIDE_SPEED) {
            currTile.setX(currTile.getX() - distX);
        }

        if (Math.abs(distY) < Tile.SLIDE_SPEED) {
            currTile.setY(currTile.getY() - distY);
        }

        if (distX < 0) {
            currTile.setX(currTile.getX() + Tile.SLIDE_SPEED);
        }

        if (distX > 0) {
            currTile.setX(currTile.getX() - Tile.SLIDE_SPEED);
        }

        if (distY < 0) {
            currTile.setY(currTile.getY() + Tile.SLIDE_SPEED);
        }

        if (distY > 0) {
            currTile.setY(currTile.getY() - Tile.SLIDE_SPEED);
        }
    }

    public void getRandomTile() {
        Random random = new Random();
        boolean valid = false;
        while (!valid) {
            int randX = random.nextInt(LIMIT);
            int randY = random.nextInt(LIMIT);

            if (board[randX][randY] == null) {
                board[randX][randY] = new Tile(random.nextInt(10) < 9 ? 2 : 4,
                        getTileMargin(randX), getTileMargin(randY));
                valid = true;
            }
        }
    }

    public int getTileMargin(int coor) {
        return SPACING + coor * (Tile.SIZE + SPACING);
    }

    public void setVector(int xd, int yd) {
        this.xd = xd;
        this.yd = yd;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
