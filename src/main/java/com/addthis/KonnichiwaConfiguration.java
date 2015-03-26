package com.addthis;

import io.dropwizard.Configuration;

/**
 * Created by sami on 3/26/15.
 */
public class KonnichiwaConfiguration extends Configuration {

    private String hello;


    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
