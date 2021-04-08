package com.victorcheng;

public class Business {
    private String name;
    private int count;
    private int level;

    public Business(String name, int count, int level) {
        this.name = name;
        this.count = count;
        this.level = level;
    }

    public String getName() {
        return name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return name + "," + count + "," + level;
    }
}
