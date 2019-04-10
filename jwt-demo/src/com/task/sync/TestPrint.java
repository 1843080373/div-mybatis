package com.task.sync;
//第一种方法，使用Object的wait和notifyAll方法
public class TestPrint {
	static int count = 0;
	static final Object obj = new Object();
	static MyTask aTask=new ATask();
	static MyTask bTask=new BTask();
	static MyTask cTask=new CTask();
	Thread t1 = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (obj) {
					if (count % 3 == 0) {
						aTask.doTask();
						count++;
						obj.notifyAll();
					} else
						try {
							obj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		}
	});
	Thread t2 = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (obj) {
					if (count % 3 == 1) {
						bTask.doTask();
						count++;
						obj.notifyAll();
					} else
						try {
							obj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		}
	});
	Thread t3 = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (obj) {
					if (count % 3 == 2) {
						cTask.doTask();
						count++;
						obj.notifyAll();
					} else
						try {
							obj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		}
	});
 
	public void fun() {
		t3.start();
		t1.start();
		t2.start();
	}
 
	public static void main(String[] args) {
		TestPrint tp = new TestPrint();
		tp.fun();
	}
}