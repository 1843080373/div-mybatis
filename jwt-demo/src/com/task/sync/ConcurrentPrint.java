package com.task.sync;
import java.util.concurrent.Semaphore;
 
//ʹ���ź���
public class ConcurrentPrint {
	// ������Դ��������ʼΪ1
	private static Semaphore s1 = new Semaphore(1);
	private static Semaphore s2 = new Semaphore(1);
	private static Semaphore s3 = new Semaphore(1);
	
	static MyTask aTask=new ATask();
	static MyTask bTask=new BTask();
	static MyTask cTask=new CTask();
	Thread t1 = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {
					s1.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				aTask.doTask();
				s2.release();
			}
		}
	});
	Thread t2 = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {
					s2.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bTask.doTask();
				s3.release();
			}
		}
	});
	Thread t3 = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {
					s3.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cTask.doTask();
				s1.release();
			}
		}
	});
 
	public void fun() throws InterruptedException {
		// ��ռ�����BC���̵߳��ź�������
		// ��ֻ�ܴ����A���߳̿�ʼ����ȡ�ź���A��Ȼ���ͷ�B-��ȡB-�ͷ�C-��ȡC-�ͷ�A���ɴ��γ�ѭ��
		s2.acquire();
		s3.acquire();
		t2.start();
		t3.start();
		t1.start();
	}
 
	public static void main(String[] args) throws InterruptedException {
		ConcurrentPrint cp = new ConcurrentPrint();
		cp.fun();
	}
}