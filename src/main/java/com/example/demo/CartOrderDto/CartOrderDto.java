package com.example.demo.CartOrderDto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CartOrderDto {
	
	private Long cartItemId;
	
	private List<CartOrderDto> cartOrderDtoList;


	

}
