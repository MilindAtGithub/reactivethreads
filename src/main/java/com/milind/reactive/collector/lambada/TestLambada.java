package com.milind.reactive.collector.lambada;

import com.milind.reactive.collector.Temp;

/**
 * Lambada to receive the call back events
 * @param <T>
 */
@FunctionalInterface
public interface TestLambada < T extends Temp> {

     void get(T a);
}
