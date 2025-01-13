package io.github.udayhe.router;

import io.github.udayhe.context.LoadBalancerContext;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.strategy.*;
import io.micronaut.context.annotation.Value;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import static io.github.udayhe.enums.LoadBalancerType.valueOf;

@Singleton
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


    public Publisher<ServiceInstance> routeRequest(Object discriminator) {
        LoadBalancerType loadBalancerType = valueOf(LoadBalancerType.class, strategyType);
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

        return loadBalancerContext.selectService(discriminator);
    }

}
