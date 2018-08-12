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

    private BufferedImage spawnImage;
    private double scaleSpawn = 0.1;
    private boolean spawnAnimation = true;
    private BufferedImage combineImage;
    private double scaleCombine = 1.2;
    private boolean combineAnimation = false;

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        tileImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        spawnImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        combineImage = new BufferedImage(SIZE * 2, SIZE * 2,
                BufferedImage.TYPE_INT_ARGB);
        drawTileImage();
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
        String strValue = String.valueOf(this.value);
        int indentX = (SIZE - getTextX(strValue, tileFont, gs)) / 2;
        int indentY = (SIZE + getTextY(strValue, tileFont, gs)) / 2;
        gs.drawString(strValue, indentX, indentY);
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

    public int getTextX(String text, Font font, Graphics2D g) {
        g.setFont(font);
        Rectangle2D x = g.getFontMetrics().getStringBounds(text, g);
        return (int) x.getWidth();
    }

    public int getTextY(String text, Font font, Graphics2D g) {
        g.setFont(font);

        if (text.length() == 0) {
            return 0;
        }

        TextLayout textLayout = new TextLayout(text, font,
                g.getFontRenderContext());
        return (int) textLayout.getBounds().getHeight();
    }

    public void render(Graphics2D g) {
        if (this.spawnAnimation) {
            g.drawImage(spawnImage, this.x, this.y, null);
        } else if (this.combineAnimation) {
            g.drawImage(combineImage,
                    (int) (this.x + SIZE / 2 - this.scaleCombine * SIZE / 2),
                    (int) (this.y + SIZE / 2 - this.scaleCombine * SIZE / 2),
                    null);
        } else {
            g.drawImage(tileImage, this.x, this.y, null);
        }
    }

    public void update() {
        if (this.spawnAnimation) {
            addAnimation(scaleSpawn, spawnImage, tileImage);
            this.scaleSpawn += 0.1;

            if (this.scaleSpawn >= 1) {
                this.spawnAnimation = false;
            }
        } else if (this.combineAnimation) {
            addAnimation(scaleCombine, combineImage, tileImage);
            this.scaleCombine -= 0.05;

            if (this.scaleCombine <= 1) {
                this.combineAnimation = false;
            }
        }
    }

    public void addAnimation(double scaleAnimation,
            BufferedImage animationImage, BufferedImage baseImage) {
        AffineTransform transform = new AffineTransform();
        transform.translate(Tile.SIZE / 2 * (1 - scaleAnimation),
                Tile.SIZE / 2 * (1 - scaleAnimation));
        transform.scale(scaleAnimation, scaleAnimation);
        Graphics2D gs = (Graphics2D) animationImage.getGraphics();
        gs.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gs.drawImage(baseImage, transform, null);
        gs.dispose();
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

    public boolean isCombineAnimation() {
        return this.combineAnimation;
    }

    public void setCombineAnimation(boolean combineAnimation) {
        this.combineAnimation = combineAnimation;
    }
}
