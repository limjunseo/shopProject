package com.example.demo.cartItem;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CartItemDto {
	
	@jakarta.validation.constraints.NotNull(message = "상품 아이디는 필수 입력값입니다")
	private Long itemId;
	
	@Min(value = 1 , message = "최소 1개 이상 담아주세요")
	private int count;

}
