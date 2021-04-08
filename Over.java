package com.victorcheng;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Over {
    private JPanel panel1;
    private JLabel Over;
    private JLabel score;
    private JButton resetButton;
    private JButton exitButton;
    public Over(){
        score.setText("Score: "+Main.player.getScore());
    }

    public JPanel getPanel1() {
        return panel1;
    }

}
