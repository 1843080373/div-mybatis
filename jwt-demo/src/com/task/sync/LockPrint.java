package com.task.sync;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
//使用lock和Condition，更加灵活
public class LockPrint {
	private static int count = 0;
	private Lock lock = new ReentrantLock();
	Condition c1 = lock.newCondition();
	Condition c2 = lock.newCondition();
	Condition c3 = lock.newCondition();
	
	static MyTask aTask=new ATask();
	static MyTask bTask=new BTask();
	static MyTask cTask=new CTask();
	Thread t1 = new Thread(new Runnable() {
 
		@Override
		public void run() {
			while (true) {
				try {
					lock.lock();
					while (count % 3 != 0)
						c1.await();
					aTask.doTask();
					count++;
					c2.signal();// 唤醒条件2
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
 
		}
	});
	Thread t2 = new Thread(new Runnable() {
 
		@Override
		public void run() {
			while (true) {
				try {
					lock.lock();
					while (count % 3 != 1)
						c2.await();
					bTask.doTask();
					count++;
					c3.signal();// 唤醒条件3
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
 
		}
	});
	Thread t3 = new Thread(new Runnable() {
 
		@Override
		public void run() {
			while (true) {
				try {
					lock.lock();
					while (count % 3 != 2)
						c3.await();
					cTask.doTask();
					count++;
					c1.signal();// 唤醒条件1
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
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
		LockPrint lp = new LockPrint();
		lp.fun();
	}
}