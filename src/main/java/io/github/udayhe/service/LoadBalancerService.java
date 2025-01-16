package io.github.udayhe.service;

import io.github.udayhe.request.RoutingRequest;
import org.reactivestreams.Publisher;

public interface LoadBalancerService {
    Publisher<String> routeRequest(RoutingRequest routingRequest);
}
