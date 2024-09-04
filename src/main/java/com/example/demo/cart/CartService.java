package com.example.demo.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.CartOrderDto.CartOrderDto;
import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.cartItem.CartDetailDto;
import com.example.demo.cartItem.CartItem;
import com.example.demo.cartItem.CartItemDto;
import com.example.demo.cartItem.CartItemRepository;
import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.order.OrderDto;
import com.example.demo.order.OrderService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;
	private final CartItemRepository cartItemRepository;
	private final MemberRepository memberRepository;
	private final OrderService orderService;
	
	@Transactional
	public Long addCart(CartItemDto cartItemDto, String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		if(cart == null) {
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}
		Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
		
		//기존 카트 아이템이 없는경우
		if(savedCartItem == null) {
			CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
			cartItemRepository.save(cartItem);
			return cartItem.getId();
		} else { //기존 카트 아이템이 있는 경우
			savedCartItem.addCount(cartItemDto.getCount());
			return savedCartItem.getId();
		}
		
	}
	
	@Transactional
	public List<CartDetailDto> getCartList(String email) {
		List<CartDetailDto> cartDetailDtoList = new ArrayList<CartDetailDto>();
		Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		if(cart == null) {
			return cartDetailDtoList;
		} 
		
		cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
		return cartDetailDtoList;
	}
	
	@Transactional(readOnly = true)
	public boolean validateCartItem(Long cartItemId, String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = cartItem.getCart().getMember();
		
		if(!member.getEmail().equals(savedMember.getEmail())) {
			return false;
		}
		return true;
	}
	
	@Transactional
	public void updateCartItemCount(Long cartItemId, int count) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItem.updateCount(count);
	}
	
	@Transactional
	public void deleteCartItem(Long cartItemId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItemRepository.delete(cartItem);
	}
	
	@Transactional
	public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
		
		for(CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
			
			OrderDto orderDto = new OrderDto();
			orderDto.setItemId(cartItem.getItem().getId());
			orderDto.setCount(cartItem.getCount());
			orderDtoList.add(orderDto);
		}
		
		Long orderId = orderService.orders(orderDtoList, email);
		
		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
			cartItemRepository.delete(cartItem);
		}
		return orderId;
	}
	
	
	

}
