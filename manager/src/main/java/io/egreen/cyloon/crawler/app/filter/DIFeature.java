package io.egreen.cyloon.crawler.app.filter;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.internal.SystemDescriptor;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

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
        packages(true, "io.egreen.cyloon.crawler.app");
    }
}
