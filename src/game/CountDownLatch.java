package game;

import java.io.Serializable;

public class CountDownLatch implements Serializable {
    private int count;
    public CountDownLatch(int count) {
        this.count = count;
    }
    public synchronized void await() throws InterruptedException {
        while(count > 0){
            wait();
        }
    }
    public synchronized void countDown(){
        count--;
        if(count == 0) {
            notifyAll();
        }
    }
}