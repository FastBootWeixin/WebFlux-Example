package com.mxixm.spring.webflux;

import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {

        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<List<String>>() {};
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(5);
        poolExecutor.schedule(() -> System.out.println(111), 1, TimeUnit.SECONDS);
        poolExecutor.schedule(() -> System.out.println(111), 1, TimeUnit.SECONDS);

        Thread.sleep(2000L);
        poolExecutor.schedule(() -> System.out.println(111), 10000, TimeUnit.SECONDS);
        Thread.sleep(1000000000L);
    }

}
