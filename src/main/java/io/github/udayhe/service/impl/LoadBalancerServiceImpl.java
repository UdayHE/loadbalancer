package io.github.udayhe.service.impl;

import io.github.udayhe.router.ServiceRouter;
import io.github.udayhe.service.LoadBalancerService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class LoadBalancerServiceImpl implements LoadBalancerService {

    private final ServiceRouter serviceRouter;

    @Override
    public Publisher<String> routeRequest(Object discriminator, String endpointPath, String payload) {
        return serviceRouter.routeRequest(discriminator, endpointPath, payload);
    }
}
