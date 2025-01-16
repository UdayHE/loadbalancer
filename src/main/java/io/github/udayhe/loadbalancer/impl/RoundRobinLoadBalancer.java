package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Iterator;

@Singleton
public class RoundRobinLoadBalancer implements CustomLoadBalancer {
    private final ServiceInstanceProvider serviceInstanceProvider;
    private Iterator<ServiceInstance> iterator;

    public RoundRobinLoadBalancer(ServiceInstanceProvider serviceInstanceProvider) {
        this.serviceInstanceProvider = serviceInstanceProvider;
        this.iterator = serviceInstanceProvider.getServiceInstances().iterator();
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (!iterator.hasNext())
            iterator = serviceInstanceProvider.getServiceInstances().iterator();
        ServiceInstance nextInstance = iterator.next();
        return Mono.just(nextInstance);
    }

    @Override
    public String getType() {
        return LoadBalancerType.ROUND_ROBIN.name();
    }

}
