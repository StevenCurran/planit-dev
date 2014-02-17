package com;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Steven on 17/02/14.
 */
public class PostgresConnector {


    public PostgresConnector(){

        try {
            Class.forName("org.postgresql.Driver");
            URI uri = new URI(ProjectUtils.getDBUrl());
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl("jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath());
            ds.setUsername(uri.getUserInfo().split(":")[0]);
            ds.setPassword(uri.getUserInfo().split(":")[1]);
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
