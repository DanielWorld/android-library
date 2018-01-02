package net.danielpark.library.algorithm.model;

/**
 * Created by Daniel Park on 2017. 12. 28..
 */

public class MazeNode extends Node {

    private MazeNode previousNode;      // the node which the current node is directed to.

    private double G = 0;               // the distance between start node and current node. (the cost so far)
    private double H = 0;               // the distance between end node and current node. (heuristic)

    private boolean isObstacle;         // is the obstacle?
    private final int diagonal;         // = root(width^2 + height^2)

    public MazeNode(int xIndex, int yIndex) {
        super(xIndex, yIndex, 10, 10);
        diagonal = 14;
    }

    public MazeNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(MazeNode previousNode) {
        this.previousNode = previousNode;
        this.calculateG();
    }

    public boolean hasPreviousNode() {
        return previousNode != null;
    }

    public void setEndNode(MazeNode endNode) {
        this.calculateH(endNode);
    }

    private void calculateG() {
        // @namgyu.park (2017-12-29) : if Previous Node exists, this Node isn't root.
        if (previousNode != null) {
            double prevG = previousNode.G;

            // this Node is not root & which is positioned at diagonal way.
            if (previousNode.xIndex - xIndex != 0 && previousNode.yIndex - yIndex != 0) {
                G = prevG + diagonal;
            }
            // this Node is not root & which is positioned at X align way.
            else if (previousNode.xIndex - xIndex != 0){
                G = prevG + width;
            }
            // this Node is not root & which is positioned at Y align way.
            else if (previousNode.yIndex - yIndex != 0) {
                G = prevG + height;
            }
            // this Node is root (But no way to enter this scope!)
            else {
                G = prevG;
            }
        }
    }

    /**
     * Note that if you use a non informative heuristic function: h(v) = 0 for each v:
     * you get that A* chooses which vertex to develop next according to the "so far cost" (g(v)) only,
     * same as Dijkstra's algorithm -
     * [so if h(v) = 0, A* defaults to Dijkstra's Algorithm.]
     * @param endNode
     */
    private void calculateH(MazeNode endNode) {
        if (endNode != null) {
            int xIndexGap = Math.abs(endNode.xIndex - xIndex);
            int yIndexGap = Math.abs(endNode.yIndex - yIndex);

            // check it might be possible to have diagonal node.
            int diagonalCount = 0;
            while (xIndexGap > 0 && yIndexGap > 0) {
                xIndexGap--;
                yIndexGap--;
                diagonalCount++;
            }

            H = diagonal * diagonalCount + xIndexGap * width + yIndexGap * height;
        }
    }

    public double getG() {
        return G;
    }

    public double getH() {
        return H;
    }

    public double getF() {
        return G + H;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }
}
