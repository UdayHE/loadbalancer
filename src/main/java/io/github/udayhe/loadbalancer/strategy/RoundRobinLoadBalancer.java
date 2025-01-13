package io.github.udayhe.loadbalancer.strategy;

import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;

@Singleton
public class RoundRobinLoadBalancer implements CustomLoadBalancer {
    private final List<ServiceInstance> instances;
    private Iterator<ServiceInstance> iterator;

    public RoundRobinLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;
        this.iterator = instances.iterator();
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (!iterator.hasNext()) {
            iterator = instances.iterator();
        }
        ServiceInstance nextInstance = iterator.next();
        return Mono.just(nextInstance);
    }

    @Override
    public String getType() {
        return LoadBalancerType.ROUND_ROBIN.name();
    }

}
