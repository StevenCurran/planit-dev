package com.planit.scheduling;

/**
 * Created by Josh on 26/02/14.
 */
public class BlockVector {
    private int[] vector;
    public static final int dimension = 2;
    private String eventId;

    public BlockVector(String id, int priority, int extraAttendees) {
        eventId = id;
        vector = new int[dimension];
        vector[0] = priority;
        vector[1] = extraAttendees;
    }

    public BlockVector() {
        this(null, 0, 0);
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

    public String getId() {
        return eventId;
    }

    public void setEventId(String id) {
        eventId = id;
    }


    public void display() {
        System.out.println("===");
        for (int i = 0; i < dimension; i++) {
            System.out.println(vector[i]);
        }
        System.out.println("===");
    }


}
