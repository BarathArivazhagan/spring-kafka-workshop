package com.barath.app.model;

import java.io.Serializable;

public class Order implements Serializable{
	
	private static final long serialVersionUID = 7069175996375597807L;

	private Long orderId;
	
	private String productName;
	
	private String locationName;
	
	private String buyerName;

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

	public Order(Long orderId, String productName, String locationName, String buyerName) {
		super();
		this.orderId = orderId;
		this.productName = productName;
		this.locationName = locationName;
		this.buyerName = buyerName;
	}

	public Order() {
		super();
		
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", productName=" + productName + ", locationName=" + locationName
				+ ", buyerName=" + buyerName + "]";
	}
	
	
	
	
	

}
