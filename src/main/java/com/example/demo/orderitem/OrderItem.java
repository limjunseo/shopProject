package com.example.demo.orderitem;

import com.example.demo.BaseEntity.BaseEntity;
import com.example.demo.Item.Item;
import com.example.demo.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
public class OrderItem extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="order_id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="item_id")
	private Item item;
	
	private int orderPrice;
	
	private int count;
	
	public static OrderItem createOrderItem(Item item, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setCount(count);
		orderItem.setOrderPrice(item.getPrice());
		
		item.removeStock(count);
		return orderItem;
	}
	
	public void cancel() {
		this.getItem().addStock(count);
	}
	
	public int getTotalPrice() {
		return orderPrice * count;
	}




	


}
