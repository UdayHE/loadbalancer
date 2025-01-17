package io.github.udayhe.constant;

public class ConfigConstant {

    private ConfigConstant(){}

    public static final String CONTEXT_PATH = "/load-balancer";
    public static final String CONTEXT_PATH_PROPERTY = "micronaut.server.context-path";
    public static final String ROUTE_CONTROLLER_PATH = "/route";
    public static final String SSE_PATH = "/sse";
    public static final String WEBSOCKET_PATH = "/ws/{topic}";
}
