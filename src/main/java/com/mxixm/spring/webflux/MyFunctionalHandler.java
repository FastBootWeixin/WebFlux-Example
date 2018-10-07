package com.mxixm.spring.webflux;

import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class MyFunctionalHandler {

    // 返回请求体内容
    public Mono<ServerResponse> echo(ServerRequest request) {
        System.out.println(request.path());
        System.out.println(request.method());
        System.out.println(request.headers().asHttpHeaders());
        System.out.println(request.queryParams());
        System.out.println(request.cookies());
        // 把请求体转换为String类型的Mono流，通过block阻塞获取数据。
        // 一般不推荐这种方式获取数据，推荐直接把Mono流通过各种转换处理转换为返回值的Mono流
        // System.out.println(request.bodyToMono(String.class).block());
        // 同上面语句，使用BodyExtractors的工具提取请求体
        System.out.println(request.body(BodyExtractors.toMono(String.class)).block());
        return ServerResponse.ok().body(request.bodyToMono(String.class), String.class);
    }

    // 返回请求参数key内容
    public Mono<ServerResponse> get(ServerRequest request) {
        // 返回404，带有一个响应头
        ServerResponse.notFound().header("X-Custom-Header", "Test");
        Map<String, Object> model = new HashMap<>(2);
        model.put("name", "Guangshan");
        // 渲染视图
        ServerResponse.ok().render("defaultView", model);
        // 通过Body返回响应体
        ServerResponse.ok().body(Mono.just("BodyTest"), String.class);
        // 通过BodyInserter的fromObject设置响应体
        return ServerResponse.ok().body(fromObject(request.queryParam("key")));
    }

    // 返回所有请求头的名称列表
    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok().body(fromObject(request.headers().asHttpHeaders().keySet()));
    }

}
