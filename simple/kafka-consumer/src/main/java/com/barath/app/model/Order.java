package com.barath.app.model;

import java.io.Serializable;

import org.springframework.lang.Nullable;

public class Order implements Serializable{
	
	private static final long serialVersionUID = -7192476980499742639L;

	private Long orderId;
	
	private String productName;
	
	private String locationName;
	
	private String buyerName;
	
	private int price;
	
	@Nullable
	private OrderStatus orderStatus;
	
	public enum OrderStatus{
		SUCCESS,
		CANCELLED		
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	
	public Order() {
		super();
		
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", productName=" + productName + ", locationName=" + locationName
				+ ", buyerName=" + buyerName + ", price=" + price + ", orderStatus=" + orderStatus + "]";
	}


	
	
	
	

}
