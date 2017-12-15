package net.danielpark.library.util;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Daniel Park on 2017. 12. 8..
 */

@TargetApi(16)
public class GradientManager {

    @ColorInt
    private final int defaultColor = 0xff000000;
    private final float defaultCenterXY = 0.5f;

    /*public enum Orientation {
        *//** draw the gradient from the top to the bottom *//*
        TOP_BOTTOM,
        *//** draw the gradient from the top-right to the bottom-left *//*
        TR_BL,
        *//** draw the gradient from the right to the left *//*
        RIGHT_LEFT,
        *//** draw the gradient from the bottom-right to the top-left *//*
        BR_TL,
        *//** draw the gradient from the bottom to the top *//*
        BOTTOM_TOP,
        *//** draw the gradient from the bottom-left to the top-right *//*
        BL_TR,
        *//** draw the gradient from the left to the right *//*
        LEFT_RIGHT,
        *//** draw the gradient from the top-left to the bottom-right *//*
        TL_BR,
    }*/
            @NonNull
    private GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;

    @ColorInt
    private Integer startColor = null;

    @ColorInt
    private Integer centerColor = null;

    @ColorInt
    private Integer endColor = null;

    private float centerX, centerY;

    @NonNull
    public GradientDrawable.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(@NonNull GradientDrawable.Orientation orientation) {
        this.orientation = orientation;
    }

    public void setStartColor(String hexColor) {
        this.startColor = Color.parseColor(hexColor.startsWith("#") ? hexColor : "#" + hexColor);
    }

    /**
     * Only center color allow null
     * @param hexColor
     */
    public void setCenterColor(String hexColor) {
        if (StringUtil.isNullorEmpty(hexColor)) {
            this.centerColor = null;
        } else {
            this.centerColor = Color.parseColor(hexColor.startsWith("#") ? hexColor : "#" + hexColor);
        }
    }

    public void setEndColor(String hexColor) {
        this.endColor = Color.parseColor(hexColor.startsWith("#") ? hexColor : "#" + hexColor);
    }

    public float getCenterX() {
        if (centerX < 0 || centerX > 1)
            return defaultCenterXY;
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        if (centerY < 0 || centerY > 1)
            return defaultCenterXY;
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void build(View view) {
        GradientDrawable gd = new GradientDrawable(orientation, buildColors());

        gd.mutate();
        gd.setGradientCenter(getCenterX(), getCenterY());

        view.setBackground(gd);
    }

    private int[] buildColors() {
        ArrayList<Integer> colors = new ArrayList<>();

        if (startColor != null)
            colors.add(startColor);
        else
            colors.add(defaultColor);

        if (centerColor != null)
            colors.add(centerColor);

        if (endColor != null)
            colors.add(endColor);
        else
            colors.add(defaultColor);

        int[] createdColors = new int[colors.size()];
        for (int index = 0; index < colors.size(); index++) {
            createdColors[index] = colors.get(index);
        }

        return createdColors;
    }
}
