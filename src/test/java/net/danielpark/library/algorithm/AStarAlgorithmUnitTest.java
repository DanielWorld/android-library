package net.danielpark.library.algorithm;

import android.util.Pair;

import net.danielpark.library.algorithm.model.MazeNode;

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

    private AStarAlgorithm aStarAlgorithm;

    @Before
    public void setUp() {
        columns = new Pair<>(25, 25);
        startNode = new Pair<>(0, 0);
        endNode = new Pair<>(14, 20);

        aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode);
    }

    @Test
    public void testPairConstructor() throws Exception {
        Pair<String, Integer> pair = new Pair<>("a", 1);
        assertThat(pair.first, is("a"));
        assertThat(pair.second, is(1));
    }

    @Test
    public void proceed() {
        println("Start node : " + aStarAlgorithm.getStartNode().getxIndex()
                + "," + aStarAlgorithm.getStartNode().getyIndex());

        println("End node : " + aStarAlgorithm.getEndNode().getxIndex()
                + "," + aStarAlgorithm.getEndNode().getyIndex());

        println("Start node G : " + aStarAlgorithm.getStartNode().getG());
        println("Start node H : " + aStarAlgorithm.getStartNode().getH());
        println("Start node F : " + aStarAlgorithm.getStartNode().getF());

        assertThat(aStarAlgorithm.getNodeMap()[0].length, is(5));
    }

    @Test
    public void search() {
        aStarAlgorithm.search(aStarAlgorithm.getStartNode());

        for (MazeNode mazeNode : aStarAlgorithm.openList) {
            println("search node : " + mazeNode.getxIndex()
                    + "," + mazeNode.getyIndex());

            println("search node G : " + mazeNode.getG());
            println("search node H : " + mazeNode.getH());
            println("search node F : " + mazeNode.getF());
            println("");
        }
    }

    @Test
    public void build() {
        long startTime = System.currentTimeMillis();

        aStarAlgorithm.build();

        for (MazeNode mazeNode : aStarAlgorithm.closedList) {
            println("final node : " + mazeNode.getxIndex()
                    + "," + mazeNode.getyIndex());

            println("final node G : " + mazeNode.getG());
            println("final node H : " + mazeNode.getH());
            println("final node F : " + mazeNode.getF());
            println("");
        }

        long endTime = System.currentTimeMillis();
        println("process time : " + (endTime - startTime) + " ms");
    }

    private void println(String s) {
        System.out.println(s);
    }
}
