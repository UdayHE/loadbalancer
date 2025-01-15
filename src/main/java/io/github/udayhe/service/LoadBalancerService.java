package io.github.udayhe.service;

import io.github.udayhe.dto.RoutingRequestDTO;
import org.reactivestreams.Publisher;

public interface LoadBalancerService {
    Publisher<String> routeRequest(RoutingRequestDTO routingRequest);
}
