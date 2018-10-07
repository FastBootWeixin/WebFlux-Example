package com.mxixm.spring.webflux;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyHandlerMapping implements HandlerMapping {
    @Override
    public Mono<Object> getHandler(ServerWebExchange exchange) {
        List<String> names = new ArrayList<>();
        names.add("Guangshan");
        names.add("July");
        names.add("Jackson");
        List<String> helloNames = names.stream().map(n -> "Hello " + n).collect(Collectors.toList());
        return Mono.error(new RuntimeException());
    }


    public static void main(String[] args) throws InterruptedException {
//        Mono<String> mono = asyncResult();
//        System.out.println(System.currentTimeMillis());
//        Thread.sleep(2000L);
//        mono.subscribe(s -> System.out.println(System.currentTimeMillis() + s));
//        System.out.println(System.currentTimeMillis());
//        Thread.sleep(10000000L);
        simulateRequestAndResponse();
    }

    private static void simulateRequestAndResponse() {
        // 先通过delayRequest获取请求数据，但请求数据5S后才可以被全部接收
        // 数据接收后暂时不会被map和flatMap处理，只有此数据流被订阅后，才执行map和flatMap处理，此时请求数据暂时存在缓存中
        // map把请求数据做一个简单的映射，flatMap调用asyncResult获取结果，因为asyncResult返回的也是一个数据流
        // 所以用flatMap操作符，可以做到请求时不获取数据，订阅时才开始获取
        // 最后执行订阅，写入响应，此时map和flatMap才开始执行
        // 可以看到在执行延迟5秒后，asyncResult方法被执行，此时获取了完整的请求数据
        // 在又延迟5秒后，打印出了结果，这是asyncResult获取结果的延迟
        Mono.fromFuture(delayRequest())
                .map(request -> "Hello " + request)
                .flatMap(request -> asyncResult(request))
                .subscribe(response -> System.out.println(response));

        System.out.println("请求线程执行完成");
        sleep(20000L);
    }

    // 模拟一个获取请求数据的情况，从申请获取到获取结果需要5秒
    private static CompletableFuture<String> delayRequest() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(5000);
            return "Request";
        });
    }
    // 模拟一个长时间的执行结果，从执行开始到执行获得结果需要5S
    private static Mono<String> asyncResult(String request) {
        System.out.println(request);
        return Mono.just("defaultView").delaySubscription(Duration.ofSeconds(5L)).log();
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
