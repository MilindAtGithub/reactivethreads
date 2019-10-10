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
    public Integer blockingSum( Integer arr[]) throws InterruptedException {

        return computationService.getSum(arr);
    }

    /**
     * This will return sum in non blocking manner
     * @param arr
     * @return
     * @throws InterruptedException
     */
    public Mono<Integer> nonBlockingSum(Integer arr[]) throws InterruptedException {

        System.out.println("!!... In Non Block Addition: "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getSum(arr)).subscribeOn(this.scheduler);
        System.out.println("!!... Returning for Non Block Addition: "+Thread.currentThread());
        return m;
    }

    /**
     * This will return Min in non blocking manner
     * @param arr
     * @return
     */
    public  Mono<Integer> getMin(Integer arr[]){
        System.out.println("In Non Block Get Min : "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getMin(arr)).subscribeOn(this.scheduler);
        System.out.println("Returning for the get Min Call: "+Thread.currentThread());
        return m;
    }

    /**
     * This will return max in non blocking manner
     * @param arr
     * @return
     */
    public  Mono<Integer> getMax(Integer arr[]){
        System.out.println("In Non Block Get Max : "+Thread.currentThread());
        Mono<Integer> m = Mono.fromSupplier(() -> this.computationService.getMax(arr)).subscribeOn(this.scheduler);
        System.out.println("Returning for the get Max Call: "+Thread.currentThread());
        return m;
    }
}
