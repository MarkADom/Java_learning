package com.marco.microservices.limitsservices.bean;

public class LimitConfiguration {


    private int maximum;
    private int minimum;

    public LimitConfiguration() {
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public LimitConfiguration(int maximum, int minimum) {
        this.maximum = maximum;
        this.minimum = minimum;
    }


}