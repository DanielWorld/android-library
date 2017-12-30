package net.danielpark.library.algorithm;

import android.util.Pair;

import net.danielpark.library.algorithm.model.MazeNode;

import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 28..
 */

public class AStarAlgorithm {

    private MazeNode[][] nodeMap;
    private MazeNode startNode, endNode;

    public AStarAlgorithm(int rowCounts, int columnCounts,
                          Pair<Integer, Integer> startIndex,
                          Pair<Integer, Integer> endIndex) {
        this(rowCounts, columnCounts, startIndex, endIndex, null);
    }

    public AStarAlgorithm(int rowCounts, int columnCounts,
                          Pair<Integer, Integer> startIndex,
                          Pair<Integer, Integer> endIndex,
                          ArrayList<Pair<Integer, Integer>> obstacles) {
        // create node map
        nodeMap = new MazeNode[rowCounts][columnCounts];

        for (int x = 0; x < rowCounts; x++) {
            for (int y = 0; y < columnCounts; y++) {
                nodeMap[x][y] = new MazeNode(x, y);
            }
        }

        // set start / end node
        startNode = nodeMap[startIndex.first][startIndex.second];
        endNode = nodeMap[endIndex.first][endIndex.second];

        startNode.setEndNode(endNode);

        // set obstacles
        if (obstacles != null) {
            for (Pair<Integer, Integer> data : obstacles) {
                nodeMap[data.first][data.second].setObstacle(true);
            }
        }
    }

    public MazeNode[][] getNodeMap() {
        return nodeMap;
    }

    public MazeNode getStartNode() {
        return startNode;
    }

    public MazeNode getEndNode() {
        return endNode;
    }
}
