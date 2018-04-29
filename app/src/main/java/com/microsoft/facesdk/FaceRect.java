package com.microsoft.facesdk;

/**
 * Created by fashi on 2015/6/12.
 */
public class FaceRect {
    private int x;
    private int y;
    private int width;
    private int height;

    public FaceRect(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
