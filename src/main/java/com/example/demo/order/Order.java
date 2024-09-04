package com.example.demo.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.BaseEntity.BaseEntity;
import com.example.demo.constant.OrderStatus;
import com.example.demo.member.Member;
import com.example.demo.orderitem.OrderItem;

import groovy.transform.ToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name ="orders")
@Setter @Getter
@Entity @ToString
public class Order extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="member_id")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}
	
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		Order order = new Order();
		order.setMember(member);
		for (OrderItem orderItem : orderItemList) {
			order.addOrderItem(orderItem);
		}
		
		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}
	
	public int getTotalPrice() {
		return orderItems.stream()
						 .mapToInt(OrderItem::getTotalPrice)
						 .sum();
	}
	
	public void cancelOrder() {
		this.orderStatus = OrderStatus.CANCEL;
		orderItems.stream()
				.forEach(OrderItem::cancel);

	}

}
