package io.github.udayhe.config;

import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.List;

@Serdeable
@Singleton
@ConfigurationProperties("services")
public class ServiceConfiguration {

    @Getter
    @Setter
    private List<ServiceInstanceConfig> instances;

    @Getter
    @Setter
    @Serdeable
    public static class ServiceInstanceConfig {
        private String id;
        private URI uri;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String id;
            private URI uri;

            public Builder withId(String id) {
                this.id = id;
                return this;
            }

            public Builder withUri(URI uri) {
                this.uri = uri;
                return this;
            }

            public ServiceInstanceConfig build() {
                ServiceInstanceConfig config = new ServiceInstanceConfig();
                config.setId(this.id);
                config.setUri(this.uri);
                return config;
            }
        }
    }

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "instance")
    protected ServiceInstanceConfig.Builder builder = ServiceInstanceConfig.builder();

}
