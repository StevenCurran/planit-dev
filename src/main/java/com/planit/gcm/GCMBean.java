package com.planit.gcm;

import com.google.android.gcm.server.Message;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Steven on 13/03/14.
 */
public class GCMBean {

    private final String API_KEY = "AIzaSyA0zAyEc47gZn1tpM6luIa4OjflCEeNd84";
    private String locaid = "APA91bFACSU7tHHlg1R4n5Pbb9K3TQiJln4vFHNATjTHHS19-uOk5WpuIWbZs13SaerMrTDe_6bq4ZTVrb08V2QCSDfRAdT46QXsdsO5oQnvuw3G3l1sCKe4m_0KCLixyVn3t8DC-ccJxwc5jzk_LL1JM5-gggaimQ";

    private GCMProxySender sender;
    private QuotaGuardProxyAuthenticator proxy;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);

    public GCMBean() {
        this.proxy = new QuotaGuardProxyAuthenticator();
        this.sender = new GCMProxySender(API_KEY, proxy);
    }

    public static void main(String[] args){
        GCMBean b = new GCMBean();

        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", "Hello Gareth...lots of love Planit").build();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("APA91bFGr2W6t9xDL1BmtPrgaixqw9Ea0tLcU36AeLzASdoYAmswtpaSdT32qJVbZ72YRV94piVxkt3N94DrcyO13xJ7gLAvEjAuWzIFChzygRphlrji9eIjtFa7PT3PzjjqmJlgp6PKtuJVEw4SGRWWucaqH-S5tQ");
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
