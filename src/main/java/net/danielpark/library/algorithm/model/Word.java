package net.danielpark.library.algorithm.model;

/**
 * Created by Daniel Park on 2018. 1. 5..
 */

public final class Word {

    private String word;
    private int length;

    public Word(String word) {
        this.word = word;
        this.length = word.length();
    }

    public String getWord() {
        return word;
    }

    public int getLength() {
        return length;
    }
}
