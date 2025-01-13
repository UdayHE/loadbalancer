package io.github.udayhe.factory;

import io.github.udayhe.config.AppConfig;
import io.github.udayhe.enums.LBType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.github.udayhe.loadbalancer.strategy.RandomLoadBalancer;
import io.github.udayhe.loadbalancer.strategy.RoundRobinLoadBalancer;
import io.github.udayhe.loadbalancer.strategy.WeightedLoadBalancer;
import io.micronaut.context.annotation.Factory;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;

import java.util.List;

@Factory
public class LoadBalancerFactory {

    private final List<ServiceInstance> instances;

    public LoadBalancerFactory(List<ServiceInstance> instances) {
        this.instances = instances;
    }

    @Singleton
    public CustomLoadBalancer customLoadBalancer(AppConfig config) {
        LBType lbType = Enum.valueOf(LBType.class, config.getLoadBalancerStrategy());
        return switch (lbType) {
            case LBType.ROUND_ROBIN -> new RoundRobinLoadBalancer(instances);
            case LBType.RANDOM -> new RandomLoadBalancer(instances);
            case LBType.WEIGHTED -> new WeightedLoadBalancer(instances);
        };
    }
}
