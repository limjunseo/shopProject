package com.example.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.order.Order;
import com.example.demo.order.OrderRepository;
import com.example.demo.orderitem.OrderItem;
import com.example.demo.orderitem.OrderItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
public class OrderTest {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("Order와 OrderItem 영속성 전이 테스트")
	@Transactional
	public void OrderCascadeTest() {
		Order order = new Order();
		
		for (int i=0; i<3; i++) {
			Item item = createItem();
			OrderItem orderItem = new OrderItem();
			em.persist(item);
			
			orderItem.setCount(item.getPrice());
			orderItem.setItem(item);
			orderItem.setOrder(order);
			orderItem.setOrderPrice(1000);
			
			order.getOrderItems().add(orderItem);
			
		}
		
		em.persist(order); //오더만 저장한 상황
		em.flush();
		em.clear();
		
		Order savedOrder = orderRepository.findById(order.getId()).get();
		
		assertEquals(3, savedOrder.getOrderItems().size());
	}
	
	@Test
	@DisplayName("Order와 OrderItem 고아 객체 제거 테스트")
	@Transactional
	public void orphanRemovalTest() {
		Order order = createOrder();
		order.getOrderItems().remove(0);
		em.flush();
		
		
		
	}
	
	@Test
	@DisplayName("지연 로딩 테스트")
	@Transactional
	public void lazyLoadingTest() {
		Order order = createOrder();
		Long orderItemID = order.getOrderItems().get(0).getId();
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = orderItemRepository.findById(orderItemID).get();
		System.out.println(orderItem.getOrder().getClass());
		
	}
	
	@Transactional
	private Order createOrder() {
	    Order order = new Order();

	    for (int i = 0; i < 3; i++) {
	        Item item = createItem();
	        itemRepository.save(item);

	        OrderItem orderItem = new OrderItem();
	        orderItem.setItem(item);
	        orderItem.setCount(10);
	        orderItem.setOrderPrice(1000);
	        orderItem.setOrder(order);
	        order.getOrderItems().add(orderItem);
	    }

	    Member member = new Member();
	    memberRepository.save(member);

	    order.setMember(member);
	    orderRepository.save(order);

	    return order;
	}

	private Item createItem() {
	    Item item = new Item();
	    item.setItemName("테스트 상품");
	    item.setPrice(10000);
	    item.setItemDetail("상세설명");
	    item.setItemSellStatus(ItemSellStatus.SELL);
	    item.setStockNumber(100);
	    item.setRegTime(LocalDateTime.now());
	    return item;
	}

}
