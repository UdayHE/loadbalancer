package io.github.udayhe.config;

import io.micronaut.context.ApplicationContext;
import io.micronaut.discovery.EmbeddedServerInstance;
import io.micronaut.discovery.ServiceInstance;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Singleton;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Singleton
public class ServiceInstanceProvider {

    public List<ServiceInstance> getServiceInstances() {
        return List.of(new EmbeddedServerInstance() {
            @Override
            public EmbeddedServer getEmbeddedServer() {
                return new EmbeddedServer() {
                    @Override
                    public boolean isRunning() {
                        return true;
                    }

                    @Override
                    public ApplicationContext getApplicationContext() {
                        return null;
                    }

                    @Override
                    public ApplicationConfiguration getApplicationConfiguration() {
                        return null;
                    }

                    @Override
                    public int getPort() {
                        return 8080;
                    }

                    @Override
                    public String getHost() {
                        return "localhost";
                    }

                    @Override
                    public String getScheme() {
                        return "http";
                    }

                    @Override
                    public URL getURL() {
                        try {
                            return new URL("http://localhost:8080/to-do");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException("Invalid URL for EmbeddedServer", e);
                        }
                    }

                    @Override
                    public URI getURI() {
                        try {
                            return new URI("http://localhost:8080/to-do/index.html");
                        } catch (URISyntaxException e) {
                            throw new RuntimeException("Invalid URI for EmbeddedServer", e);
                        }
                    }

                    @Override
                    public boolean isKeepAlive() {
                        return true;
                    }
                };
            }

            @Override
            public String getId() {
                return "id1";
            }

            @Override
            public URI getURI() {
                try {
                    return new URI("http://localhost:8080/to-do/index.html");
                } catch (URISyntaxException e) {
                    throw new RuntimeException("Invalid URI for ServiceInstance", e);
                }
            }
        });
    }



}
