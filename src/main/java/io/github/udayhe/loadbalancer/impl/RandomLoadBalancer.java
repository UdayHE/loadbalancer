package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Random;

@Singleton
public class RandomLoadBalancer implements CustomLoadBalancer {

    private final ServiceInstanceProvider serviceInstanceProvider;
    private final Random random = new Random();

    public RandomLoadBalancer(ServiceInstanceProvider serviceInstanceProvider) {
        this.serviceInstanceProvider = serviceInstanceProvider;
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        int index = random.nextInt(serviceInstanceProvider.getServiceInstances().size());
        ServiceInstance selectedInstance = serviceInstanceProvider.getServiceInstances().get(index);
        return Mono.just(selectedInstance);
    }

    @Override
    public String getType() {
        return LoadBalancerType.RANDOM.name();
    }
}
