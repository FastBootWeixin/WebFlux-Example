package com.mxixm.spring.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

// 静态导入static的方法或常量
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

// 声明为配置类
@Configuration
public class MyRouteFunctionConfig {

    // 路由函数注册为Bean
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        // 创建处理器实例
        MyFunctionalHandler handler = new MyFunctionalHandler();
        // 注册路由函数，/echo的POST方法路由到handler的echo方法
        // /get路径上带有请求参数key的GET方法路由到handler的get方法
        // /list路径上的GET方法路由到handler的list方法
        RouterFunction<ServerResponse> route =
                        route(POST("/echo"), handler::echo)
                        .andRoute(GET("/get").and(queryParam("key", value -> true)), handler::get)
                        .andRoute(GET("/list"), handler::list);

        // GET请求，路径为/echo，Content-Type为ApplicationJson的条件
        method(HttpMethod.GET).and(path("/echo")).and(contentType(MediaType.APPLICATION_JSON));

        // GET或POST请求，且路径为/method，注意or、and之类的运算符优先级按照从左到右结合。每一次连接都相当于加了括号，忽略普通优先级的问题
        method(HttpMethod.GET).or(method(HttpMethod.POST)).and(path("/method"));

        // GET请求，路径为/get，请求参数包含key。
        GET("/get").and(queryParam("key", value -> true));
        return route;
    }

}
