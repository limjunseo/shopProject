package com.example.demo.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	CartItem findByCartIdAndItemId(Long cartId, Long itemId);
	
	@Query("select new com.example.demo.cartItem.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl) " + 
			"from CartItem ci, ItemImg im " +
			"join ci.item i " +
			"where ci.cart.id = :cartId " + 
			"and im.item.id = ci.item.id " +
			"and im.repimgYn = 'Y' " + 
			"order by ci.regTime desc"
			)
	java.util.List<CartDetailDto> findCartDetailDtoList(Long cartId);
	
}


//
//public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
//	this.cartItemId = cartItemId;
//	this.itemNm = itemNm;
//	this.price = price;
//	this.count = count;
//	this.imgUrl = imgUrl;
//}


//private Long cartItemId;
//
//private String itemNm;
//
//private int price;
//
//private int count;
//
//private String imgUrl;