package com.linhusp.twentyfortyeight;

import javax.swing.JFrame;

public class Start extends JFrame {

    private static final long serialVersionUID = 1L;

    public Start() {
        super("2048");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(new Game());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Start();
    }
}
