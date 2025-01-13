package io.github.udayhe.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "my-service")
public interface MyServiceClient {
    @Get("/api/resource")
    String getResource();
}
