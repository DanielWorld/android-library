package net.danielpark.library.algorithm;

import android.os.Build;
import android.support.annotation.VisibleForTesting;

import net.danielpark.library.util.StringUtil;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Split text to multiple string with lines.
 * <br><br>
 * Created by Daniel Park on 2018. 1. 4..
 */

public class TextAlignAlgorithm implements Algorithm {

    private String text;
    private final int maxLength;

    @VisibleForTesting()
    final ArrayList<String> stringAtDot;

    @VisibleForTesting()
    final ArrayList<String> stringAtLine;

    public TextAlignAlgorithm() {
        this(15);
    }

    public TextAlignAlgorithm(int maxLengthAtLine) {
        this.maxLength = maxLengthAtLine;
        this.stringAtDot = new ArrayList<>();
        this.stringAtLine = new ArrayList<>();
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void build() {
        // ---------------------
        // Rule
        // 1. Each word can't be broken.
        // 2. max Length at line is a limit. But keep first rule.
        // 3. If a sentence ends with '.' , don't add another words in the same line.

        // Process
        // 1. Split by '.[\\s]' so gather info each sentence.
        // (So, you can guess words info in a sentence at least.)
        createStringAtDot();

        // 2. Each sentence has words, and their information.
        // 2-1. try to distribute numbers within max length, and no limit lines
        // (But, distribute algorithm must keep the balance! remember!)
    }

    /**
     * Split text to multiple string ends with '.'
     */
    @VisibleForTesting()
    void createStringAtDot() {
        stringAtDot.clear();

        String[] sentences = text.split("([\\\\.][\\s])");

        for (int index = 0; index < sentences.length; index++) {
            if (StringUtil.isNullorEmpty(sentences[index])) continue;

            // Try to trim!
            sentences[index] = sentences[index].trim();

            if (index < sentences.length - 1)
                stringAtDot.add(sentences[index] + ".");
            else
                stringAtDot.add(sentences[index]);
        }
    }

}
