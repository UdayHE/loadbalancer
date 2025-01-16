package io.github.udayhe.router;

import io.github.udayhe.context.LoadBalancerContext;
import io.github.udayhe.dto.RoutingRequest;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.exception.LoadBalancerException;
import io.github.udayhe.loadbalancer.impl.*;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import static io.github.udayhe.enums.LoadBalancerType.valueOf;

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

    @Client("/")
    private final HttpClient httpClient;
    @Value("${app.loadBalancerStrategy}")
    private String strategyType;

    public Publisher<String> routeRequest(RoutingRequest routingRequest) {
        Object discriminator = routingRequest.getDiscriminator();
        String endPointPath = routingRequest.getEndPointPath();
        String payload = routingRequest.getPayload();
        String httpMethod = routingRequest.getHttpMethod();
        String resourceType = routingRequest.getResourceType();

        LoadBalancerType loadBalancerType = valueOf(strategyType);
        setLoadBalancerContext(loadBalancerType);
        return Mono.from(loadBalancerContext.selectService(discriminator))
                .flatMap(serviceInstance -> {
                    String instanceUri = getInstanceURI(resourceType, serviceInstance.getURI().toString() + endPointPath);
                    log.info("Routing {} request to URI: {}", httpMethod, instanceUri);
                    HttpRequest<?> request = getHttpRequest(httpMethod, instanceUri, payload);

                    return Mono.from(httpClient.retrieve(request, String.class));
                }).doOnError(e -> log.error("Exception in routeRequest", e));
    }

    private void setLoadBalancerContext(LoadBalancerType loadBalancerType) {
        switch (loadBalancerType) {
            case IP_URL_HASH -> loadBalancerContext.setLoadBalancer(ipUrlHashLoadBalancer);
            case STICKY_ROUND_ROBIN -> loadBalancerContext.setLoadBalancer(stickyRoundRobinLoadBalancer);
            case LEAST_RESPONSE_TIME -> loadBalancerContext.setLoadBalancer(leastResponseTimeLoadBalancer);
            case LEAST_CONNECTIONS -> loadBalancerContext.setLoadBalancer(leastConnectionsLoadBalancer);
            case WEIGHTED -> loadBalancerContext.setLoadBalancer(weightedLoadBalancer);
            case ROUND_ROBIN -> loadBalancerContext.setLoadBalancer(roundRobinLoadBalancer);
            case RANDOM -> loadBalancerContext.setLoadBalancer(randomLoadBalancer);
            default -> throw new LoadBalancerException("Unknown strategy type: " + strategyType);
        }
    }

    private String getInstanceURI(String resourceType, String instanceUri) {
        switch (resourceType.toLowerCase()) {
            case "html" -> instanceUri += ".html";
            case "js" -> instanceUri += ".js";
            case "scripts" -> instanceUri += ".script";
            default -> throw new LoadBalancerException("Unknown resource type:" + resourceType + ". Using raw endpoint. ");
        }
        return instanceUri;
    }

    private HttpRequest<?> getHttpRequest(String httpMethod, String instanceUri, String payload) {
        return switch (httpMethod.toUpperCase()) {
            case "GET" -> HttpRequest.GET(instanceUri);
            case "POST" -> HttpRequest.POST(instanceUri, payload);
            case "PUT" -> HttpRequest.PUT(instanceUri, payload);
            case "DELETE" -> HttpRequest.DELETE(instanceUri);
            default -> throw new LoadBalancerException("Unsupported HTTP method: " + httpMethod);
        };
    }
}
