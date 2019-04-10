package com.task.sync;

public class CTask implements MyTask {

	@Override
	public void doTask() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("C");
	}

}
