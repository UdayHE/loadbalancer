package io.github.udayhe.config;

import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Singleton
public class ServiceInstanceProvider {

    @Singleton
    public List<ServiceInstance> serviceInstances() {
        return List.of(new ServiceInstance() {
            @Override
            public String getId() {
                return "id1";
            }

            @Override
            public URI getURI() {
                try {
                    return new URI("http://localhost:8080/to-do");
                } catch (URISyntaxException e) {
                    throw new RuntimeException("Invalid URI for ServiceInstance", e);
                }
            }
        });
    }


}
