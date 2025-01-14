package io.github.udayhe;

import io.micronaut.runtime.Micronaut;

import java.util.Map;

import static io.github.udayhe.constant.ConfigConstant.CONTEXT_PATH;
import static io.github.udayhe.constant.ConfigConstant.CONTEXT_PATH_PROPERTY;

public class LoadBalancerLauncher {


    public static void main(String[] args) {
        Micronaut.build(args)
                .properties(Map.of(CONTEXT_PATH_PROPERTY, CONTEXT_PATH))
                .start();
    }
}