package com.addthis;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by sami on 3/26/15.
 */
public class KonnichiwaApplication extends Application<KonnichiwaConfiguration> {
    public static void main(String[] args) throws Exception {
        new KonnichiwaApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<KonnichiwaConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(KonnichiwaConfiguration configuration,
                    Environment environment) {

        final OAuthResource resource = new OAuthResource();
        environment.jersey().register(resource);
    }

}