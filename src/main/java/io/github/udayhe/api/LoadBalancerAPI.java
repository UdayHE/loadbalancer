package io.github.udayhe.api;

import io.github.udayhe.dto.RoutingRequestDTO;
import io.github.udayhe.service.LoadBalancerService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

@Controller("/route")
@RequiredArgsConstructor
public class LoadBalancerAPI {

    private final LoadBalancerService loadBalancerService;

    @Post()
    public HttpResponse<Publisher<String>> route(@Body RoutingRequestDTO routingRequest) {
        return HttpResponse.ok(loadBalancerService.routeRequest(routingRequest));
    }
}
