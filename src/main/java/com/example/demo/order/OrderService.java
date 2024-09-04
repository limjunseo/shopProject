package com.example.demo.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.itemimg.ItemImg;
import com.example.demo.itemimg.ItemImgRepository;
import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;
import com.example.demo.orderhis.OrderHisDto;
import com.example.demo.orderitem.OrderItem;
import com.example.demo.orderitem.OrderItemDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final ItemImgRepository itemImgRepository;

	@Transactional
	public Long order(OrderDto orderDto, String name) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		//.get() 수정바람
		Member member = memberRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
		
		
		log.info("Member, ItemRepository에는 문제가 없을거에요");
		//createOrderItem에 item.remove가 있음 -> 이게 맞나? 로직이 보이게 빼는게 좋을거 같은데?
		OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
		orderItemList.add(orderItem);
				
		Order order = Order.createOrder(member, orderItemList);
		orderItemList.stream()
					.forEach(oi -> oi.setOrder(order));
		Order savedOrder = orderRepository.save(order);
		return savedOrder.getId();
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Page<OrderHisDto> getOrderList(String email, Pageable pageable) {
		List<Order> orders = orderRepository.findOrders(email, pageable);
		Long totalCount = orderRepository.countOrder(email);
		
		List<OrderHisDto> orderHisDtos = new ArrayList<OrderHisDto>();
		for (Order order : orders) {

			OrderHisDto orderHisDto = new OrderHisDto(order);
			List<OrderItem> orderItems = order.getOrderItems(); //lazy loading
			log.info("lazy loading중인 orderItems : {}",orderItems);
			for (OrderItem orderItem : orderItems) {

				ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
				OrderItemDto orderItemDto = new OrderItemDto(orderItem, img.getImgUrl());
				orderHisDto.addOrderItemDto(orderItemDto);
			}
			orderHisDtos.add(orderHisDto);
		}
		return new PageImpl<OrderHisDto>(orderHisDtos, pageable, totalCount);
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public boolean validateOrder(Long orderId, String email) {
		Member curMember = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = order.getMember();
		
		if(!curMember.getEmail().equals(savedMember.getEmail())) {
			return false;
		}
		return true;
	}
	
	@org.springframework.transaction.annotation.Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		order.cancelOrder();
	}
	
	
	
	public Long orders(List<OrderDto> orderDtoList, String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		
		for(OrderDto orderDto : orderDtoList) {
			Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
			OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
			orderItemList.add(orderItem);
		}
		
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);
		
		return order.getId();
		
	}
	
	
	
	
	
	
}
