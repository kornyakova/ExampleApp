package com.example.demo.queue;

import java.util.LinkedList;
import java.util.Queue;


public class ArticleQueue{

	
	private final Queue<String> queue = new LinkedList<>();
    private final Object FULL_QUEUE = new Object();
    private final Object EMPTY_QUEUE = new Object();

    public ArticleQueue(int maxSize) {
    }
	public boolean isFull() {
		return false;
	}

	public void notifyAllForEmpty() {
	    synchronized (EMPTY_QUEUE) {
	        EMPTY_QUEUE.notify();
	    }
	}

	public void notifyAllForFull() {
	    synchronized (FULL_QUEUE) {
	        FULL_QUEUE.notifyAll();
	    }
	}

	public void waitOnFull() throws InterruptedException {
	    synchronized (FULL_QUEUE) {
	        FULL_QUEUE.wait();
	    }
	}
	public void waitOnEmpty() throws InterruptedException {
		synchronized (EMPTY_QUEUE) {
	        EMPTY_QUEUE.wait();
	    }
	}
	
	public void add(String message) {
	    synchronized (queue) {
	        queue.add(message);
	    }
	}

	public String remove() {
	    synchronized (queue) {
	        return queue.poll();
	    }
	}
	public boolean isEmpty() {
		return queue.isEmpty();
	}


}
