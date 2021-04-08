package com.victorcheng;

import java.io.*;
import java.util.ArrayList;

public class Player {
    private String name;
    private int masteryLevel;
    private long score;
    private int wordBonusLevel;


    public Player() throws IOException {
        //reads from file
        BufferedReader f = new BufferedReader(new FileReader("dataSave"));

        String name = f.readLine();
        if (name == null) {
            this.name = "";
            masteryLevel = 0;
            score = 0;
            wordBonusLevel = 0;
        } else {
            this.name = name;
            this.masteryLevel = Integer.parseInt(f.readLine());
            this.score = Long.parseLong(f.readLine());
            this.wordBonusLevel = Integer.parseInt(f.readLine());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getWordBonusLevel() {
        return wordBonusLevel;
    }

    public void setWordBonusLevel(int wordBonusLevel) {
        this.wordBonusLevel = wordBonusLevel;
    }


    public int getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(int masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public void saveStats() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dataSave")));
        out.println(name);
        out.println(masteryLevel);
        out.println(score);
        out.println(wordBonusLevel);
        out.close();
    }

    public void wipeStats() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dataSave")));
        out.close();
        out = new PrintWriter(new BufferedWriter(new FileWriter("learnedWords")));
        out.close();

        BufferedReader f = new BufferedReader(new FileReader("businessNames"));
        String l = f.readLine();
        ArrayList<String> st = new ArrayList<String>();
        while (l != null) {
            String[] split = l.split(",");
            st.add(split[0] + ",0,0");
            l = f.readLine();
        }
        out = new PrintWriter(new BufferedWriter(new FileWriter("businessNames")));
        for (String s : st) {
            out.println(s);
        }
        out.close();

    }
}
