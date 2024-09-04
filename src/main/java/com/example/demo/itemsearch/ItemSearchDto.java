package com.example.demo.itemsearch;

import com.example.demo.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {
	
	private String searchDateType;
	
	private ItemSellStatus searchSellStatus;
	
	private String searchBy; //itemName, createdBy
	
	private String searchQuery="";

}
