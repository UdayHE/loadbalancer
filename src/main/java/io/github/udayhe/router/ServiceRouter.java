package io.github.udayhe.router;

import io.github.udayhe.context.LoadBalancerContext;
import io.github.udayhe.dto.RoutingRequest;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.impl.*;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class ServiceRouter {

    private final LoadBalancerContext loadBalancerContext;
    private final IPUrlHashLoadBalancer ipUrlHashLoadBalancer;
    private final StickyRoundRobinLoadBalancer stickyRoundRobinLoadBalancer;
    private final LeastResponseTimeLoadBalancer leastResponseTimeLoadBalancer;
    private final LeastConnectionsLoadBalancer leastConnectionsLoadBalancer;
    private final WeightedLoadBalancer weightedLoadBalancer;
    private final RoundRobinLoadBalancer roundRobinLoadBalancer;
    private final RandomLoadBalancer randomLoadBalancer;

    @Value("${app.loadBalancerStrategy}")
    private String strategyType;

    @Inject
    @Client("/")
    private HttpClient httpClient;

    public Publisher<String> routeRequest(RoutingRequest routingRequest) {
        Object discriminator = routingRequest.getDiscriminator();
        String endPointPath = routingRequest.getEndPointPath();
        String payload = routingRequest.getPayload();
        String httpMethod = routingRequest.getHttpMethod();
        String resourceType = routingRequest.getResourceType();

        LoadBalancerType loadBalancerType = LoadBalancerType.valueOf(strategyType);
        switch (loadBalancerType) {
            case IP_URL_HASH:
                loadBalancerContext.setLoadBalancer(ipUrlHashLoadBalancer);
                break;
            case STICKY_ROUND_ROBIN:
                loadBalancerContext.setLoadBalancer(stickyRoundRobinLoadBalancer);
                break;
            case LEAST_RESPONSE_TIME:
                loadBalancerContext.setLoadBalancer(leastResponseTimeLoadBalancer);
                break;
            case LEAST_CONNECTIONS:
                loadBalancerContext.setLoadBalancer(leastConnectionsLoadBalancer);
                break;
            case WEIGHTED:
                loadBalancerContext.setLoadBalancer(weightedLoadBalancer);
                break;
            case ROUND_ROBIN:
                loadBalancerContext.setLoadBalancer(roundRobinLoadBalancer);
                break;
            case RANDOM:
                loadBalancerContext.setLoadBalancer(randomLoadBalancer);
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy type: " + strategyType);
        }

        // Select service instance and route the request
        return Mono.from(loadBalancerContext.selectService(discriminator))
                .flatMap(serviceInstance -> {
                    String instanceUri = serviceInstance.getURI().toString() + endPointPath;

                    // Adjust URI based on resource type
                    switch (resourceType.toLowerCase()) {
                        case "html":
                            instanceUri += ".html";
                            break;
                        case "js":
                            instanceUri += ".js";
                            break;
                        case "scripts":
                            instanceUri += ".script";
                            break;
                        // Add other resource types as needed
                        default:
                            log.warn("Unknown resource type: {}. Using raw endpoint.", resourceType);
                    }

                    log.info("Routing {} request to URI: {}", httpMethod, instanceUri);
                    // Create the appropriate HttpRequest based on the HTTP method
                    HttpRequest<?> request;
                    switch (httpMethod.toUpperCase()) {
                        case "GET":
                            request = HttpRequest.GET(instanceUri);
                            break;
                        case "POST":
                            request = HttpRequest.POST(instanceUri, payload);
                            break;
                        case "PUT":
                            request = HttpRequest.PUT(instanceUri, payload);
                            break;
                        case "DELETE":
                            request = HttpRequest.DELETE(instanceUri);
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
                    }

                    return Mono.from(httpClient.retrieve(request, String.class));
                })
                .doOnError(e -> log.error("Exception in routeRequest", e));
    }
}
