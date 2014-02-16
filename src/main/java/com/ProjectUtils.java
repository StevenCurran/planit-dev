package com;

/**
 * Created by Steven on 16/02/14.
 */
public class ProjectUtils {

    private static final String LOCAL_ADDRESS= "http://localhost:8080";
    private static final String HEROKU_ADDRESS= "http://planit-dev.herokuapp.com";

    public static String getRuntimeEnvironment(){
        String var = System.getenv("runtime_environment");
        if(var == null){
            return System.getProperty("runtime_environment");
        }
        else
            return var;
    }

    public static String getBaseUrl(){
        return getRuntimeEnvironment().equals("local") ? LOCAL_ADDRESS : HEROKU_ADDRESS;
    }
}
