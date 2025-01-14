package io.github.udayhe.service;

import org.reactivestreams.Publisher;

public interface LoadBalancerService {
    Publisher<String> routeRequest(Object discriminator, String endpointPath, String payload);
}
