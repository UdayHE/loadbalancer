package io.github.udayhe.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String resourceType;
    private String httpMethod;
}
