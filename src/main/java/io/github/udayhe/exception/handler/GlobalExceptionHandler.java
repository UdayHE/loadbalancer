package io.github.udayhe.exception.handler;

import io.github.udayhe.exception.ErrorResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import static io.github.udayhe.constant.ExceptionConstant.INTERNAL_SERVER_ERROR;

@Produces
@Singleton
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        return HttpResponse.serverError(new ErrorResponse(INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}
