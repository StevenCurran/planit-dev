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



    public static String getDBUrl(){
        if(System.getenv("DATABASE_URL") != null){
            return System.getenv("DATABASE_URL");
        }
        else{
            return "postgres://xzfgairvikdtkm:69vM_uwc7OUjwGOmpUgWufasTD@ec2-54-247-107-140.eu-west-1.compute.amazonaws.com:5432/d1vg2rmk0eal7p";
        }
    }
}
