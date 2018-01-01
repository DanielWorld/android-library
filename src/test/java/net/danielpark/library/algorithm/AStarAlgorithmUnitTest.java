package net.danielpark.library.algorithm;

import android.util.Pair;

import net.danielpark.library.algorithm.model.MazeNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

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
    private ArrayList<Pair<Integer, Integer>> obstacles;

    private AStarAlgorithm aStarAlgorithm;

    @Before
    public void setUp() {
        columns = new Pair<>(6, 8);
        startNode = new Pair<>(1, 7);
        endNode = new Pair<>(0, 1);
        obstacles = new ArrayList<>();

        obstacles.add(new Pair<>(4, 0));
        obstacles.add(new Pair<>(1, 1));
        obstacles.add(new Pair<>(1, 2));
        obstacles.add(new Pair<>(2, 2));
        obstacles.add(new Pair<>(4, 2));
        obstacles.add(new Pair<>(4, 3));
        obstacles.add(new Pair<>(0, 4));
        obstacles.add(new Pair<>(1, 4));
        obstacles.add(new Pair<>(2, 4));
        obstacles.add(new Pair<>(3, 4));
        obstacles.add(new Pair<>(4, 4));
        obstacles.add(new Pair<>(2, 6));
        obstacles.add(new Pair<>(3, 6));

        aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode, obstacles);
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
        println("");

        assertThat(aStarAlgorithm.getNodeMap()[0].length, is(8));
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
    public void buildTest1() {
        columns = new Pair<>(6, 8);
        startNode = new Pair<>(1, 7);
        endNode = new Pair<>(0, 1);
        obstacles = new ArrayList<>();

        obstacles.add(new Pair<>(4, 0));
        obstacles.add(new Pair<>(1, 1));
        obstacles.add(new Pair<>(1, 2));
        obstacles.add(new Pair<>(2, 2));
        obstacles.add(new Pair<>(4, 2));
        obstacles.add(new Pair<>(4, 3));
        obstacles.add(new Pair<>(0, 4));
        obstacles.add(new Pair<>(1, 4));
        obstacles.add(new Pair<>(2, 4));
        obstacles.add(new Pair<>(3, 4));
        obstacles.add(new Pair<>(4, 4));
        obstacles.add(new Pair<>(2, 6));
        obstacles.add(new Pair<>(3, 6));

        aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode, obstacles);

        build();
    }

    @Test
    public void buildTest2() {
        columns = new Pair<>(5, 5);
        startNode = new Pair<>(0, 0);
        endNode = new Pair<>(4, 4);
        obstacles = new ArrayList<>();

        obstacles.add(new Pair<>(0, 3));
        obstacles.add(new Pair<>(1, 3));
        obstacles.add(new Pair<>(2, 3));
        obstacles.add(new Pair<>(3, 3));
        obstacles.add(new Pair<>(4, 3));

        aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode, obstacles);

        build();
    }

    @Test
    public void buildTest3() {
        columns = new Pair<>(6, 6);
        startNode = new Pair<>(4, 0);
        endNode = new Pair<>(2, 4);
        obstacles = new ArrayList<>();

        obstacles.add(new Pair<>(1, 1));
        obstacles.add(new Pair<>(3, 1));
        obstacles.add(new Pair<>(4, 1));
        obstacles.add(new Pair<>(5, 1));
        obstacles.add(new Pair<>(4, 2));
        obstacles.add(new Pair<>(1, 3));
        obstacles.add(new Pair<>(2, 3));
        obstacles.add(new Pair<>(3, 3));
        obstacles.add(new Pair<>(1, 4));

        aStarAlgorithm = new AStarAlgorithm(
                columns.first, columns.second, startNode, endNode, obstacles);

        build();
    }

    private void build() {
        long startTime = System.currentTimeMillis();

        aStarAlgorithm.build();

        long endTime = System.currentTimeMillis();
        println("process build time : " + (endTime - startTime) + " ms");
        println("");

        for (MazeNode mazeNode : aStarAlgorithm.closedList) {
            println("checked node : " + mazeNode.getxIndex()
                    + "," + mazeNode.getyIndex());

            println("checked node G : " + mazeNode.getG());
            println("checked node H : " + mazeNode.getH());
            println("checked node F : " + mazeNode.getF());
            println("");
        }

        MazeNode endNode = aStarAlgorithm.getEndNode();
        while (endNode.hasPreviousNode()) {
            println("final node : " + endNode.getxIndex()
                    + "," + endNode.getyIndex());

            println("final node G : " + endNode.getG());
            println("final node H : " + endNode.getH());
            println("final node F : " + endNode.getF());
            println("");

            endNode = endNode.getPreviousNode();
        }

        println("final node : " + endNode.getxIndex()
                + "," + endNode.getyIndex());

        println("final node G : " + endNode.getG());
        println("final node H : " + endNode.getH());
        println("final node F : " + endNode.getF());
        println("");
    }

    private void println(String s) {
        System.out.println(s);
    }
}
