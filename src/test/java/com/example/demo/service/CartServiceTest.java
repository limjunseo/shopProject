package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.cart.CartService;
import com.example.demo.cartItem.CartItem;
import com.example.demo.cartItem.CartItemDto;
import com.example.demo.cartItem.CartItemRepository;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@PersistenceContext
	EntityManager em;
	
	
	@Test
	@DisplayName("장바구니 담기 테스트")
	@Transactional
	public void addCart() {
		Item item = saveItem();
		Member member = saveMember();
		CartItemDto cartItemDto = new CartItemDto();
		cartItemDto.setCount(10);
		cartItemDto.setItemId(item.getId());
		
		Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
		
		em.flush();
		em.clear();
		
		CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cartItemId, item.getId());
		assertEquals(cartItem.getItem().getId(), item.getId());
		assertEquals(cartItemDto.getCount(), 10);
		
	}
	
	public Item saveItem(){
	    Item item = new Item();
	    item.setItemName("테스트 상품");
	    item.setPrice(10000);
	    item.setItemDetail("테스트 상품 상세 설명");
	    item.setItemSellStatus(ItemSellStatus.SELL);
	    item.setStockNumber(100);
	    return itemRepository.save(item);
	}

	public Member saveMember(){
	    Member member = new Member();
	    member.setEmail("test@test.com");
	    return memberRepository.save(member);
	}


}
