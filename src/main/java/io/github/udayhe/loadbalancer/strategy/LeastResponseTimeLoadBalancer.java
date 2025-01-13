package io.github.udayhe.loadbalancer.strategy;

import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class LeastResponseTimeLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> instances;
    private final ConcurrentHashMap<ServiceInstance, ServerMetrics> metricsMap = new ConcurrentHashMap<>();

    public LeastResponseTimeLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;

        // Initialize metrics for each instance
        instances.forEach(instance -> metricsMap.put(instance, new ServerMetrics()));
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        // Find the server with the lowest load score
        ServiceInstance selectedInstance = instances.stream()
                .min(Comparator.comparingDouble(instance -> calculateLoadScore(metricsMap.get(instance))))
                .orElseThrow(() -> new RuntimeException("No available instances"));

        // Increment the active connections for the selected instance
        metricsMap.get(selectedInstance).incrementConnections();

        return Mono.just(selectedInstance);
    }

    public void recordResponseTime(ServiceInstance instance, long responseTime) {
        ServerMetrics metrics = metricsMap.get(instance);
        if (metrics != null) {
            metrics.addResponseTime(responseTime);
        }
    }

    public void release(ServiceInstance instance) {
        ServerMetrics metrics = metricsMap.get(instance);
        if (metrics != null) {
            metrics.decrementConnections();
        }
    }

    @Override
    public String getType() {
        return LoadBalancerType.LEAST_RESPONSE_TIME.name();
    }

    private double calculateLoadScore(ServerMetrics metrics) {
        if (metrics == null) return Double.MAX_VALUE;

        // Normalize the response time to a weight (can be tuned as needed)
        double normalizedResponseTime = metrics.getAverageResponseTime() / 100.0;
        return metrics.getActiveConnections() + normalizedResponseTime;
    }

    /**
     * Inner class to track metrics for each server.
     */
    private static class ServerMetrics {
        private final AtomicInteger activeConnections = new AtomicInteger(0);
        private long totalResponseTime = 0;
        private long responseCount = 0;

        public void incrementConnections() {
            activeConnections.incrementAndGet();
        }

        public void decrementConnections() {
            if (activeConnections.get() > 0) {
                activeConnections.decrementAndGet();
            }
        }

        public int getActiveConnections() {
            return activeConnections.get();
        }

        public synchronized void addResponseTime(long responseTime) {
            totalResponseTime += responseTime;
            responseCount++;
        }

        public synchronized double getAverageResponseTime() {
            return responseCount == 0 ? 0 : (double) totalResponseTime / responseCount;
        }
    }
}
