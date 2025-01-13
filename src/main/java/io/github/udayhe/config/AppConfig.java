package io.github.udayhe.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("app")
public class AppConfig {
    private String loadBalancerStrategy;

    public String getLoadBalancerStrategy() {
        return loadBalancerStrategy;
    }

    public void setLoadBalancerStrategy(String loadBalancerStrategy) {
        this.loadBalancerStrategy = loadBalancerStrategy;
    }
}
