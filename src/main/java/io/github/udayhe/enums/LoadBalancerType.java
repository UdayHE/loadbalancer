package io.github.udayhe.enums;

public enum LoadBalancerType {
    RANDOM,
    ROUND_ROBIN,
    WEIGHTED,
    LEAST_CONNECTIONS,
    LEAST_RESPONSE_TIME,
    STICKY_ROUND_ROBIN,
    IP_URL_HASH
}
