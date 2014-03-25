package com.planit.scheduling;

/**
 * Created by Josh on 26/02/14.
 */
public class BlockVector {
    private float[] vector;
    public static final int dimension = 3;
    private String eventId;

    public BlockVector(String id, float priority, float extraAttendees, float uPref) {
        eventId = id;
        vector = new float[dimension];
        vector[0] = priority;
        vector[1] = extraAttendees;
        vector[2] = uPref;
    }

    public BlockVector() {
        this(null, 0.0f, 0.0f, 0.0f);
    }

    public float getAtIndex(int i) {
        return vector[i];
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
