package com.example.demo.order;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.orderhis.OrderHisDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class OrderController {
	private final OrderService orderService;
	
	@PostMapping(value = "/order")
	public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto, 
			BindingResult bindingResult, Principal principal){
		
		if(bindingResult.hasErrors()) {

			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String name = principal.getName();
		Long orderId;
		log.info("orderDto :  {}" , orderDto);
		try {

			orderId = orderService.order(orderDto, name);
			log.info("여기서 주문은하니?");
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/orders", "/orders/{page}"})
	public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, ModelMap model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 4);
		Page<OrderHisDto> hisDtoList = orderService.getOrderList(principal.getName(), pageable);
		
		model.put("orders", hisDtoList);
		model.put("page", pageable.getPageNumber());
		model.put("maxPage", 5);
		return "order/orderHist";
	}
	
	@PostMapping("/order/{orderId}/cancel")
	public @ResponseBody ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		if(orderService.validateOrder(orderId, principal.getName())) {
			return new ResponseEntity<String>("주문 취소 권한이 없습니다", HttpStatus.FORBIDDEN);
		}
		orderService.cancelOrder(orderId);
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}

}














