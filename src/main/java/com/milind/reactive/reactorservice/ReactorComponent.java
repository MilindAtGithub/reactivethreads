package com.milind.reactive.reactorservice;

import com.milind.reactive.service.ComputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * This is the Component which will expose Reactor Services
 */
@Component
public class ReactorComponent {

    @Autowired
    ComputationService computationService;

    private Scheduler scheduler = Schedulers.newElastic("ReactiveScheduler");

    /**
     * This will return sum in blocking manner.
     * @param arr
     * @return
     * @throws InterruptedException
     */
    public Integer blockingSum( final Integer arr[]) throws InterruptedException {

        return computationService.getSum(arr);
    }

    /**
     * This will return sum in non blocking manner
     * @param arr
     * @return
     * @throws InterruptedException
     */
    public Mono<Integer> nonBlockingSum(final Integer arr[]) throws InterruptedException {

        System.out.println("In ReactorComponent.nonBlockingSum: "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getSum(arr)).subscribeOn(this.scheduler);
        System.out.println("Returning form ReactorComponent.nonBlockingSum: "+Thread.currentThread());
        return m;
    }

    /**
     * This will return Min in non blocking manner
     * @param arr
     * @return
     */
    public  Mono<Integer> getMin(final Integer arr[]){
        System.out.println("In ReactorComponent.getMin : "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getMin(arr)).subscribeOn(this.scheduler);
        System.out.println("Returning from ReactorComponent.getMin: "+Thread.currentThread());
        return m;
    }

    /**
     * This will return max in non blocking manner
     * @param arr
     * @return
     */
    public  Mono<Integer> getMax(final Integer arr[]){
        System.out.println("In ReactorComponent.getMax : "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getMax(arr)).subscribeOn(this.scheduler);
        System.out.println("Returning from ReactorComponent.getMax: "+Thread.currentThread());
        return m;
    }
}
