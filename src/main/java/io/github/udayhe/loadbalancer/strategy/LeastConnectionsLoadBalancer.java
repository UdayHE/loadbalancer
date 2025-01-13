package io.github.udayhe.loadbalancer.strategy;

import io.github.udayhe.enums.LBType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class LeastConnectionsLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> instances;
    private final ConcurrentHashMap<ServiceInstance, AtomicInteger> connectionCounts = new ConcurrentHashMap<>();

    public LeastConnectionsLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;
        instances.forEach(instance -> connectionCounts.put(instance, new AtomicInteger(0)));
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        ServiceInstance selectedInstance = instances.stream()
                .min(Comparator.comparingInt(instance -> connectionCounts.get(instance).get()))
                .orElseThrow(() -> new RuntimeException("No available instances"));

        connectionCounts.get(selectedInstance).incrementAndGet();
        return Mono.just(selectedInstance);
    }

    /**
     * Decrement the connection count for a service instance.
     * Should be called when a request is completed.
     */
    public void release(ServiceInstance instance) {
        AtomicInteger count = connectionCounts.get(instance);
        if (count != null) {
            count.decrementAndGet();
        }
    }

    @Override
    public String getType() {
        return LBType.LEAST_CONNECTIONS.name();
    }
}
