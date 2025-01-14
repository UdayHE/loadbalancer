package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.udayhe.enums.LoadBalancerType.STICKY_ROUND_ROBIN;

@Singleton
public class StickyRoundRobinLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> serviceInstances;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public StickyRoundRobinLoadBalancer(List<ServiceInstance> instances) {
        this.serviceInstances = instances;
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (discriminator == null) {
            return Mono.empty();
        }

        // We could use discriminator (like IP or session) to make the round robin sticky.
        // In this example, we'll ignore the discriminator and just apply round robin.
        int index = currentIndex.getAndIncrement() % serviceInstances.size();
        ServiceInstance selectedInstance = serviceInstances.get(index);
        return Mono.just(selectedInstance);
    }

    @Override
    public String getType() {
        return STICKY_ROUND_ROBIN.name();
    }
}
