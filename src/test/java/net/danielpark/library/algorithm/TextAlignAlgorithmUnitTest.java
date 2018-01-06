package net.danielpark.library.algorithm;

import net.danielpark.library.algorithm.model.Word;

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
        maxLengthAtLine = 18;
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

        text = "트리케라톱스는 백악기 후기의 조반목 각룡아목의 초식 공룡입니다. 몸길이는 6~9m이며 네 다리로 보행했습니다.";
        textStringAtDotResult = new String[]{"트리케라톱스는 백악기 후기의 조반목 각룡아목의 초식 공룡입니다.",
                "몸길이는 6~9m이며 네 다리로 보행했습니다."};

        createStringAtDot(text, textStringAtDotResult);

        text = "화성은 지구의 밖을 돌고 있는 첫 번째의 외행성이며 태양을 중심으로 4번째 궤도를 공전하는 태양계 행성입니다.";
        textStringAtDotResult = new String[]{
                "화성은 지구의 밖을 돌고 있는 첫 번째의 외행성이며 태양을 중심으로 4번째 궤도를 공전하는 태양계 행성입니다."};

        createStringAtDot(text, textStringAtDotResult);

        text = "인조는 남한산성에서 항전을 계속하지만 청나라가 너무 강했고, 이에 인조는 1637년 1월 30일 항복을 하게 됩니다.";
        textStringAtDotResult = new String[]{
                "인조는 남한산성에서 항전을 계속하지만 청나라가 너무 강했고, 이에 인조는 1637년 1월 30일 항복을 하게 됩니다."};

        createStringAtDot(text, textStringAtDotResult);
    }

    @Test
    public void calculateTest() {
        text = "인조는 남한산성에서 항전을 계속하지만 청나라가 너무 강했고, 이에 인조는 1637년 1월 30일 항복을 하게 됩니다.";
        calculate(text);

        text = "화성은 지구의 밖을 돌고 있는 첫 번째의 외행성이며 태양을 중심으로 4번째 궤도를 공전하는 태양계 행성입니다.";
        calculate(text);

        text = "트리케라톱스는 백악기 후기의 조반목 각룡아목의 초식 공룡이며, 몸길이는 6~9m이며 네 다리로 보행했습니다.";
        calculate(text);

        text = "트리케라톱스는 백악기 후기의 조반목 각룡아목의 초식 공룡입니다.";
        calculate(text);

        text = "트리케라톱스는 백악기 후기의 조반목 각룡아목의 초식 공룡입니다요.....................";
        calculate(text);

        text = "트리케라톱스는 백악기 후기의오에오에오에오에오에오에오에오에오에 조반목 각룡아목의 초식 공룡입니다요.....................";
        calculate(text);

    }

    private void calculate(String text) {
        long startTime = System.currentTimeMillis();

        textAlignAlgorithm.calculate(text);

        int key = 0;
        while (textAlignAlgorithm.wordData.get(key) != null) {

            for (Word w : textAlignAlgorithm.wordData.get(key)) {
                print(w.getWord() + " ");
            }
            key++;

            println("");
        }

        println("");
        println("Process time : " + (System.currentTimeMillis() - startTime) + " ms");
        println("");
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

    private void print(String s) {
        System.out.print(s);
    }

    private void println(String s) {
        System.out.println(s);
    }
}
