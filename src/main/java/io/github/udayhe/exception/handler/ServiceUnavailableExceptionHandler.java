package io.github.udayhe.exception.handler;

import io.github.udayhe.exception.ErrorResponse;
import io.github.udayhe.exception.ServiceUnavailableException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static io.github.udayhe.constant.ExceptionConstant.SERVICE_UNAVAILABLE;

@Produces
@Singleton
public class ServiceUnavailableExceptionHandler implements ExceptionHandler<ServiceUnavailableException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ServiceUnavailableException exception) {
        return HttpResponse.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse(SERVICE_UNAVAILABLE, exception.getMessage()));
    }
}