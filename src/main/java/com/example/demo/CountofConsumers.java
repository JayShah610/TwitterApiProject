package com.example.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class CountofConsumers {
    private static AtomicInteger consumerCount = new AtomicInteger(0);

    public synchronized void decrease(){
        int i = consumerCount.get();
        while(consumerCount.get()<=0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        consumerCount.decrementAndGet();
    }

    public synchronized void increase(){
        consumerCount.incrementAndGet();
        if(consumerCount.get()>0) {
            this.notifyAll();
        }
    }

}
