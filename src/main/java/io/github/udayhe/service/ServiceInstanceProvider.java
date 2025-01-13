package io.github.udayhe.service;

import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Singleton
public class ServiceInstanceProvider {
    public List<ServiceInstance> getInstances() {
        return Arrays.asList(
            ServiceInstance.of("localhost:8081", URI.create("http://localhost:8081")),
            ServiceInstance.of("localhost:8082", URI.create("http://localhost:8082")),
            ServiceInstance.of("localhost:8083", URI.create("http://localhost:8083"))
        );
    }
}
