package io.github.udayhe.api;

import io.github.udayhe.request.RoutingRequest;
import io.github.udayhe.service.LoadBalancerService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import static io.github.udayhe.constant.ConfigConstant.ROUTE_CONTROLLER_PATH;

@Controller(ROUTE_CONTROLLER_PATH)
@RequiredArgsConstructor
public class LoadBalancerAPI {

    private final LoadBalancerService loadBalancerService;

    @Post()
    public HttpResponse<Publisher<String>> route(@Body RoutingRequest routingRequest) {
        return HttpResponse.ok(loadBalancerService.routeRequest(routingRequest));
    }
}
