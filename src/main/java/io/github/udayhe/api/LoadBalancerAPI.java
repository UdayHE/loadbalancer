package io.github.udayhe.api;

import io.github.udayhe.service.LoadBalancerService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

@Controller("/route")
@RequiredArgsConstructor
public class LoadBalancerAPI {

    private final LoadBalancerService loadBalancerService;

    @Post()
    public Publisher<String> route(String payload) {
        return loadBalancerService.routeRequest("user123", "/target-service-endpoint", payload);
    }
}
