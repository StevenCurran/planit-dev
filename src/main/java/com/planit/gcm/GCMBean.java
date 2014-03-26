package com.planit.gcm;

import com.google.android.gcm.server.Message;

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
    private String locaid = "APA91bFy1CG5D_BVNAfZRwIAd105Iws8jWK3WO_PrtuXKXwzzy4svlXK9ai0cKVQuZVMR4hEfcjhkY-rMwxk3Lu8xdkSRgBZ_b3HJUd9TUxT2IxJfFCZAc5Wm-9ntffsSjweXmJ3D6RT7EatnRonyE91YBtCy9LvIw";

    private GCMProxySender sender;
    private QuotaGuardProxyAuthenticator proxy;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);

    public GCMBean() {
        this.proxy = new QuotaGuardProxyAuthenticator();
        this.sender = new GCMProxySender(API_KEY, proxy);
    }

    public static void main(String[] args) {
        GCMBean b = new GCMBean();

        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", "Hello Gareth...lots of love Planit").build();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("APA91bEKC1HZuWh40W2tBU58SxzUTpkuipXczqH_6Q9dLLNRiYFlmA5ChvA37h0HHl0k2HTzKZNHJjnb3JkwqqVJZn8A-ymEnja_VPViMHfdxGxImyERp-W8wA9HG1aBy-bBqoJ3ORSSmIX1D0npR6QJfdVgEwFjjw");
        b.sendMessageToUsers(m, strings);
        System.out.println("Sent");

    }

    public void send(String name) {
        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", "hello there " + name + " Welcome to planit!").build();

        List<String> id = new ArrayList<>();
        id.add(locaid);
        try {
            proxy.setProxy();
            sender.send(m, id, 1);
            proxy.clearProxy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent");

    }

    public void sendRegConfirm(String name, String deviceId) {
        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", "Device registered with planit. " + name).build();

        List<String> id = new ArrayList<>();
        id.add(deviceId);
        System.out.println("Sending to: " + id.toString());
        try {
            proxy.setProxy();
            sender.send(m, id, 1);
            proxy.clearProxy();
        } catch (IOException e) {
            System.out.println("Registration error...."); //log
            e.printStackTrace();
        }

    }

    public void sendMessageToUsers(Message m, List<String> deviceIds) {
        try {
            proxy.setProxy();
            sender.send(m, deviceIds, 1);
            proxy.clearProxy();
        } catch (IOException e) {
            System.out.println("Registration error...."); //log
            e.printStackTrace();
        }


    }


}
