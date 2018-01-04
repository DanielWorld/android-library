package net.danielpark.library.algorithm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Daniel Park on 2018. 1. 4..
 */
@RunWith(RobolectricTestRunner.class)
public class TextAlignAlgorithmUnitTest {

    private TextAlignAlgorithm textAlignAlgorithm;
    private String text;
    String[] textStringAtDotResult;
    private int maxLengthAtLine;

    @Before
    public void setUp() {
        maxLengthAtLine = 12;
        textAlignAlgorithm = new TextAlignAlgorithm(maxLengthAtLine);
    }

    @Test
    public void createStringAtDotTest() {
        text = "안녕하세요...... 저는    다니엘    이라고.해요. 오늘 기분이 어때요? 저는 힘이 더욱 나서 기분이     좋네요.";
        textStringAtDotResult = new String[]{ "안녕하세요......",
                "저는    다니엘    이라고.해요.",
                "오늘 기분이 어때요? 저는 힘이 더욱 나서 기분이     좋네요."};

        createStringAtDot(text, textStringAtDotResult);

        text = "여러가지 가능성이 있을 것입니다만....   현재  자네의    기분이 좋다구? 그럼.난.자넬.그냥.둘 수야. 없잖은가???";
        textStringAtDotResult = new String[]{ "여러가지 가능성이 있을 것입니다만....",
                "현재  자네의    기분이 좋다구? 그럼.난.자넬.그냥.둘 수야.", "없잖은가???"};

        createStringAtDot(text, textStringAtDotResult);

        text = "안녕하세요, 저는 안드로이드 개발자 다니엘이라고 합니다. 반갑습니다!";
        textStringAtDotResult = new String[]{ "안녕하세요, 저는 안드로이드 개발자 다니엘이라고 합니다.",
                "반갑습니다!"};

        createStringAtDot(text, textStringAtDotResult);
    }

    private void createStringAtDot(String text, String[] result) {
        int resultSize = result.length;
        textAlignAlgorithm.setText(text);
        textAlignAlgorithm.createStringAtDot();

        for (String dots : textAlignAlgorithm.stringAtDot)
            println(dots);

        println("");

        assertThat(Arrays.deepEquals(
                textAlignAlgorithm.stringAtDot.toArray(new String[resultSize]), result), is(true));
    }

    private void println(String s) {
        System.out.println(s);
    }
}
