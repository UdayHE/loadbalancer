package io.github.udayhe.context;

import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

@Singleton
public class LoadBalancerContext {

    private CustomLoadBalancer loadBalancer;

    public void setLoadBalancer(CustomLoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Publisher<ServiceInstance> selectService(Object discriminator) {
        return loadBalancer.select(discriminator);
    }
    public String getLoadBalancerType() {
        return loadBalancer.getType();
    }
}
