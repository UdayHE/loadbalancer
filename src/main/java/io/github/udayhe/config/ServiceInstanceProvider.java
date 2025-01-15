package io.github.udayhe.config;

import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Singleton
public class ServiceInstanceProvider {

//    public List<ServiceInstance> getServiceInstances() {
//        return List.of(new EmbeddedServerInstance() {
//            @Override
//            public EmbeddedServer getEmbeddedServer() {
//                return new EmbeddedServer() {
//                    @Override
//                    public boolean isRunning() {
//                        return true;
//                    }
//
//                    @Override
//                    public ApplicationContext getApplicationContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public ApplicationConfiguration getApplicationConfiguration() {
//                        return null;
//                    }
//
//                    @Override
//                    public int getPort() {
//                        return 8080;
//                    }
//
//                    @Override
//                    public String getHost() {
//                        return "localhost";
//                    }
//
//                    @Override
//                    public String getScheme() {
//                        return "http";
//                    }
//
//                    @Override
//                    public URL getURL() {
//                        try {
//                            return new URL("http://localhost:8080/to-do");
//                        } catch (MalformedURLException e) {
//                            throw new RuntimeException("Invalid URL for EmbeddedServer", e);
//                        }
//                    }
//
//                    @Override
//                    public URI getURI() {
//                        try {
//                            return new URI("http://localhost:8080/to-do");
//                        } catch (URISyntaxException e) {
//                            throw new RuntimeException("Invalid URI for EmbeddedServer", e);
//                        }
//                    }
//
//                    @Override
//                    public boolean isKeepAlive() {
//                        return true;
//                    }
//                };
//            }
//
//            @Override
//            public String getId() {
//                return "id1";
//            }
//
//            @Override
//            public URI getURI() {
//                try {
//                    return new URI("http://localhost:8080/to-do");
//                } catch (URISyntaxException e) {
//                    throw new RuntimeException("Invalid URI for ServiceInstance", e);
//                }
//            }
//        });
//    }

    private final List<ServiceInstance> serviceInstances;

    public ServiceInstanceProvider(ServiceConfiguration serviceConfiguration) {
        this.serviceInstances = serviceConfiguration.getInstances().stream()
                .map(config -> new CustomServiceInstance(config.getId(), config.getUri()))
                .collect(toList());
    }

}
