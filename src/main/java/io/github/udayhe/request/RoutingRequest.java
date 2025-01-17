package io.github.udayhe.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.udayhe.enums.HttpMethod;
import io.github.udayhe.enums.ResourceType;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class RoutingRequest {

    private Object discriminator;
    private String endPointPath;
    private String payload;
    private ResourceType resourceType;
    private HttpMethod httpMethod;
}
