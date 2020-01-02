package com.milind.reactive.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;

@Service
public class ComputationService {

    /**
     * This will compute sum for given integer array
     * @param arr
     * @return
     */
    public Integer getSum(final Integer arr[]) {
        System.out.println("In ComputationService.getSum: " + Thread.currentThread());
        Integer count = 0;
        for (int i = 0; i < arr.length; i++) {
            count += arr[i];
        }
        System.out.println("Returning from ComputationService.getSum: " + Thread.currentThread());
        return count;
    }

    /**
     * This will return Min Value from Integer Array
     * 
     * @param arr
     * @return
     */
    public Integer getMin(final Integer arr[]) {
        return Arrays.stream(arr).min(Comparator.comparing(Integer::intValue)).get();
    }

    /**
     * This will return Max Value from Integer Array
     * 
     * @param arr
     * @return
     */
    public Integer getMax(final Integer arr[]) {
        return Arrays.stream(arr).max(Comparator.comparing(Integer::intValue)).get();
    }
}
