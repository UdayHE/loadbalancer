package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.exception.LoadBalancerException;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.udayhe.constant.ExceptionConstant.INSTANCE_UNAVAILABLE;

@Singleton
public class LeastConnectionsLoadBalancer implements CustomLoadBalancer {

    private final ServiceInstanceProvider serviceInstanceProvider;
    private final ConcurrentHashMap<ServiceInstance, AtomicInteger> connectionCounts = new ConcurrentHashMap<>();

    public LeastConnectionsLoadBalancer(ServiceInstanceProvider serviceInstanceProvider) {
        this.serviceInstanceProvider = serviceInstanceProvider;
        this.serviceInstanceProvider.getServiceInstances()
                .forEach(instance -> connectionCounts.put(instance, new AtomicInteger(0)));
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        ServiceInstance selectedInstance = this.serviceInstanceProvider.getServiceInstances().stream()
                .min(Comparator.comparingInt(instance -> connectionCounts.get(instance).get()))
                .orElseThrow(() -> new LoadBalancerException(INSTANCE_UNAVAILABLE));

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
        return LoadBalancerType.LEAST_CONNECTIONS.name();
    }
}
