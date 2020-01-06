package com.milind.reactive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.milind.reactive.reactorservice.ReactorComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class ReactiveWebServer {

    public static void main(String args[]) throws Exception {
        ApplicationContext ctx = SpringApplication.run(ReactiveWebServer.class, args);

        startServer(ctx);
    }

    private static void startServer(ApplicationContext ctx) throws Exception{
        ServerSocket server = new ServerSocket(9090);
        System.out.println("Listening for connection on port 9090 ....");
        while (true) { 
            try (Socket socket = server.accept()) {
                Consumer<Integer> response = a -> {
                    String responseStr  = "HTTP/1.1 200 OK\r\n\r\n"+"Result= "+ a + " and Thread: "+Thread.currentThread();
                    try {
                        socket.getOutputStream().write(responseStr.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                Random random = new Random();
                ctx.getBean(ReactorComponent.class).nonBlockingSum(new Integer[] {
                        random.nextInt(), random.nextInt(), random.nextInt()
                }).subscribe(response);
                // Sleeping to Keep The Socket Open and depict event loop
                TimeUnit.MILLISECONDS.sleep(200);
                System.out.println("After Sleep Thread: "+ Thread.currentThread());
            } 
        }
    }
}