package com.milind.reactive.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Service
public class ComputationService {

    /**
     * This will compute sum for given integer array
     * @param arr
     * @return
     */
    public Integer getSum(Integer arr [] )  {
        System.out.println("@@... In Addition Service: "+Thread.currentThread());
        Integer count = 0;
        for(int i =0; i< arr.length; i++){
            count+= arr[i];
        }
        return count;
    }

    /**
     * This will return Min Value from Integer Array
     * @param arr
     * @return
     */
    public Integer getMin(Integer arr[]){
        System.out.println("!!... In Get Minimum Service: "+Thread.currentThread());
        return Arrays.stream(arr).min(Comparator.comparing(Integer::intValue)).get();
    }

    /**
     * This will return Max Value from Integer Array
     * @param arr
     * @return
     */
    public Integer getMax(Integer arr[]){
        System.out.println("!!... In Get Maximum Service: "+Thread.currentThread());
        return Arrays.stream(arr).max(Comparator.comparing(Integer::intValue)).get();
    }
}
