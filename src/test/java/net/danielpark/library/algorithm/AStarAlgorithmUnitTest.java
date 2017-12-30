package net.danielpark.library.algorithm;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Created by Daniel Park on 2017. 12. 29..
 */
@RunWith(RobolectricTestRunner.class)
public class AStarAlgorithmUnitTest {

    private Pair<Integer, Integer> columns;
    private Pair<Integer, Integer> startNode;
    private Pair<Integer, Integer> endNode;

    @Before
    public void setUp() {
        columns = new Pair<>(5, 5);
        startNode = new Pair<>(0, 0);
        endNode = new Pair<>(4, 4);
    }

    @Test
    public void testPairConstructor() throws Exception {
        Pair<String, Integer> pair = new Pair<>("a", 1);
        assertThat(pair.first, is("a"));
        assertThat(pair.second, is(1));
    }

    @Test
    public void proceed() {
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode);

        println("Start node : " + aStarAlgorithm.getStartNode().getxIndex()
                + "," + aStarAlgorithm.getStartNode().getyIndex());

        println("End node : " + aStarAlgorithm.getEndNode().getxIndex()
                + "," + aStarAlgorithm.getEndNode().getyIndex());

        println("Start node G : " + aStarAlgorithm.getStartNode().getG());
        println("Start node H : " + aStarAlgorithm.getStartNode().getH());
        println("Start node F : " + aStarAlgorithm.getStartNode().getF());

        assertThat(aStarAlgorithm.getNodeMap()[0].length, is(5));
    }

    private void println(String s) {
        System.out.println(s);
    }
}
