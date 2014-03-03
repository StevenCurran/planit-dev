package com.planit.scheduling;

/**
 * Created by Josh on 26/02/14.
 */
public class BlockVector {
    private int[] vector;
    public static final int dimension = 2;

    public BlockVector(int priority, int extraAttendees) {
        vector = new int[dimension];
        vector[0] = priority;
        vector[1] = extraAttendees;
    }

    public BlockVector()
    {
        this(0,0);
    }


    public int getAtIndex(int i) {
        return vector[i];
    }

    public int getPriority() {
        return vector[0];
    }

    public int getExtraAttendees() {
        return vector[1];
    }

    public void display() {
        System.out.println("===");
        for (int i = 0; i < dimension; i++) {
            System.out.println(vector[i]);
        }
        System.out.println("===");
    }


}
