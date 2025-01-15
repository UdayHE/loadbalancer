package io.github.udayhe.exception.handler;

import io.github.udayhe.exception.ErrorResponse;
import io.github.udayhe.exception.LoadBalancerException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;


public class LoadBalancerExceptionHandler implements ExceptionHandler<LoadBalancerException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, LoadBalancerException exception) {
        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getMessage()));
    }
}
