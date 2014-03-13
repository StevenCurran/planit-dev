package com.planit.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Steven on 13/03/14.
 */
public class GCMBean {

    private final String API_KEY = "AIzaSyA0zAyEc47gZn1tpM6luIa4OjflCEeNd84";
    private String locaid = "APA91bFvE-3Q_SZckFQOqtx3DMyzKms1Ch1oEv3Tazmpu3LHNii7WM9NNFDYWFldJHLi3gRQhMfEKzb0jkh1IrxmpsFX3GOwKgtotpnjOE7hD0t5wGbu3rDCDYskzQ8ojfz2ZhTcuZmFaLYFpHuGeJEpidrFoW4kVw";

    private Sender sender;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);

    public GCMBean() {
        this.sender = new Sender(API_KEY);
        System.out.println("Sender created");

    }

    public void send(String name){
        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", "hello there " + name + " Welcome to planit!").build();

        List<String> id = new ArrayList<>();
        id.add(locaid);
        try {
            sender.send(m, id, 10);
        } catch (IOException e) {
            System.err.print("WTF HAPPENED");
            e.printStackTrace();
        }

    }


}
