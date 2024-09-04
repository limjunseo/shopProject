package com.example.demo.cartItem;

import com.example.demo.BaseEntity.BaseEntity;
import com.example.demo.BaseTimeEntity.BaseTimeEntity;
import com.example.demo.Item.Item;
import com.example.demo.cart.Cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.utils.LogNode;

@Entity
@Setter @Getter
@lombok.ToString
public class CartItem extends BaseEntity  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JoinColumn(name = "item_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
	@JoinColumn(name = "cart_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Cart cart;
	
	private int count;
	
	public static com.example.demo.cartItem.CartItem createCartItem(Cart cart, Item item, int count) {
		com.example.demo.cartItem.CartItem cartItem = new com.example.demo.cartItem.CartItem();
		cartItem.setCart(cart);
		cartItem.setCount(count);
		cartItem.setItem(item);
		return cartItem;
	}
	
	public void addCount(int count) {
		this.count += count;
	}
	
	public void updateCount(int count) {
		this.count = count;
	}

}
