package com.linhusp.twentyfortyeight;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Tile {
    public static final int SIZE = 100;
    public static final int SLIDE_SPEED = 30;

    private int x, y;
    private int value;
    private BufferedImage tileImage;
    private Color textColor;
    private Color backgroundColor;
    private Font tileFont;
    private boolean canCombine = true;

    private BufferedImage creatorImage;
    private double scaleCreator = 0.1;
    private boolean creatorAnimation = true;
    private BufferedImage combineImage;
    private double scaleCombine = 1.2;
    private boolean combineAnimation = false;

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        tileImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        creatorImage = new BufferedImage(SIZE, SIZE,
                BufferedImage.TYPE_INT_ARGB);
        combineImage = new BufferedImage(SIZE * 2, SIZE * 2,
                BufferedImage.TYPE_INT_ARGB);
        drawTileImage();
    }

    public void render(Graphics2D g) {
        if (this.creatorAnimation) {
            g.drawImage(creatorImage, this.x, this.y, null);
        } else if (this.combineAnimation) {
            g.drawImage(combineImage,
                    (int) (this.x + SIZE / 2 * (1 - this.scaleCombine)),
                    (int) (this.y + SIZE / 2 * (1 - this.scaleCombine)), null);
        } else {
            g.drawImage(tileImage, this.x, this.y, null);
        }
    }

    public void update() {
        if (this.creatorAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(SIZE / 2 * (1 - this.scaleCreator),
                    SIZE / 2 * (1 - this.scaleCreator));
            transform.scale(this.scaleCreator, this.scaleCreator);
            Graphics2D gs = (Graphics2D) creatorImage.getGraphics();
            gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gs.drawImage(tileImage, transform, null);
            this.scaleCreator += 0.1;
            gs.dispose();

            if (this.scaleCreator >= 1) {
                this.creatorAnimation = false;
            }
        } else if (this.combineAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(SIZE / 2 * (1 - this.scaleCombine),
                    SIZE / 2 * (1 - this.scaleCombine));
            transform.scale(this.scaleCombine, this.scaleCombine);
            Graphics2D gs = (Graphics2D) combineImage.getGraphics();
            gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gs.drawImage(tileImage, transform, null);
            this.scaleCombine += 0.05;
            gs.dispose();

            if (this.scaleCombine <= 1) {
                this.combineAnimation = false;
            }
        }
    }

    public void drawTileImage() {
        Graphics2D gs = (Graphics2D) tileImage.getGraphics();
        textColor = new Color(0xf9f6f2);

        if (this.value == 2) {
            backgroundColor = new Color(0xeee4da);
            textColor = new Color(0x776e65);
        } else if (this.value == 4) {
            backgroundColor = new Color(0xede0c8);
            textColor = new Color(0x776e65);
        } else if (this.value == 8) {
            backgroundColor = new Color(0xf2b179);
        } else if (this.value == 16) {
            backgroundColor = new Color(0xf59563);
        } else if (this.value == 32) {
            backgroundColor = new Color(0xf67c5f);
        } else if (this.value == 64) {
            backgroundColor = new Color(0xf65e3b);
        } else if (this.value == 128) {
            backgroundColor = new Color(0xedcf72);
        } else if (this.value == 256) {
            backgroundColor = new Color(0xedcc61);
        } else if (this.value == 512) {
            backgroundColor = new Color(0xedc850);
        } else if (this.value == 1024) {
            backgroundColor = new Color(0xedc53f);
        } else if (this.value == 2048) {
            backgroundColor = new Color(0xedc22e);
        } else if (this.value == 4096) {
            backgroundColor = new Color(0xff3d3e);
        } else {
            backgroundColor = new Color(0xff1e1e);
        }

        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(backgroundColor);
        gs.fillRoundRect(0, 0, SIZE, SIZE, Board.ARC, Board.ARC);
        gs.setColor(textColor);

        if (this.value <= 64) {
            tileFont = Game.MAIN_FONT;
        } else if (this.value <= 512) {
            tileFont = Game.MAIN_FONT.deriveFont(38f);
        } else if (this.value <= 8192) {
            tileFont = Game.MAIN_FONT.deriveFont(32f);
        } else {
            tileFont = Game.MAIN_FONT.deriveFont(26f);
        }

        gs.setFont(tileFont);
        int indentX = (SIZE
                - getTextX(String.valueOf(this.value), tileFont, gs)) / 2;
        int indentY = (SIZE
                + getTextY(String.valueOf(this.value), tileFont, gs)) / 2;
        gs.drawString(String.valueOf(this.value), indentX, indentY);
        gs.dispose();
    }

    public int getTextX(String v, Font tileFont, Graphics2D g) {
        g.setFont(tileFont);
        Rectangle2D x = g.getFontMetrics().getStringBounds(v, g);
        return (int) x.getWidth();
    }

    public int getTextY(String v, Font tileFont, Graphics2D g) {
        g.setFont(tileFont);
        return v.length() == 0 ? 0
                : (int) new TextLayout(v, tileFont, g.getFontRenderContext())
                        .getBounds().getHeight();
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        drawTileImage();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCanCombine() {
        return this.canCombine;
    }

    public void setCanCombine(boolean canCombine) {
        this.canCombine = canCombine;
    }
}