package com.victorcheng;

public class Generation extends Thread {
    public void run() {
        while (this.isAlive()) {
            try {
                Thread.sleep(1000);
                Main.player.setScore(Main.player.getScore() + ActivityForm.uP.getCPS());
                Main.af.updateCash();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
