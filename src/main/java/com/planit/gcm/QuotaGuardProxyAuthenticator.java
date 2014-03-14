package com.planit.gcm;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class QuotaGuardProxyAuthenticator extends Authenticator{
    private String user, password, host;
    private int port;
    private ProxyAuthenticator auth;

    public QuotaGuardProxyAuthenticator() {
        //String proxyUrlEnv = System.getenv("QUOTAGUARDSTATIC_URL");
        String proxyUrlEnv = "http://quotaguard1124:345f4549b71f@eu-west-1-babbage.quotaguard.com:9293";
        System.out.println(proxyUrlEnv);
        if(proxyUrlEnv!=null){
            try {
                URL proxyUrl = new URL(proxyUrlEnv);
                String authString = proxyUrl.getUserInfo();
                user = authString.split(":")[0];
                password = authString.split(":")[1];
                host = proxyUrl.getHost();
                port = proxyUrl.getPort();
                auth = new ProxyAuthenticator(user,password);
                setProxy();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.err.println("You need to set the environment variable QUOTAGUARDSTATIC_URL!");
        }

    }

    private void setProxy(){
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", String.valueOf(port));
        System.setProperty("https.proxyHost",host);
        System.setProperty("https.proxyPort", String.valueOf(port));
    }

    public String getEncodedAuth(){
        //If using Java8 you can use the standard java.util.Base64 package.
        //String encoded = java.util.Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
        String encoded = new String(Base64.encodeToByte((user + ":" + password).getBytes(), true));
        return encoded;
    }

    public ProxyAuthenticator getAuth(){
        return auth;
    }

    class ProxyAuthenticator extends Authenticator {

        private String user, password;

        public ProxyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }

}