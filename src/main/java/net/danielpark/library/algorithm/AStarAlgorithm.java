package net.danielpark.library.algorithm;

import android.support.annotation.VisibleForTesting;
import android.util.Pair;

import net.danielpark.library.algorithm.model.MazeNode;
import net.danielpark.library.log.Logger;

import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 28..
 */

public class AStarAlgorithm implements Algorithm {
    private final String TAG = AStarAlgorithm.class.getSimpleName();

    private MazeNode[][] nodeMap;
    private MazeNode startNode, endNode;

    private int rowCount, columnCount;

    private MazeNode currentNode;

    @VisibleForTesting()
    ArrayList<MazeNode> openList = new ArrayList<>();

    @VisibleForTesting()
    ArrayList<MazeNode> closedList = new ArrayList<>();

    @VisibleForTesting
    ArrayList<MazeNode> obstacleList = new ArrayList<>();


    public AStarAlgorithm(int rowCounts, int columnCounts,
                          Pair<Integer, Integer> startIndex,
                          Pair<Integer, Integer> endIndex) {
        this(rowCounts, columnCounts, startIndex, endIndex, null);
    }

    public AStarAlgorithm(int rowCounts, int columnCounts,
                          Pair<Integer, Integer> startIndex,
                          Pair<Integer, Integer> endIndex,
                          ArrayList<Pair<Integer, Integer>> obstacles) {

        this.rowCount = rowCounts;
        this.columnCount = columnCounts;

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

                // add obstacles to obstacleList.
                obstacleList.add(nodeMap[data.first][data.second]);
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

    @Override
    public void build() {
        if (currentNode == null)
            currentNode = startNode;

        loop(currentNode);
    }

    void loop(MazeNode currentNode) {
        closedList.add(currentNode);

        search(currentNode);

        int targetIndex = -1;
        double h = 0, f = 0;
        for (int i = 0; i < openList.size(); i++) {
            if (targetIndex == -1) {
                targetIndex = 0;
                h = openList.get(i).getH();
                f = openList.get(i).getF();
            }
            else if (openList.get(i).getF() < f
                    // TODO: when you check next node, compare H
                    || (openList.get(i).getF() == f && openList.get(i).getH() < h)) {
                targetIndex = i;
                h = openList.get(i).getH();
                f = openList.get(i).getF();
            }
        }

        // TODO: if openList's size == 0, no nodes found.
        if (openList.isEmpty()) {
            Logger.i(TAG, "No nodes found. Terminate!");
            return;
        }

        MazeNode targetNode = openList.get(targetIndex);
        openList.remove(targetIndex);
//        openList.clear();

        if (targetNode.getxIndex() == endNode.getxIndex()
                && targetNode.getyIndex() == endNode.getyIndex()) {
            closedList.add(targetNode);
            Logger.d(TAG, "Get to End node!");
            return;
        }

        loop(targetNode);
    }

    /**
     * Search all nodes around the current node.
     * @param currentNode
     */
    @VisibleForTesting()
    void search(MazeNode currentNode) {
        int x = currentNode.getxIndex();
        int y = currentNode.getyIndex();

        // search all nodes around the current node.
        int xDirection = -1;
        int yDirection = -1;

        while (xDirection <= 1 && yDirection <= 1) {

            // Except for myself node.
            if (xDirection == 0 && yDirection == 0) {
                xDirection++;
                continue;
            }

            register(currentNode, x + xDirection, y + yDirection);

            if (xDirection == 1 && yDirection == 1) {
                // terminate
                break;
            }
            else if (xDirection == 1) {
                xDirection = -1;
                yDirection++;
            }
            else {
                xDirection++;
            }
        }
    }

    @VisibleForTesting()
    MazeNode register(MazeNode preNode, int x, int y) {
        if (x < 0 || y < 0 || x >= rowCount || y >= columnCount) return null;

        MazeNode node = nodeMap[x][y];

        if (closedList.contains(node) || obstacleList.contains(node))
            return null;

        // TODO: before set previous node, you need to check which previous node is better.
        if (node.hasPreviousNode()) {
            MazeNode ppNode = node.getPreviousNode();
            if (ppNode.getF() > preNode.getF()
                    // TODO: when you check previous node, compare to G!
//                    || (ppNode.getF() == preNode.getF() && ppNode.getH() > preNode.getH())) {
                    || (ppNode.getF() == preNode.getF() && ppNode.getG() > preNode.getG())) {
                node.setPreviousNode(preNode);
            }
        } else {
            node.setPreviousNode(preNode);
        }
        node.setEndNode(endNode);

        // Add node to open list, if it's not the obstacle...
        if (!node.isObstacle()) {
            // If there was same node in open list, remove old one and add new one.
            if (openList.contains(node)) {
                openList.remove(node);
            }
            openList.add(node);
        }

        return node;
    }
}
