package com.victorcheng;

import java.io.*;
import java.util.ArrayList;

public class Upgrader {


    private static final int baseStoreCost = 200;
    private static final int baseUpgradeCost = 1000;
    private static final int baseCPS = 1;
    private static final double baseStoreRate = 1.5;
    private static final double baseUpgradeRate = 1.5;
    private static final double baseCPSRate = 1.5;
    private static final double storeRate = 1.1;
    private static final double upgradeRate = 1.5;
    private static final double CPSRate = 1.5;
    private static final int baseWordBonus = 100;
    private static final double bonusRate = 1.5;
    private static final int baseWordBonusCost = 1000;
    private static final double bonusCostRate = 1.5;
    private final ArrayList<Business> businesses = new ArrayList<>();
    private final ArrayList<BusinessPrice> upgrades = new ArrayList<>();
    private final ArrayList<BusinessPrice> stores = new ArrayList<>();
    private long CPS;
    private int wordBonusAmount;
    private int wordBonusPrice;


    public Upgrader() throws IOException {
        //read file
        BufferedReader f = new BufferedReader(new FileReader("businessNames"));
        String line = f.readLine();
        boolean firstTime = false;
        while (line != null) {
            String[] s = line.split(",");
            Business b;
            if (s.length > 1) {
                b = new Business(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]));
            } else {
                firstTime = true;
                b = new Business(s[0], 0, 0);
            }
            businesses.add(b);
            line = f.readLine();
        }
        if (firstTime) saveData();

        int i = 0;
        CPS = 0;
        //calculate stats
        for (Business b : businesses) {
            BusinessPrice u = new BusinessPrice(b.getName(), calculate(2, i, b.getLevel()));
            upgrades.add(u);
            BusinessPrice store = new BusinessPrice(b.getName(), calculate(1, i, b.getCount()));
            stores.add(store);
            CPS += (calculate(3, i, b.getLevel()) * b.getCount());
            i++;
        }
        wordBonusCalculations();
        Generation generationThread = new Generation();
        generationThread.start();
    }

    public ArrayList<BusinessPrice> getUpgrades() {
        return upgrades;
    }

    public ArrayList<BusinessPrice> getStores() {
        return stores;
    }

    public void saveData() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("businessNames")));
        for (Business b : businesses) {
            out.println(b.toString());
        }
        out.close();
    }

    public int getWordBonusAmount() {
        return wordBonusAmount;
    }

    public int getWordBonusPrice() {
        return wordBonusPrice;
    }

    //Other End: Handles purchase price, handles turning list of prices into lists, score deduction too
    public void onPurchaseWordBonus() {
        //calculate new amount
        Main.player.setWordBonusLevel(Main.player.getWordBonusLevel() + 1);
        wordBonusCalculations();
    }

    public void onPurchaseBusiness(int index) {
        Business b = businesses.get(index);//gets business
        b.setCount(b.getCount() + 1);//adds business
        businesses.set(index, b);//saves new business count
        BusinessPrice store = new BusinessPrice(b.getName(), calculate(1, index, b.getCount()));
        stores.set(index, store);//sets store price
        CPS += calculate(3, index, b.getLevel());//add CPS
        //other side will handle updating the button with the new price
    }

    public void onUpgradeBusiness(int index) {
        Business b = businesses.get(index);//gets business
        b.setLevel(b.getLevel() + 1);//adds business
        businesses.set(index, b);//saves new business level
        BusinessPrice up = new BusinessPrice(b.getName(), calculate(2, index, b.getLevel()));
        upgrades.set(index, up);//sets store price
        CPS = 0;
        int i = 0;
        for (Business h : businesses) {
            CPS += (calculate(3, i, h.getLevel()) * h.getCount());
            i++;
        }
        //other side will handle updating the button with the new price
    }

    public long getCPS() {
        return CPS;
    }

    private int calculate(int type, int index, int magnitude) {//1: store 2: upgrade 3: CPS
        int base;
        double baseRate;
        double rate;

        switch (type) {
            case 1:
                base = baseStoreCost;
                baseRate = baseStoreRate;
                rate = storeRate;
                break;
            case 2:
                base = baseUpgradeCost;
                baseRate = baseUpgradeRate;
                rate = upgradeRate;
                break;
            case 3:
                base = baseCPS;
                baseRate = baseCPSRate;
                rate = CPSRate;
                break;
            default:
                return 0;
        }

        return (int) Math.ceil(((double) base * (Math.pow(baseRate, index))) * (Math.pow(rate, magnitude)));
    }

    private void wordBonusCalculations() {
        wordBonusAmount = calculateWordBonus(false, Main.player.getWordBonusLevel());
        wordBonusPrice = calculateWordBonus(true, Main.player.getWordBonusLevel());
    }

    private int calculateWordBonus(boolean price, int level) {
        if (!price)
            return (int) Math.ceil(baseWordBonus * Math.pow(bonusRate, level));
        else
            return (int) Math.ceil(baseWordBonusCost * Math.pow(bonusCostRate, level));
    }

}
