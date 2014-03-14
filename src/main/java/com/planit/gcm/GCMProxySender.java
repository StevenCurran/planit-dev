package com.planit.gcm;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.gcm.server.Sender;

public class GCMProxySender extends Sender {
    private QuotaGuardProxyAuthenticator proxy;

    public GCMProxySender(String key, QuotaGuardProxyAuthenticator authenticator) {
        super(key);
        this.proxy = authenticator;
    }

    @Override

    protected HttpURLConnection getConnection(String url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Proxy-Authorization", "Basic " + proxy.getEncodedAuth());
        Authenticator.setDefault(proxy.getAuth());

        return conn;

    }
}