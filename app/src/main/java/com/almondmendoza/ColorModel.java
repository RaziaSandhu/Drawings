package com.almondmendoza;

/**
 * Created by new on 17/3/2017.
 */

public class ColorModel {

    int Hue;
    int Saturation;

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public int getSaturation() {
        return Saturation;
    }

    public void setSaturation(int saturation) {
        Saturation = saturation;
    }

    public int getHue() {
        return Hue;
    }

    public void setHue(int hue) {
        Hue = hue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    int Value;
    int color;
    String colorname;
}
