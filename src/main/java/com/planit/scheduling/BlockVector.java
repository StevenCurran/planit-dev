package com.planit.scheduling;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh on 26/02/14.
 */
public class BlockVector {
    private float[] vector;
    public static final int dimension = 3;
    private List<String> eventIds;

    public BlockVector(String id, float priority, float extraAttendees, float uPref) {
        eventIds = new LinkedList<String>();
        eventIds.add(id);
        vector = new float[dimension];
        vector[0] = priority;
        vector[1] = extraAttendees;
        vector[2] = uPref;
    }

    public BlockVector(List<String> ids, float priority, float extraAttendees, float uPref)
    {
        this(ids.get(0), priority,extraAttendees,uPref);
        for (String id : ids.subList(1,-1))
        {
            eventIds.add(id);
        }
    }


    public BlockVector() {
        this("", 0.0f, 0.0f, 0.0f);
    }

    public float getAtIndex(int i) {
        return vector[i];
    }

    public List<String> getIds() {
        return eventIds;
    }

    public void addEventId(String id) {
        eventIds.add(id);
    }

    public void display() {
        System.out.println("===");
        for (int i = 0; i < dimension; i++) {
            System.out.println(vector[i]);
        }
        System.out.println("===");
    }

    public void add(BlockVector bv)
    {
        for (int i = 0; i < dimension; i++)
        {
            this.vector[i]+=bv.vector[i];
        }

        for(String thisEventId : bv.eventIds){
            if(!eventIds.contains(thisEventId))
            {
                eventIds.add(thisEventId);
            }
        }
    }


}
