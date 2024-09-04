package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.constant.OrderStatus;
import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.order.Order;
import com.example.demo.order.OrderDto;
import com.example.demo.order.OrderRepository;
import com.example.demo.order.OrderService;
import com.example.demo.orderitem.OrderItem;

import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations ="classpath:application-test.properties")
@lombok.extern.slf4j.Slf4j
public class OrderserviceTest {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext 
	private EntityManager em;
	
	
	@Test
	@DisplayName("주문 테스트")
	@Transactional
	public void order() {
		Item item = saveItem(); 
		Member member = saveMember(); 
		
		OrderDto orderDto = new OrderDto();
		orderDto.setCount(10);
		orderDto.setItemId(item.getId());
		
		Long orderId = orderService.order(orderDto, member.getEmail());
		log.info("orderId : {}",orderId);
		em.flush();
		em.clear();
		
		Order findOrder = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		log.info("findOrder : {} and {} ", findOrder.getMember() , findOrder.getOrderStatus());
		log.info("findorder의 orderItems의 개수: {}" , findOrder.getOrderItems().size());
		log.info("findorder의 totalprice: {} " , findOrder.getTotalPrice());
		List<OrderItem> orderItemList = findOrder.getOrderItems();
		int totalPrice = orderDto.getCount() * item.getPrice();
		
		assertEquals(totalPrice, findOrder.getTotalPrice());
		
	}
	
	@Test
	@DisplayName("주문 취소 테스트")
	@Transactional
	public void cancelOrder() {
		Item item = saveItem();
		Member member = saveMember();
		
		OrderDto orderDto = new OrderDto();
		orderDto.setCount(10);
		orderDto.setItemId(item.getId());
		Long orderId = orderService.order(orderDto, member.getEmail());
		
		em.flush();
		em.clear();
		
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		orderService.cancelOrder(orderId);
		
		assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
	}
	
	public Item saveItem() {
	    Item item = new Item();
	    item.setItemName("테스트 상품");
	    item.setPrice(10000);
	    item.setItemDetail("테스트 상품 상세 설명");
	    item.setItemSellStatus(ItemSellStatus.SELL);
	    item.setStockNumber(100);
	    return itemRepository.save(item);
	}

	public Member saveMember() {
	    Member member = new Member();
	    member.setEmail("test@test.com");
	    return memberRepository.save(member);
	}

}
