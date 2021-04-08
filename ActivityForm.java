package com.victorcheng;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityForm extends JFrame {
    public static WordLoader wL;
    public static Upgrader uP;
    private JPanel panel1;
    private JTextField textField1;
    private JButton giveUpButton;
    private JLabel ChineseWord;
    private JLabel Definition;
    private JLabel output;
    private JButton fullResetButton1;
    private JLabel score;
    private JLabel nameDisplay;
    private JButton upgradeCashPerWordButton;
    private JLabel upgradeCostLabel;
    private JTable table1;
    private JTable table2;
    private JLabel CashPerSecond;
    private ArrayList<BusinessPrice> upgradeList;
    private ArrayList<BusinessPrice> storeList;

    private boolean wrong = false;

    public ActivityForm() throws IOException {
        //creates instances
        wL = new WordLoader();
        loadWord();

        uP = new Upgrader();


        loadUpgrades();
        loadStores();

        //sets up texts
        nameDisplay.setText("Name: " + Main.player.getName());
        ChineseWord.setFont(ChineseWord.getFont().deriveFont(20.0f));
        upgradeCostLabel.setText("Upgrade Cost: $" + uP.getWordBonusPrice());
        CashPerSecond.setText("Total Cash Per Second: " + uP.getCPS());

        //buttons
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (textField1.getText().replaceAll(" ", "").equals(wL.getCurrentWord())) {
                    wL.wordCorrect(wrong);
                    output.setText("Correct!");
                    loadWord();
                    textField1.setText("");
                    if (!wrong) {
                        Main.player.setScore(Main.player.getScore() + uP.getWordBonusAmount());
                    }
                    updateCash();
                    wrong = false;
                } else {
                    wL.wordWrong();
                    output.setText("Incorrect");
                    wrong = true;
                    textField1.setText("");
                }

            }
        });
        giveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wL.wordGiveUp();
                output.setText("Word Copied To Clipboard");
                wL.wordWrong();
                wrong = true;
            }
        });
        fullResetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Main.player.wipeStats();
                    System.exit(0);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        upgradeCashPerWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.player.getScore() >= uP.getWordBonusPrice()) {
                    Main.player.setScore(Main.player.getScore() - uP.getWordBonusPrice());
                    uP.onPurchaseWordBonus();
                    upgradeCostLabel.setText("Upgrade Cost: $" + uP.getWordBonusPrice());
                } else {
                    output.setText("Not enough cash");
                }
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    private void loadStores() { //loading the store lists
        storeList = uP.getStores();
        String[] columnNames = {"Buy Store", "Cost"};

        Object[][] data = new Object[storeList.size()][2];
        int index = 0;
        for (BusinessPrice bp : storeList) {
            String t = bp.getName();
            String price = "Cost: $" + bp.getPrice();
            data[index][0] = t;
            data[index][1] = price;
            index++;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table1.setModel(model);
        table1.setRowHeight(30);

        Action purchase = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table1 = (JTable) e.getSource();
                int row = Integer.valueOf(e.getActionCommand());

                if (Main.player.getScore() >= storeList.get(row).getPrice()) {
                    Main.player.setScore(Main.player.getScore() - storeList.get(row).getPrice());
                    uP.onPurchaseBusiness(row);
                    loadStores();
                } else {
                    output.setText("Not enough cash");
                }
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(table1, purchase, 1);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        CashPerSecond.setText("Total Cash Per Second: " + uP.getCPS());
    }

    private void loadUpgrades() { //loading upgrade lists
        upgradeList = uP.getUpgrades();
        String[] columnNames = {"Upgrade Store", "Cost"};

        Object[][] data = new Object[upgradeList.size()][2];
        int index = 0;
        for (BusinessPrice bp : upgradeList) {
            String t = bp.getName();
            String price = "Cost: $" + bp.getPrice();
            data[index][0] = t;
            data[index][1] = price;
            index++;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table2.setModel(model);
        table2.setRowHeight(30);

        Action purchaseUpgrade = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table1 = (JTable) e.getSource();
                int row = Integer.valueOf(e.getActionCommand());

                if (Main.player.getScore() >= upgradeList.get(row).getPrice()) {
                    Main.player.setScore(Main.player.getScore() - upgradeList.get(row).getPrice());
                    uP.onUpgradeBusiness(row);
                    loadUpgrades();
                } else {
                    output.setText("Not enough cash");
                }
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(table2, purchaseUpgrade, 1);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        CashPerSecond.setText("Total Cash Per Second: " + uP.getCPS());
    }

    private void loadWord() {
        ChineseWord.setText(wL.getCurrentWord());
        Definition.setText(wL.getCurrentDefinition());
    }

    public void updateCash() {
        score.setText("$" + Main.player.getScore());
    }

}
