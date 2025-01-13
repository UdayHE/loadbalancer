package io.github.udayhe.loadbalancer;

import io.micronaut.discovery.ServiceInstance;
import io.micronaut.http.client.LoadBalancer;
import org.reactivestreams.Publisher;

public interface CustomLoadBalancer extends LoadBalancer {

    Publisher<ServiceInstance> select(Object discriminator);
    String getType();
}
