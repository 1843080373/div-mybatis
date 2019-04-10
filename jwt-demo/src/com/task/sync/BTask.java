package com.task.sync;

public class BTask implements MyTask {

	@Override
	public void doTask() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("B");
	}

}
