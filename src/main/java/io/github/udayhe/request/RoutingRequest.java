package io.github.udayhe.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.udayhe.enums.HttpMethod;
import io.github.udayhe.enums.ResourceType;
import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class RoutingRequest {

    @NotNull(message = "Discriminator cannot be null")
    private Object discriminator;

    @NotBlank(message = "Endpoint path cannot be blank")
    private String endPointPath;

    private String payload;

    @NotNull(message = "Resource type cannot be null")
    private ResourceType resourceType;

    @NotNull(message = "HTTP method cannot be null")
    private HttpMethod httpMethod;
}
