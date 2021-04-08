package com.victorcheng;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NameEnter {
    private JPanel panel1;
    private JTextField enterName;
    private JSlider slider1;
    private JButton startButton;
    private JLabel skillValue;
    private JLabel mastery;

    public NameEnter() {
        startButton.addActionListener(new ActionListener() { //The enter button
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enterName.getText().replaceAll("\\s", "").equals("")) {
                    enterName.setText("Name Cannot Be Blank");
                } else if (enterName.getText().contains("\\")) {
                    enterName.setText("Name Cannot Contain Backslashes");
                } else {
                    Main.player.setName(enterName.getText());
                    Main.player.setMasteryLevel(slider1.getValue());
                    try {
                        Main.player.saveStats();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        Main.changePanel();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                skillValue.setText("" + slider1.getValue());
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
