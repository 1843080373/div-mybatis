package com.batis.test;

import java.util.Date;

public class SimpleVO {
	private int i;
	private float f;
	private Date date;
    // ���췽��
    public SimpleVO(int i, float f, Date date) {
        this.i = i;
        this.f = f;
        this.date = date;
    }
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public float getF() {
		return f;
	}
	public void setF(float f) {
		this.f = f;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
    
}
