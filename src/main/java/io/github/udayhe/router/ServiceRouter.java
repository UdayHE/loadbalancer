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
                    String instanceUri = serviceInstance.getURI().toString();
                    log.info("Service URI:{}", instanceUri);
                    try {
                        HttpRequest<String> request = HttpRequest.GET(instanceUri);
                        return Mono.from(httpClient.retrieve(request, String.class));
                    } catch (Exception e) {
                        log.error("Exception in routeRequest.", e);
                        return Mono.error(e);
                    }
                });
    }
}
