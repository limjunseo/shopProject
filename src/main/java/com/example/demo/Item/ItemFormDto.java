package com.example.demo.Item;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.example.demo.constant.ItemSellStatus;
import com.example.demo.itemimg.ItemImgDto;

import groovy.transform.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @lombok.ToString
public class ItemFormDto {
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력")
	private String itemName;
	
	@NotNull(message = "가격 필수 입력")
	private Integer price;
	
	@NotBlank(message = "이름은 필수 입력")
	private String itemDetail;
	
	@jakarta.validation.constraints.NotNull(message = "재고는 필수 입력")
	private Integer stockNumber;
	
	private ItemSellStatus itemSellStatus;

	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

	private List<Long> itemImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	public Item createItem() {
	    return modelMapper.map(this, Item.class);
	}

	public static ItemFormDto of(Item item) {
	    return modelMapper.map(item, ItemFormDto.class);
	}


}
