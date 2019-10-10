package com.milind.reactive.service;

import com.milind.reactive.collector.lambada.TestLambada;
import com.milind.reactive.collector.Temp;
import com.milind.reactive.reactorservice.ReactorComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Test Class
 */
@Component
public class TestComponent {

    @Autowired
    ReactorComponent legacyComponent;

    public void printSum(Integer arr[]) throws InterruptedException {
        Consumer<Integer> integerConsumer = a -> {
            Temp t = new Temp(a);
            System.out.println("Value of which is set = " + a);
        };
        legacyComponent.nonBlockingSum(arr).subscribe(integerConsumer);
    }

    public void printSum(Integer arr[], TestLambada lambada) throws InterruptedException {
        Consumer<Integer> integerConsumer = a -> {
            System.out.println("Value of which is set = " + a);
            lambada.get(new Temp(a));
        };
        legacyComponent.nonBlockingSum(arr).subscribe(integerConsumer);
    }
}


