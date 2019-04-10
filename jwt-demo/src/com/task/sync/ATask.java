package com.task.sync;

public class ATask implements MyTask {

	@Override
	public void doTask() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("A");
	}

}
