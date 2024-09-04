package com.example.demo.orderhis;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.constant.OrderStatus;
import com.example.demo.order.Order;
import com.example.demo.orderitem.OrderItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class OrderHisDto {
	
	public OrderHisDto(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
	}
	
	private Long orderId;
	
	private String orderDate;
	
	private OrderStatus orderStatus;
	
	private List<OrderItemDto> orderItemDtoList = new ArrayList<OrderItemDto>();
	
	public void addOrderItemDto(OrderItemDto orderItemDto) {
		this.orderItemDtoList.add(orderItemDto);
	}
	
}
