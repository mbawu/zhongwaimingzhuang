package com.zwmz.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable{

	private String OrderID;//������ID
	private String UserName;//�ύ�������û���
	private String OrderSubject;//����
	private String ProductNum;//��Ʒ����
	private String TotalPrice;//�ܼ�
	private String OrderCode;//������
	private String RealName;//�ջ�������
	private String Mobile;//�ջ����ֻ���
	private String Address;//�ջ�����ϸ��ַ
	private String Flag;//����״̬��1���µ���2��ȡ����3�����У�4������գ�5�������
	private String OutCreateTime;//�µ�ʱ��
	private String isPay;//�Ƿ�֧��0δ֧��1��֧��
	private String comments;//����״̬ 1�����ۣ�0δ����
	private String totalRecord;//������
	private String orderType;//Ҫ�鿴�Ķ������ͣ�1.�����2.��������3.���ջ���4.�����
	private String payWay;//payway		���ʽ��1����֧����2��������
	private ArrayList<Object> products;
	private String Freight;//�����˷�

	public String getFreight() {
		return Freight;
	}
	public void setFreight(String freight) {
		Freight = freight;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getOrderSubject() {
		return OrderSubject;
	}
	public void setOrderSubject(String orderSubject) {
		OrderSubject = orderSubject;
	}
	public String getProductNum() {
		return ProductNum;
	}
	public void setProductNum(String productNum) {
		ProductNum = productNum;
	}
	public String getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		TotalPrice = totalPrice;
	}
	public String getOrderCode() {
		return OrderCode;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getOutCreateTime() {
		return OutCreateTime;
	}
	public void setOutCreateTime(String outCreateTime) {
		OutCreateTime = outCreateTime;
	}
	public String getIsPay() {
		return isPay;
	}
	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	public ArrayList<Object> getProducts() {
		return products;
	}
	public void addProduct(Product product) {
		products.add(product);
	}
	
	public Order()
	{
		products=new ArrayList<Object>();
	}
}
