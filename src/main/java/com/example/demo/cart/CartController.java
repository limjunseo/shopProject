package com.example.demo.cart;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;import java.util.function.LongBinaryOperator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.CartOrderDto.CartOrderDto;
import com.example.demo.cartItem.CartDetailDto;
import com.example.demo.cartItem.CartItemDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@PostMapping(value = "/cart")
	@ResponseBody
	public ResponseEntity<?> order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			java.util.List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString()	, HttpStatus.BAD_REQUEST);
		}
		
		String email = principal.getName();
		Long cartItemId;
		
		try {
			cartItemId = cartService.addCart(cartItemDto, email);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}

	
	@GetMapping(value = "/cart")
	public String orderHist(Principal principal, ModelMap model) {
		List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
		model.put("cartItems", cartDetailList);
		return "cart/cartList";
	}
	
	@PatchMapping(value ="/cartItem/{cartItemId}")
	@ResponseBody
	public ResponseEntity<?> updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count , Principal principal) {
		if(count <= 0) {
			return new ResponseEntity<String>("최소 1개 이상", HttpStatus.BAD_REQUEST);
		} else if(!cartService.validateCartItem(cartItemId, principal.getName())) {
			return new ResponseEntity<String>("수정 권한이 없음", HttpStatus.BAD_REQUEST);
		} 
		
		cartService.updateCartItemCount(cartItemId, count);
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
		
	}
	
	@DeleteMapping(value ="/cartItem/{cartItemId}")
	@ResponseBody
	public ResponseEntity<?> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
		if(!cartService.validateCartItem(cartItemId, principal.getName())) {
			return new ResponseEntity<String>("수정 권한이 없음", HttpStatus.FORBIDDEN);
		}
		
		cartService.deleteCartItem(cartItemId);
		
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}
	
	
	@PostMapping(value ="/cart/orders")
	@ResponseBody
	public ResponseEntity<?> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {
		List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
		
		if(cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
			return new ResponseEntity<String>("주문할 상품을 선택하세요", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Long>(cartOrderDto.getCartItemId(), HttpStatus.OK);
	}
	
	
	
	
	
	
}
