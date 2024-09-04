package com.example.demo.orderitem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrderItemDto {

	public OrderItemDto(OrderItem orderItem, String imgUrl) {
		this.count = orderItem.getCount();
		this.itemNm = orderItem.getItem().getItemName();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
	}

	private String itemNm;
	
	private int count;
	
	private int orderPrice;
	
	private String imgUrl;
	

}
