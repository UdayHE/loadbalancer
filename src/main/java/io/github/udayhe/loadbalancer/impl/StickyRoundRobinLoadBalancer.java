package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.udayhe.enums.LoadBalancerType.STICKY_ROUND_ROBIN;
import static java.util.Objects.isNull;

@Singleton
public class StickyRoundRobinLoadBalancer implements CustomLoadBalancer {

    private final ServiceInstanceProvider serviceInstanceProvider;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public StickyRoundRobinLoadBalancer(ServiceInstanceProvider serviceInstanceProvider) {
        this.serviceInstanceProvider = serviceInstanceProvider;
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (isNull(discriminator))
            return Mono.empty();
        int index = currentIndex.getAndIncrement() % serviceInstanceProvider.getServiceInstances().size();
        ServiceInstance selectedInstance = serviceInstanceProvider.getServiceInstances().get(index);
        return Mono.just(selectedInstance);
    }

    @Override
    public String getType() {
        return STICKY_ROUND_ROBIN.name();
    }
}
