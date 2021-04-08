package com.victorcheng;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class Main {

    public static Player player;
    public static ActivityForm af;
    private static JFrame frame;

    public static void main(String[] args) throws IOException {
        player = new Player();
        frame = new JFrame("Rapid Chinese");  // create an instance, title in ""

        frame.setSize(1000, 800);         // set it’s size
        frame.setLocation(200, 100);     // where to place it
        if (player.getName().equals("")) {
            frame.setContentPane(new NameEnter().getPanel1());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);         // show it
        } else {
            changePanel();
        }
    }

    public static void changePanel() throws IOException {
        af = new ActivityForm();
        frame.setContentPane(af.getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);         // show it

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ActivityForm.wL.saveStats();
                    player.saveStats();
                    ActivityForm.uP.saveData();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            // contined in second column
            // your code would go in windowClosing
            // to save data, etc.
            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public static void userComplete() {
        frame.setContentPane(new Over().getPanel1());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);         // show it
    }


}
