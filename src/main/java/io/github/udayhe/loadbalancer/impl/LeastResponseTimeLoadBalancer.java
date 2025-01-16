package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.enums.LoadBalancerType;
import io.github.udayhe.exception.LoadBalancerException;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.udayhe.constant.ExceptionConstant.INSTANCE_UNAVAILABLE;

@Singleton
public class LeastResponseTimeLoadBalancer implements CustomLoadBalancer {

    private final ServiceInstanceProvider serviceInstanceProvider;
    private final ConcurrentHashMap<ServiceInstance, ServerMetrics> metricsMap = new ConcurrentHashMap<>();

    public LeastResponseTimeLoadBalancer(ServiceInstanceProvider serviceInstanceProvider) {
        this.serviceInstanceProvider = serviceInstanceProvider;
        serviceInstanceProvider.getServiceInstances().forEach(instance -> metricsMap.put(instance, new ServerMetrics()));
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        ServiceInstance selectedInstance = serviceInstanceProvider.getServiceInstances().stream()
                .min(Comparator.comparingDouble(instance -> calculateLoadScore(metricsMap.get(instance))))
                .orElseThrow(() -> new LoadBalancerException(INSTANCE_UNAVAILABLE));

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
