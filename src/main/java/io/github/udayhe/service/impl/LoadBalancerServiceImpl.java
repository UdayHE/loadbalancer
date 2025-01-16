package io.github.udayhe.service.impl;

import io.github.udayhe.request.RoutingRequest;
import io.github.udayhe.router.ServiceRouter;
import io.github.udayhe.service.LoadBalancerService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
        if (isBlank(routingRequest.getResourceType())) {
            String path = routingRequest.getEndPointPath();
            String resourceType = determineResourceType(path);
            routingRequest.setResourceType(resourceType);
        }
    }

    private String determineResourceType(String path) {
        if (path.endsWith(".html")) return "html";
        if (path.endsWith(".js")) return "js";
        if (path.endsWith(".css")) return "css";
        if (path.matches(".*\\.(png|jpg|jpeg|gif|svg)$")) return "image";
        if (path.startsWith("/api/")) return "api";
        return "unknown";
    }
}
