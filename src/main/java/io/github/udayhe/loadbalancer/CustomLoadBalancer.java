package io.github.udayhe.loadbalancer;

import io.micronaut.http.client.LoadBalancer;

public interface CustomLoadBalancer extends LoadBalancer {

    String getType();
}
