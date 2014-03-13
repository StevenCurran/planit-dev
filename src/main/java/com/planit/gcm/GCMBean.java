package com.planit.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Steven on 13/03/14.
 */
public class GCMBean {

    private final String API_KEY = "AIzaSyA0zAyEc47gZn1tpM6luIa4OjflCEeNd84";
    private final String PROJECT_NUMBER = "115023261213";

    private Sender sender;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);

    public GCMBean() {
        this.sender = new Sender(API_KEY);
        System.out.println("Sender created");
        Message m = new Message.Builder().addData("envets", "10").build();


    }


}
