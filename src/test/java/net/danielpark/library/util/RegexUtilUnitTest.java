package net.danielpark.library.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 15..
 */

public class RegexUtilUnitTest {

    private ArrayList<String> correctColorHex = new ArrayList<>();
    private ArrayList<String> incorrectColorHex = new ArrayList<>();

    private ArrayList<String> correctFloatString = new ArrayList<>();
    private ArrayList<String> incorrectFloatString = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        correctColorHex.add("228718");
        correctColorHex.add("#228718");
        correctColorHex.add("f2871f");
        correctColorHex.add("#fff118");
        correctColorHex.add("#aafff118");
        correctColorHex.add("aafff118");

        incorrectColorHex.add("hf1822");
        incorrectColorHex.add("#fh1822");
        incorrectColorHex.add("#fh18211");
        incorrectColorHex.add("fh18211");
        incorrectColorHex.add("#fh182110");
        incorrectColorHex.add("fh182110");

        correctFloatString.add("0.0");
        correctFloatString.add("0.0000");
        correctFloatString.add("0.0011");
        correctFloatString.add("0.9181881");
        correctFloatString.add("1.0");
        correctFloatString.add("1.00");
        correctFloatString.add("1.000");
        correctFloatString.add("0");
        correctFloatString.add("1");

        // Daniel (2017-12-16 23:27:19): 0000 is not right format.. sorry!
        incorrectFloatString.add("0000");
        incorrectFloatString.add("0000.0");
        incorrectFloatString.add("0000.1");
        incorrectFloatString.add("0011.1");
        incorrectFloatString.add("2231.1");
        incorrectFloatString.add("10019");
        incorrectFloatString.add("1.01");
        incorrectFloatString.add("1.");
        incorrectFloatString.add("0.");
        incorrectFloatString.add(".5834");
    }

    @Test
    public void isValidColorString() {
        for (String colorHex : correctColorHex) {
            Assert.assertTrue(RegexUtil.isValidColorString(colorHex));
        }

        for (String colorHex : incorrectColorHex) {
            Assert.assertFalse(RegexUtil.isValidColorString(colorHex));
        }
    }

    @Test
    public void isValidFloatString() {
        for (String colorHex : correctFloatString) {
            Assert.assertTrue(RegexUtil.isValidFloatString(colorHex));
        }

        for (String colorHex : incorrectFloatString) {
            Assert.assertFalse(RegexUtil.isValidFloatString(colorHex));
        }
    }
}
