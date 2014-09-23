package com.bdh.model;

import java.io.Serializable;

public class Coupon implements Serializable{

	private String CouponID;//�Ż�ȯID
	private String StoreID;//�̼�ID
	private String ProductID;//��ƷID
	private String ProductName;//��Ʒ����
	private String PriceLine;//������Ѽ۸�
	private String Price;//�Ż�ȯ��ֵ
	private String oldCouponPrice;//��¼�޸�ͬһ����Ʒ���Ż�ȯ��ԭ���Ż�ȯ�۸�
	private String oldRealPrice;//��¼�޸�ʵ����֮ǰ��ʵ����۸�
	private String start_time;//��ʼ����
	private String end_time;//��������
	private String flag;//״̬
	private String validity;//�Ƿ����1��Ч2����
	private String orderID;//����ID
	
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getOldCouponPrice() {
		return oldCouponPrice;
	}
	public void setOldCouponPrice(String oldCouponPrice) {
		this.oldCouponPrice = oldCouponPrice;
	}
	public String getOldRealPrice() {
		return oldRealPrice;
	}
	public void setOldRealPrice(String oldRealPrice) {
		this.oldRealPrice = oldRealPrice;
	}
	public String getCouponID() {
		return CouponID;
	}
	public void setCouponID(String couponID) {
		CouponID = couponID;
	}
	public String getStoreID() {
		return StoreID;
	}
	public void setStoreID(String storeID) {
		StoreID = storeID;
	}
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getPriceLine() {
		return PriceLine;
	}
	public void setPriceLine(String priceLine) {
		PriceLine = priceLine;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}

	
}
