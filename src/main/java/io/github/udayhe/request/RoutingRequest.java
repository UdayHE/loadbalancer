package io.github.udayhe.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.udayhe.enums.HttpMethod;
import io.github.udayhe.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutingRequest {

    private Object discriminator;
    private String endPointPath;
    private String payload;
    private ResourceType resourceType;
    private HttpMethod httpMethod;
}
