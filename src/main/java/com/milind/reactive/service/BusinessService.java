package com.milind.reactive.service;

import com.milind.reactive.collector.lambada.TestLambada;
import com.milind.reactive.collector.Temp;
import com.milind.reactive.reactorservice.ReactorComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Component
public class BusinessService {

    @Autowired
    ReactorComponent legacyComponent;


    /**
     * This is the business service to compute sum - min - max in sequential Manner
     * @param arr
     * @return
     */
    public void businessService(Integer arr[], TestLambada <Temp> testLambada) throws InterruptedException {

        System.out.println("In BusinessService.businessService: "+Thread.currentThread());
        // Start the execution in sequence.
        Consumer<Integer> consumerForSum = sum -> {
            AtomicInteger result = new AtomicInteger();
            System.out.println("In consumerForSum: "+Thread.currentThread());
            result.addAndGet(sum);
            Consumer<Integer> consumerForMin = min -> {
                System.out.println("In consumerForMin: "+Thread.currentThread());
                result.set(result.intValue()-min);
                Consumer <Integer> consumerForMax = max -> {
                    System.out.println("In consumerForMax: "+Thread.currentThread());
                    result.set(result.intValue()-max);
                    testLambada.get(new Temp(result.get()));
                    System.out.println("End of consumerForMax: "+Thread.currentThread());
                };
                legacyComponent.getMax(arr).subscribe(consumerForMax);
                System.out.println("End of consumerForMin: "+Thread.currentThread());
            };
            legacyComponent.getMin(arr).subscribe(consumerForMin);
            System.out.println("End of consumerForSum: "+Thread.currentThread());
        };
        legacyComponent.nonBlockingSum(arr).subscribe(consumerForSum);
        System.out.println("Returning from BusinessService.businessService: "+Thread.currentThread());
    }


    /**
     * This is the business service to compute sum - min - max in Parallel
     * @param arr
     * @return
     */
    public void busServiceInParallel(Integer arr[], TestLambada <Temp> testLambada) throws InterruptedException {

        System.out.println("In BusinessService.busServiceInParallel: "+Thread.currentThread());
        Consumer<Tuple3<Integer,Integer,Integer>> consumer= a -> {
            System.out.println("In consumer For Parallel: "+Thread.currentThread());
            testLambada.get(new Temp(((Integer)a.get(0)-(Integer)a.get(1)-(Integer)a.get(2))));
        };
        //Running in Parallel and waiting for the execution to finish.
        Mono.zip(legacyComponent.nonBlockingSum(arr),legacyComponent.getMin(arr),legacyComponent.getMax(arr)).subscribe(consumer);
        System.out.println("Returning from BusinessService.busServiceInParallel: "+Thread.currentThread());
    }
}
