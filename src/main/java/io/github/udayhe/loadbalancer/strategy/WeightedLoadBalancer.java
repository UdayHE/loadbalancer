package io.github.udayhe.loadbalancer.strategy;

import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Singleton
public class WeightedLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> weightedInstances;
    private final Random random = new Random();

    public WeightedLoadBalancer(List<ServiceInstance> instances) {
        this.weightedInstances = instances;
    }

    public WeightedLoadBalancer(List<ServiceInstance> instances, List<Integer> weights) {
        this.weightedInstances = new ArrayList<>();
        for (int i = 0; i < instances.size(); i++) {
            int weight = weights.get(i);
            for (int j = 0; j < weight; j++) {
                weightedInstances.add(instances.get(i));
            }
        }
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        int index = random.nextInt(weightedInstances.size());
        ServiceInstance selectedInstance = weightedInstances.get(index);
        return Mono.just(selectedInstance);
    }


    @Override
    public String getType() {
        return LoadBalancerType.WEIGHTED.name();
    }
}
