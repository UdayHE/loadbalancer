package io.github.udayhe.loadbalancer.strategy;

import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static io.github.udayhe.enums.LoadBalancerType.IP_URL_HASH;

@Singleton
public class IPUrlHashLoadBalancer implements CustomLoadBalancer {

    private final List<ServiceInstance> instances;

    public IPUrlHashLoadBalancer(List<ServiceInstance> instances) {
        this.instances = instances;
    }

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (discriminator == null)
            return Mono.empty();

        int index = getHash(discriminator.toString()) % instances.size();
        ServiceInstance selectedInstance = instances.get(index);
        return Mono.just(selectedInstance);
    }

    private int getHash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(value.getBytes());
            int hashCode = Arrays.hashCode(hash);
            return (hashCode & Integer.MAX_VALUE);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }

    @Override
    public String getType() {
        return IP_URL_HASH.name();
    }
}
