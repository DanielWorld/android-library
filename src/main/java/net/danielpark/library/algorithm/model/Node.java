package net.danielpark.library.algorithm.model;

/**
 * Created by Daniel Park on 2017. 12. 28..
 */

public class Node {

    protected final int xIndex;         // Array x index
    protected final int yIndex;         // Array y index

    protected final float centerX;      // Center x coordinate
    protected final float centerY;      // Center y coordinate

    protected float width;              // Node's width
    protected float height;             // Node's height

    public Node(int xIndex, int yIndex, float width, float height) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;

        this.width = width;
        this.height = height;

        this.centerX = xIndex * width + width / 2;
        this.centerY = yIndex * height + height / 2;
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
