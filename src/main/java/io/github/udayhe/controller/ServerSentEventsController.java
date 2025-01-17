package io.github.udayhe.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller("/sse")
public class ServerSentEventsController {

    @Get(produces = MediaType.TEXT_EVENT_STREAM)
    public Publisher<String> streamData() {
        return Flux.interval(Duration.ofSeconds(1))
                   .map(count -> "Event " + count);
    }
}
