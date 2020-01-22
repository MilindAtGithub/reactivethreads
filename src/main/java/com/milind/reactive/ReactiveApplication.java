package com.milind.reactive;

import com.milind.reactive.collector.lambada.TestLambada;
import com.milind.reactive.reactorservice.ReactorComponent;
import com.milind.reactive.service.BusinessService;
import com.milind.reactive.collector.Temp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.function.Consumer;

@SpringBootApplication
public class ReactiveApplication {
	/**
	 * Main Method or application start point
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx =SpringApplication.run(ReactiveApplication.class, args);

		blockingCall(ctx);

		nonBlockingCall(ctx);

		nonBlockingCallAndStartNewThread(ctx);

		callBusinessWorkflowSerial(ctx);

		callBusinessWorkflowParallel(ctx);

		((ConfigurableApplicationContext) ctx).close();

	}
	/**
	 * This is the test for blocking call where the main/request thread will get blocked
	 * @param ctx
	 * @throws InterruptedException
	 */
	public static void blockingCall(ApplicationContext ctx) throws InterruptedException {

		System.out.println("In ReactiveApplication.blockingCall: "+Thread.currentThread());
		Integer t = ctx.getBean(ReactorComponent.class).nonBlockingSum(new Integer[] {4,78,4,676,3,45}).block();
		System.out.println("Returning from ReactiveApplication.blockingCall result= " + t);
	}

	/**
	 * This is the test for Non Blocking call where the request/main thread will not get blocked.
	 * @param ctx
	 * @throws InterruptedException
	 */
	public static void nonBlockingCall(ApplicationContext ctx) throws InterruptedException {

		System.out.println("In ReactiveApplication.nonBlockingCall: "+Thread.currentThread());
		Consumer<Integer> display = a -> {
			System.out.println("In Consumer/Lambada result= "+ a + " and Thread: "+Thread.currentThread());
		};
		ctx.getBean(ReactorComponent.class).nonBlockingSum(new Integer[] {4,78,4,676,3,45}).subscribe(display);
		System.out.println("Returning from ReactiveApplication.nonBlockingCall: "+Thread.currentThread());
	}

	/**
	 * This is the test for Non Blocking Call and starting the new threads on need basis
	 * @param ctx
	 * @throws InterruptedException
	 */
	public static void nonBlockingCallAndStartNewThread(ApplicationContext ctx) throws InterruptedException {

		System.out.println("In ReactiveApplication.nonBlockingCallAndStartNewThread: "+Thread.currentThread());
		Runnable r =  new Runnable() {
			@Override
			public void run() {
				System.out.println("In New Thread Run method: "+Thread.currentThread());
				try {
					Consumer<Integer> consumer = C -> print(C);
					ctx.getBean(ReactorComponent.class).nonBlockingSum(
							new Integer[] {400000,780,40,6760,30,3456450}).subscribe(consumer);

				 	Runnable r1 = new Runnable() {
						@Override
						public void run() {
							System.out.println("Started Another Thread: "+Thread.currentThread());
							for(int i =0; i <5; i++){
								i++;
							}
							System.out.println("End of Another Thread: "+Thread.currentThread());
						}
					};
					new Thread(r1).start();
					System.out.println("End of New Thread: "+Thread.currentThread());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
		System.out.println("Returning from  ReactiveApplication.nonBlockingCallAndStartNewThread: "+Thread.currentThread());

	}

	/**
	 * This is the test for reactive business flow where business requirement is sum of integers - min of integers - max of integers
	 * This test will execute each step in serial and finally give the result out.
	 * @param ctx
	 * @throws InterruptedException
	 */
	public static  void callBusinessWorkflowSerial(ApplicationContext ctx) throws InterruptedException {

		System.out.println("In ReactiveApplication.callBusinessWorkflowSerial: "+Thread.currentThread());
		TestLambada<Temp> tempTestLambada = test -> {
			System.out.println("Printing result in tempTestLambada= " + test.getA()+ " and Thread: "+Thread.currentThread());
		};
		ctx.getBean(BusinessService.class).businessService(new Integer[] {4,78,4,676,3,45},tempTestLambada);
		System.out.println("Returning from ReactiveApplication.callBusinessWorkflowSerial: "+Thread.currentThread());
	}

	/**
	 * This is the test for reactive business flow where business requirement is sum of integers - min of integers - max of integers
	 * This test will execute each step in parallel and finally give the result out.
	 * @param ctx
	 * @throws InterruptedException
	 */
	public static  void callBusinessWorkflowParallel(ApplicationContext ctx) throws InterruptedException {

		System.out.println("In ReactiveApplication.callBusinessWorkflowParallel: "+Thread.currentThread());
		TestLambada<Temp> tempTestLambada = test -> {
			System.out.println("In Parallel Business Workflow Lambada Result: "+test.getA()+" and Thread: "+Thread.currentThread());
		};
		ctx.getBean(BusinessService.class).busServiceInParallel(new Integer[] {4,78,4,676,3,45},tempTestLambada);
		System.out.println("Returning from ReactiveApplication.callBusinessWorkflowParallel: "+Thread.currentThread());
	}
	/**
	 * Method for printing purpose
	 * @param a
	 */
	public static void  print(Integer a){
		System.out.println("In Consumer/Lambada Result= " + a+ " Thread: "+Thread.currentThread());
	}

	


}
