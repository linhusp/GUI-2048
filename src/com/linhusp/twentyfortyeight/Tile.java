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
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        backgroundColor = getBackground();
        gs.setColor(backgroundColor);
        gs.fillRoundRect(0, 0, SIZE, SIZE, Board.ARC, Board.ARC);
        textColor = getForeground();
        gs.setColor(textColor);
        tileFont = getFontResize();
        gs.setFont(tileFont);
        int indentX = (SIZE
                - getTextX(String.valueOf(this.value), tileFont, gs)) / 2;
        int indentY = (SIZE
                + getTextY(String.valueOf(this.value), tileFont, gs)) / 2;
        gs.drawString(String.valueOf(this.value), indentX, indentY);
        gs.dispose();
    }

    public Color getForeground() {
        return this.value < 8 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    public Color getBackground() {
        switch (this.value) {
        case 2:
            return new Color(0xeee4da);
        case 4:
            return new Color(0xede0c8);
        case 8:
            return new Color(0xf2b179);
        case 16:
            return new Color(0xf59563);
        case 32:
            return new Color(0xf67c5f);
        case 64:
            return new Color(0xf65e3b);
        case 128:
            return new Color(0xedcf72);
        case 256:
            return new Color(0xedcc61);
        case 512:
            return new Color(0xedc850);
        case 1024:
            return new Color(0xedc53f);
        case 2048:
            return new Color(0xedc22e);
        default:
            return new Color(0xff1e1e);
        }
    }

    public Font getFontResize() {
        if (this.value <= 64) {
            return Game.MAIN_FONT;
        } else if (this.value <= 512) {
            return Game.MAIN_FONT.deriveFont(38f);
        } else if (this.value <= 8192) {
            return Game.MAIN_FONT.deriveFont(32f);
        }
        return Game.MAIN_FONT.deriveFont(26f);
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
