package com.daka.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueUtil {
	
	public  static final int QUEUE_COUNT = 100;
	
	public static final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(QUEUE_COUNT);

	public static final ExecutorService executor = new ThreadPoolExecutor(20, 100, 10L, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(200));
	
}
