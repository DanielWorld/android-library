package net.danielpark.library.algorithm;

import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;

import net.danielpark.library.algorithm.model.Word;
import net.danielpark.library.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Split text to multiple string with lines.
 * <br><br>
 * Created by Daniel Park on 2018. 1. 4..
 */

public class TextAlignAlgorithm implements Algorithm {

    private String text;
    private final int maxLengthAtLine;

    @VisibleForTesting()
    final ArrayList<String> stringAtDot;                // string which ends with '.'

    @VisibleForTesting()
    final ArrayList<String> stringAtLine;

    final SparseArray<LinkedList<Word>> wordData;

    public TextAlignAlgorithm() {
        this(15);
    }

    public TextAlignAlgorithm(int maxLengthAtLine) {
        this.maxLengthAtLine = maxLengthAtLine;
        this.stringAtDot = new ArrayList<>();
        this.stringAtLine = new ArrayList<>();
        this.wordData = new SparseArray<>();
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
//        createStringAtDot();

        // 2. Each sentence has words, and their information.
        // 2-1. try to distribute numbers within max length, and no limit lines
        // (But, distribute algorithm must keep the balance! remember!)
        calculate(text);
        generateText();
    }

    /**
     * Split text to multiple string ends with '.'
     */
    @VisibleForTesting()
    void createStringAtDot() {
        stringAtDot.clear();
        stringAtLine.clear();

        String[] sentences = text.split("([\\\\.][\\s])");

        for (int index = 0; index < sentences.length; index++) {
            if (StringUtil.isNullorEmpty(sentences[index])) continue;

            // Try to trim!
            sentences[index] = sentences[index].trim();

            if (index < sentences.length - 1)
                stringAtDot.add(sentences[index] + ".");
            else
                stringAtDot.add(sentences[index]);

            // TODO: SHOULD calculate asynchronously.
//            calculate(sentences[index]);
        }
    }



    @VisibleForTesting()
    void calculate(final String sentence) {
        wordData.clear();

        String[] words = sentence.split("(\\s)");

        int startIndex = 0;
        String line = "";

        // Arrange text with multiple lines.
        for (String w : words) {
            Word word = new Word(w);

            LinkedList<Word> tempWordData = wordData.get(startIndex);
            if (tempWordData == null) {
                tempWordData = new LinkedList<>();
            }

            if (StringUtil.isNullorEmpty(line)) {
                // This is first line init. and no way to avoid it. just put this!
                tempWordData.add(word);
                wordData.put(startIndex, tempWordData);

                line = w;
            }
            else if (line.length() + 1 + w.length() <= maxLengthAtLine) {
                // line + " " + word is below then max length.
                tempWordData.add(word);
                wordData.put(startIndex, tempWordData);

                line += " " + w;
            }
            else {
                Word prevWord = null;
                // TODO: of course but what if previous line has more than 2 words and the last word is only one character?
                // Move to next line.
                if (tempWordData.size() > 1 && tempWordData.getLast().getLength() == 1) {
                    prevWord = tempWordData.getLast();
                    tempWordData.removeLast();
                    wordData.put(startIndex, tempWordData);
                }

                // Can't add word anymore. move to next
                startIndex++;
                tempWordData = new LinkedList<>();

                if (prevWord != null)
                    tempWordData.add(prevWord);

                tempWordData.add(word);
                wordData.put(startIndex, tempWordData);

                if (prevWord != null) {
                    line = prevWord.getWord() + " " + w;
                } else {
                    line = w;
                }
            }
        }

        // If there are more than 2 lines and last line has only a few words.
        // Need to calculate again for better adjustment.
        reArrangeText(startIndex);
    }

    private void reArrangeText(int lastIndex) {
        if (lastIndex <= 0) return;

        LinkedList<Word> lastData = wordData.get(lastIndex);
        LinkedList<Word> prevData = wordData.get(lastIndex - 1);

        int lastDataSum = 0, prevDataSum = 0;

        for (Word w : lastData) {
            lastDataSum += w.getLength() + 1;
        }
        lastDataSum -= 1;

        for (Word w : prevData) {
            prevDataSum += w.getLength() + 1;
        }
        prevDataSum -= 1;

        // While re-arranging words, sometimes previous text contains one character at the end position.
        // Not good for showing it to user, check this of course!
        while (lastDataSum <= maxLengthAtLine &&
                ((prevDataSum - lastDataSum) > maxLengthAtLine / 2) || prevData.getLast().getLength() == 1) {
            Word prevLastWord = prevData.getLast();

            if (prevLastWord != null) {
                prevDataSum -= (prevLastWord.getLength() + 1);
                lastDataSum += (prevLastWord.getLength() + 1);

                if (lastDataSum <= maxLengthAtLine) {
                    lastData.addFirst(prevData.getLast());
                    prevData.removeLast();
                } else {
                    break;
                }
            }
        }

        wordData.put(lastIndex, lastData);
        wordData.put(lastIndex - 1, prevData);
    }

    /**
     * Generate parsed text data.
     */
    private void generateText() {
        stringAtLine.clear();

        int key = 0;

        StringBuilder sb = new StringBuilder();

        while (wordData.get(key) != null) {
            for (Word w : wordData.get(key)) {
                sb.append(w.getWord() + " ");
            }
            sb.deleteCharAt(sb.length() - 1);

            stringAtLine.add(sb.toString());

            key++;
            sb.delete(0, sb.length());
        }
    }
}
