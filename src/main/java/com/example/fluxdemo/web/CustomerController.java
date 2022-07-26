package com.example.fluxdemo.web;

import com.example.fluxdemo.domain.Customer;
import com.example.fluxdemo.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository repository;
    private final Sinks.Many<Customer> sink;

    // A 요쳥 -> Flux => Stream
    // B 요청 -> Flux => Stream
    // => Flux.merge / sink

    @GetMapping("flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value="fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxstream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value="customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Customer> findAll() {
        return repository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("customer/{id}")
    public Mono<Customer> findById(@PathVariable long id) {
        return repository.findById(id).log();
    }

    @GetMapping(value = "customer/sse") // ServerSentEvenr로 반환하면 mime type 생략가능,  produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public Flux<ServerSentEvent<Customer>> findAllSSE() {
        return sink.asFlux().map(c -> ServerSentEvent.builder(c).build()).doOnCancel(() -> sink.asFlux().blockLast());
    }

    @PostMapping("customer")
    public Mono<Customer> save() {
        return repository.save(new Customer("gilding", "Hong")).doOnNext(c -> {
            sink.tryEmitNext(c);
        });
    }
}
