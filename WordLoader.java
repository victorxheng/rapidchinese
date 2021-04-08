package com.victorcheng;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.ArrayList;

public class WordLoader {

    private static final int wordsAtOnce = 20;
    private static int masteryRequirement;
    private final ArrayList<String> allWords = new ArrayList<String>();
    private final ArrayList<Word> learningWords = new ArrayList<Word>();//should be learning in groups of 10
    private final ArrayList<Word> masteredWords = new ArrayList<Word>();//should be learning in groups of 10
    private String currentWord;
    private String currentDefinition;
    private int wordIndex;
    private int currentIndex;


    public WordLoader() throws IOException {
        masteryRequirement = Main.player.getMasteryLevel();
        BufferedReader f = new BufferedReader(new FileReader("allWords"));
        String line = f.readLine();
        while (line != null) {
            allWords.add(line);
            line = f.readLine();
        }

        f = new BufferedReader(new FileReader("learnedWords"));
        line = f.readLine();
        if (line == null) {//First time: add words to learningWords list

            for (int i = 0; i < wordsAtOnce; i++) {
                String[] split = allWords.get(i).split(",");
                Word w = new Word(split[0], split[1], 0);
                learningWords.add(w);
            }
            wordIndex = wordsAtOnce - 1;
            saveStats();
        } else {//Not First Time: Load words into arraylists
            wordIndex = Integer.parseInt(line);
            line = f.readLine();
            while (line != null) {
                String[] split = line.split(",");
                int score = Integer.parseInt(split[2]);
                if (score >= masteryRequirement) {
                    Word w = new Word(split[0], split[1], masteryRequirement);
                    masteredWords.add(w);
                } else {
                    Word w = new Word(split[0], split[1], score);
                    learningWords.add(w);
                }
                line = f.readLine();
            }
        }

        currentIndex = 0;
        setCurrent(currentIndex);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public String getCurrentDefinition() {
        return currentDefinition;
    }

    public void wordCorrect(boolean wrong) {
        if (!wrong) learningWords.get(currentIndex).setScore(learningWords.get(currentIndex).getScore() + 1);
        if (learningWords.get(currentIndex).getScore() >= masteryRequirement) {
            masteredWords.add(learningWords.get(currentIndex));
            learningWords.remove(currentIndex);
            wordIndex++;
            if (wordIndex < allWords.size()) {
                String[] s = allWords.get(wordIndex).split(",");
                Word w = new Word(s[0], s[1], 0);
                learningWords.add(w);
            } else {
                if (learningWords.size() <= 0) {//User Completes all
                    Main.userComplete();
                }
            }
        } else {
            currentIndex++;
        }
        if (currentIndex >= learningWords.size()) {
            currentIndex = 0;
        }
        setCurrent(currentIndex);
    }

    public void wordWrong() {
        learningWords.get(currentIndex).setScore(0);
    }

    public void wordGiveUp() {
        saveToClip(currentWord);
    }

    public void saveStats() throws IOException {
        //save Arraylists into the learnedWords document for future use
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("learnedWords")));
        out.println(wordIndex);
        for (Word s : masteredWords) {//write mastered words first
            out.println(s.toString());
        }
        for (int i = 0; i < learningWords.size(); i++) {//writes learning words next
            Word w = learningWords.get(i);
            out.println(w.toString());
        }
        out.close();
    }

    public void reformatDocument() throws IOException {
        ArrayList<String> allWordsNew = new ArrayList<>();
        BufferedReader f = new BufferedReader(new FileReader("allWords"));
        String line = f.readLine();
        while (line != null) {
            String[] s = line.split(",");
            String write = s[0] + ",";
            if (s[1].contains("-")) {
                String[] split = s[1].split("-");
                if (split.length == 2) {
                    String writeIn = split[1];
                    int i = 0;
                    while (writeIn.charAt(i) == ' ') {
                        writeIn = writeIn.substring(i + 1);
                    }
                    write += writeIn;
                    allWordsNew.add(write);
                }
            }
            line = f.readLine();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("allWords")));
        for (String l : allWordsNew) {
            out.println(l);
        }
        out.close();
    }

    //need to modify
    private void saveToClip(String w) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(w);
        clipboard.setContents(transferable, null);
    }

    private void setCurrent(int index) {
        currentDefinition = learningWords.get(index).getDef();
        currentWord = learningWords.get(index).getWord();
    }


}
