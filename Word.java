package com.victorcheng;

public class Word {
    public String word;
    public String def;
    public int score;

    public Word(String word, String def, int score) {
        this.word = word;
        this.def = def;
        this.score = score;
    }

    @Override
    public String toString() {
        return word + "," + def + "," + score;
    }

    public String getWord() {
        return word;
    }

    public String getDef() {
        return def;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
