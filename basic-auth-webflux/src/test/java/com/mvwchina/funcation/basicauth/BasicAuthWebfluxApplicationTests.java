package com.mvwchina.funcation.basicauth;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicAuthWebfluxApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testMonoBasic() {
//		Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
//		Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
//		Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
    }

    public static void main(String[] args) {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);

        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        System.out.println("=====");
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);

        final String block1 = Flux.fromIterable(Lists.newArrayList("a1", "a2", "b", "a2"))
                .filter(s -> s.startsWith("a"))
                .switchIfEmpty(
                        Mono.empty()
                                .then(Mono.fromRunnable(() -> System.out.println("找不到a开头的")))
                                .then(Mono.error(new RuntimeException("找不到a开头的 exception")))
                )
                .filter(s -> s.equals("a2"))
                .switchIfEmpty(
                        Mono.empty()
                                .then(Mono.fromRunnable(() -> System.out.println("找不到a2的")))
                                .then(Mono.error(new RuntimeException("找不到a2的")))
                )
                .map(s -> {
                    System.out.println("do some things: " + s);
                    return s + " done!";
                }).next().block();
        System.out.println(block1);

    }
}
