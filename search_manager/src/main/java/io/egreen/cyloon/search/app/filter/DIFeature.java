package io.egreen.cyloon.search.app.filter;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by dewmal on 10/15/15.
 */
public class DIFeature extends ResourceConfig {
    public DIFeature() {

        register(new AbstractBinder() {
            @Override
            protected void configure() {


            }
        });
        packages(true, "io.egreen.cyloon.search.app");
    }
}
