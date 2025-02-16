package io.github.udayhe.loadbalancer.impl;

import io.github.udayhe.config.ServiceInstanceProvider;
import io.github.udayhe.exception.LoadBalancerException;
import io.github.udayhe.loadbalancer.CustomLoadBalancer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.discovery.ServiceInstance;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static io.github.udayhe.enums.LoadBalancerType.IP_URL_HASH;
import static java.util.Objects.isNull;

@Singleton
@RequiredArgsConstructor
public class IPUrlHashLoadBalancer implements CustomLoadBalancer {

    private final ServiceInstanceProvider serviceInstanceProvider;

    @Override
    public Publisher<ServiceInstance> select(@Nullable Object discriminator) {
        if (isNull(discriminator))
            return Mono.empty();

        List<ServiceInstance> serviceInstances = serviceInstanceProvider.getServiceInstances();
        int index = getHash(discriminator.toString()) % serviceInstances.size();
        ServiceInstance selectedInstance = serviceInstances.get(index);
        return Mono.just(selectedInstance);
    }

    private int getHash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(value.getBytes());
            int hashCode = Arrays.hashCode(hash);
            return (hashCode & Integer.MAX_VALUE);
        } catch (NoSuchAlgorithmException e) {
            throw new LoadBalancerException("Error calculating hash.." + e);
        }
    }

    @Override
    public String getType() {
        return IP_URL_HASH.name();
    }
}
