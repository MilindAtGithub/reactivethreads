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

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx =SpringApplication.run(ReactiveApplication.class, args);

		blockingCall(ctx);

		nonBlockingCall(ctx);

		nonBlockingMainThread(ctx);

		callBusinessWorkflowSerial(ctx);

		callBusinessWorkflowParallel(ctx);

		((ConfigurableApplicationContext) ctx).close();

	}

	public static void blockingCall(ApplicationContext ctx) throws InterruptedException {

		System.out.println("~~... Blocking Call Start: "+Thread.currentThread());
		Integer t = ctx.getBean(ReactorComponent.class).nonBlockingSum(new Integer[] {4,78,4,676,3,45}).block();
		System.out.println("~~... Print this after the call: "+Thread.currentThread());
		System.out.println("~~... t = " + t);
	}

	public static void nonBlockingCall(ApplicationContext ctx) throws InterruptedException {

		System.out.println("~~... NonBlocking Call Start: "+Thread.currentThread());
		Consumer<Integer> display = a -> {
			System.out.println("##... Display from Consumer: "+ a + " Thread: "+Thread.currentThread());
		};
		ctx.getBean(ReactorComponent.class).nonBlockingSum(new Integer[] {4,78,4,676,3,45}).subscribe(display);
		System.out.println("~~... Print this after the call: "+Thread.currentThread());
	}

	public static void nonBlockingMainThread(ApplicationContext ctx) throws InterruptedException {

		System.out.println("~~... nonBlockingMainThread Call Start: "+Thread.currentThread());
		Runnable r =  new Runnable() {
			@Override
			public void run() {
				System.out.println("$$... In New Thread Run: "+Thread.currentThread());
				Integer t = null;
				try {
					Consumer<Integer> consumer = C -> print(C);
					ctx.getBean(ReactorComponent.class).nonBlockingSum(
							new Integer[] {400000,780,40,6760,30,3456450}).subscribe(consumer);
					System.out.println("$$... In New Thread Run: "+Thread.currentThread() +" Doing Other work");
					Runnable r1 = new Runnable() {
						@Override
						public void run() {
							for(int i =0; i <5; i++){
								System.out.println("%%... In Another New Thread:" +Thread.currentThread()+"-"+i);
							}
						}
					};
					new Thread(r1).start();
					System.out.println("$$... In New Thread After Spawn Another Thread: "+Thread.currentThread());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
		System.out.println("~~... Printing from the Main Thread: "+Thread.currentThread());

	}


	public static  void callBusinessWorkflowSerial(ApplicationContext ctx) throws InterruptedException {

		System.out.println("~~... In callBusinessWorkflowSerial Call : "+Thread.currentThread());
		TestLambada<Temp> tempTestLambada = test -> {
			System.out.println("&&... In Serial Business Workflow Result Lambada: "+Thread.currentThread());
			System.out.println("&&... Business Result= " + test.getA());
		};
		ctx.getBean(BusinessService.class).businessService(new Integer[] {4,78,4,676,3,45},tempTestLambada);
		System.out.println("~~... Printing Before Return from callBusinessWorkflowSerial: "+Thread.currentThread());
	}

	public static  void callBusinessWorkflowParallel(ApplicationContext ctx) throws InterruptedException {

		System.out.println("~~... In callBusinessWorkflowParallel Call : "+Thread.currentThread());
		TestLambada<Temp> tempTestLambada = test -> {
			System.out.println("&&... In Parallel Business Workflow Result Lambada: "+Thread.currentThread());
			System.out.println("&&... Parallel Business Result= " + test.getA());
		};
		ctx.getBean(BusinessService.class).businessService(new Integer[] {4,78,4,676,3,45},tempTestLambada);
		System.out.println("~~... Printing Before Return from callBusinessWorkflowParallel: "+Thread.currentThread());
	}

	public static void  print(Integer a){
		System.out.println("^^... a = " + a+ " Thread: "+Thread.currentThread());
	}

}
