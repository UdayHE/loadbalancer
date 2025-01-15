package io.github.udayhe.config;

import io.micronaut.discovery.ServiceInstance;
import lombok.Getter;

import java.net.URI;

public class CustomServiceInstance implements ServiceInstance {

    private final String id;
    private final URI uri;

    public CustomServiceInstance(String id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public URI getURI() {
        return uri;
    }
}
