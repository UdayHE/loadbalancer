package io.github.udayhe.service.impl;

import io.github.udayhe.enums.ResourceType;
import io.github.udayhe.request.RoutingRequest;
import io.github.udayhe.router.ServiceRouter;
import io.github.udayhe.service.LoadBalancerService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import static io.github.udayhe.enums.ResourceType.*;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Singleton
public class LoadBalancerServiceImpl implements LoadBalancerService {

    private final ServiceRouter serviceRouter;

    @Override
    public Publisher<String> routeRequest(RoutingRequest routingRequest) {
        determineResourceTypeIdRequired(routingRequest);
        return serviceRouter.routeRequest(routingRequest);
    }

    private void determineResourceTypeIdRequired(RoutingRequest routingRequest) {
        if (isNull(routingRequest.getResourceType())) {
            String path = routingRequest.getEndPointPath();
            ResourceType resourceType = determineResourceType(path);
            routingRequest.setResourceType(resourceType);
        }
    }

    private ResourceType determineResourceType(String path) {
        if (path.endsWith(".html")) return HTML;
        if (path.endsWith(".js")) return JS;
        if (path.endsWith(".css")) return CSS;
        if (path.matches(".*\\.(png|jpg|jpeg|gif|svg)$")) return IMAGE;
        if (path.startsWith("/api/")) return API;
        return UNKNOWN;
    }
}
