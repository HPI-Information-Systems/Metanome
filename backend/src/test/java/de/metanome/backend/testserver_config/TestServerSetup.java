package de.metanome.backend.testserver_config;

import de.metanome.backend.api.ApplicationConfig;
import de.metanome.backend.initializer.DatabaseInitializer;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent on 12.12.16.
 */
public class TestServerSetup extends JerseyTest {


    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
        config.register(LoggingFilter.class);
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    //Servlet Deployment Context configured based on web.xml
    @Override
    protected DeploymentContext configureDeployment()
    {
        ResourceConfig rc = new ResourceConfig();
        rc.packages("de.metanome.backend.resources");
        rc.register(ApplicationConfig.class);
        rc.register(MultiPartFeature.class);

        return ServletDeploymentContext.forServlet(new ServletContainer(rc)).servletPath("/api")
                .addListener(DatabaseInitializer.class)
                .build();
    }

    @Ignore
    public void availableTest() {
        final String available = target("algorithms/test").request().get(String.class);
        System.out.println(available);
        assertEquals("Test Webserver is not working!","Hello World!", available);

    }
}
