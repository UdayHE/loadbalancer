package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Singleton
public class RandomLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> serviceInstances;
    private final Random random = new Random();

    public RandomLoadBalancer(List<ServiceInstance> instances) {
        this.serviceInstances = instances;
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        int index = random.nextInt(serviceInstances.size());
        ServiceInstance selectedInstance = serviceInstances.get(index);
        return Mono.just(selectedInstance);
    }

    @Override
    public String getType() {
        return LoadBalancerType.RANDOM.name();
    }
}
