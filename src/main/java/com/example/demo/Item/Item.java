package com.example.demo.Item;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.example.demo.BaseEntity.BaseEntity;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.exception.OutOfStockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name ="item")
public class Item extends BaseEntity {

	public Item() {}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="item_id")
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String itemName;

	@Column(name ="price", nullable = false)
	private int price;
	
	@Column(nullable = false)
	private int stockNumber;
	
	@Lob
	@Column(nullable = false)
	private String itemDetail;
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;
	
	public Item(String itemName, int price, int stockNumber, String itemDetail) {
		this.itemName = itemName;
		this.price = price;
		this.stockNumber = stockNumber;
		this.itemDetail = itemDetail;
	}

	//logic	
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber = stockNumber;
		if(restStock < 0 ) {
			throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
		}
		
		this.stockNumber = restStock;
	}
	
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
	
	
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemName = itemFormDto.getItemName();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
}



