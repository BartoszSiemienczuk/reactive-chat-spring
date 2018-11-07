package com.softwarehut.reactivechat;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PublishersTests {
    @Test
    public void givenFluxesWhenZippedAndSummedThenSum(){

        Flux<Integer> zippedFlux = Flux.zip(
                Flux.just(1, 3, 5),
                Flux.just(2, 4, 6),
                (a, b) -> a + b
        );

        StepVerifier.create(zippedFlux)
            .expectNext(3)
            .expectNext(7)
            .expectNext(11)
            .expectComplete()
            .verify();
    }

    @Test
    public void givenFluxWithRangeExpectOrderMaintained(){
        Flux<Integer> rangeFlux = Flux.range(1,5);

        StepVerifier.create(rangeFlux)
            .expectNext(1)
            .expectNext(2)
            .expectNext(3)
            .expectNext(4)
            .expectNext(5)
            .expectComplete()
            .verify();

    }
}
