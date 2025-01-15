package io.github.udayhe.config;

import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Singleton
public class ServiceInstanceProvider {

    private final List<ServiceInstance> serviceInstances;

    public ServiceInstanceProvider(ServiceConfiguration serviceConfiguration) {
        this.serviceInstances = serviceConfiguration.getInstances().stream()
                .map(config -> new CustomServiceInstance(config.getId(), config.getUri()))
                .collect(toList());
    }

}
