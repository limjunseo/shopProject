package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.entity.Item.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {
	
	@Autowired
	ItemRepository itemRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Test
	@DisplayName("상품 저장 테스트")
	public void createItemTest() {
		Item item = new Item();
		item.setItemName("테스트 상품");
		item.setPrice(10000);
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		item.setItemDetail("테스트 상품 상세 설명");
		Item savedItem = itemRepository.save(item);
		assertThat(savedItem.getItemName()).isEqualTo("테스트 상품");
		
	}
	
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNameTest() {
		Item item = new Item();
		String itemName = "테스트 상품";
		item.setItemName(itemName);
		item.setPrice(10000);
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		item.setItemDetail("테스트 상품 상세 설명");
		Item savedItem = itemRepository.save(item);	
		List<Item> findItemList = itemRepository.findByItemName(itemName);
		assertThat(findItemList.size()).isEqualTo(1);
		assertThat(findItemList.get(0).getItemName()).isEqualTo(savedItem.getItemName());
	}
	
	@Test
	@DisplayName("상품명, 상품상세설명 or 테스트")
	public void findByItemNameOrItemDetailTest() {
		this.createItemList();
		List<Item> list1 = itemRepository.findByItemNameOrItemDetail("테스트 상품1", null);
		
		assertThat(list1.size()).isEqualTo(1);
		assertThat(list1.get(0).getItemName()).isEqualTo("테스트 상품1");
		
		List<Item> list2 = itemRepository.findByItemNameOrItemDetail(null, "테스트 상품 상세 설명");
		assertThat(list2.size()).isEqualTo(2);
		assertThat(list2.get(0).getItemDetail()).isEqualTo("테스트 상품 상세 설명");
	}
	
	@Test
	@DisplayName("가격 LessThan 테스트")
	public void findBypriceLessThanTest() {
		this.createItemList();
		List<Item> list = itemRepository.findByPriceLessThan(10000);
		
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).getItemName()).isEqualTo("테스트 상품1");
		
	}
	
	@Test
	@DisplayName("가격 LessThan OrderBy 내림차순 테스트")
	public void findByPriceLessThanOrderByDesc() {
		Item item1 = new Item("테스트 상품1", 10000, 20, "테스트 상품 설명1");
		Item item2 = new Item("테스트 상품2", 9000, 20, "테스트 상품 설명2");
		Item item3 = new Item("테스트 상품3", 8000, 20, "테스트 상품 설명3");
		Item item4 = new Item("테스트 상품4", 8500, 20, "테스트 상품 설명4");
		Item item5 = new Item("테스트 상품5", 8900, 20, "테스트 상품 설명5");
		
		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item3);
		itemRepository.save(item4);
		itemRepository.save(item5);
		
		List<Item> list = itemRepository.findByPriceLessThanOrderByPriceDesc(10001);
		
		assertThat(list.size()).isEqualTo(5);
		assertThat(list.get(0).getItemName()).isEqualTo("테스트 상품1");
		assertThat(list.get(1).getItemName()).isEqualTo("테스트 상품2");

	}
	
	@Test
	@DisplayName("findByItemDetail 정적 쿼리 메소드 테스트")
	public void findByItemDetailOrderByPriceDescTest() {
		this.createItemList2();
		Item item5 = new Item("테스트 상품5", 8600, 20, "테스트 상품 설명4");
		Item item6 = new Item("테스트 상품6", 8700, 20, "테스트 상품 설명4");
		itemRepository.save(item5);
		itemRepository.save(item6);
		List<Item> list = itemRepository.findByItemDetail("테스트 상품 설명4");
		assertThat(list.size()).isEqualTo(3);
		assertThat(list.get(0).getItemName()).isEqualTo("테스트 상품6");
		
	}
	
	@Test
	@DisplayName("findByItemDetail 정적 네이티브 쿼리 메소드 테스트")
	public void findByItemDetailOrderByPriceDescByNativeQueryTest() {
		this.createItemList2();
		Item item5 = new Item("테스트 상품5", 8600, 20, "테스트 상품 설명4");
		Item item6 = new Item("테스트 상품6", 8700, 20, "테스트 상품 설명4");
		itemRepository.save(item5);
		itemRepository.save(item6);
		List<Item> list = itemRepository.findByItemDetailByNative("테스트 상품 설명4");
		assertThat(list.size()).isEqualTo(3);
		assertThat(list.get(0).getItemName()).isEqualTo("테스트 상품6");
		
	}
	
	@Test
	@DisplayName("Querydsl 조회 테스트1")
	public void queryDslTest() {
		this.createItemList2();
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QItem qItem = QItem.item;
		JPAQuery<Item> query = queryFactory.selectFrom(qItem)
								.where(qItem.itemDetail.like("%" + "테스트 상품 설명" + "%"))
								.orderBy(qItem.price.desc());
		List<Item> itemList = query.fetch();
		
		assertThat(itemList.size()).isEqualTo(5);
		assertThat(itemList.get(0).getItemDetail()).isEqualTo("테스트 상품 설명1");
	
	}
	
	@Test
	@DisplayName("상품 Querydsl 조회 테스트 2")
	public void queryDslTest2() {
		this.createItemList2();
		
		com.querydsl.core.BooleanBuilder booleanBuilder = new com.querydsl.core.BooleanBuilder();
		QItem item = QItem.item;
		String itemDetail = "테스트 상품 설명";
		int price = 5000;
		String itemSellStat = "SELL";
		
		booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
		booleanBuilder.and(item.price.gt(price));
		if(itemSellStat.equals(ItemSellStatus.SELL)) {
			booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		

		org.springframework.data.domain.Pageable pageable = PageRequest.of(0, 5);
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
		List<Item> resultItemList = itemPagingResult.getContent();

		
	}
	
	@Test
	@DisplayName("상품 findById 테스트")
	public void findById() {
		Item item1 = new Item("테스트 상품1", 10000, 20, "테스트 상품 설명1");
		
		Item savedItem = itemRepository.save(item1);
		assertEquals(savedItem.getItemDetail(), "테스트 상품 설명1");
	}
	
	private void createItemList2() {
		Item item1 = new Item("테스트 상품1", 10000, 20, "테스트 상품 설명1");
		Item item2 = new Item("테스트 상품2", 9000, 20, "테스트 상품 설명2");
		Item item3 = new Item("테스트 상품3", 8000, 20, "테스트 상품 설명3");
		Item item4 = new Item("테스트 상품4", 8500, 20, "테스트 상품 설명4");
		Item item5 = new Item("테스트 상품5", 8900, 20, "테스트 상품 설명5");


		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item3);
		itemRepository.save(item4);
		itemRepository.save(item5);
	}
	
	private void createItemList() {
		Item item1 = new Item();
		item1.setItemName("테스트 상품1");
		item1.setPrice(5000);
		item1.setItemSellStatus(ItemSellStatus.SELL);
		item1.setStockNumber(100);
		item1.setRegTime(LocalDateTime.now());
		item1.setUpdateTime(LocalDateTime.now());
		item1.setItemDetail("테스트 상품 상세 설명");
		
		Item item2 = new Item();
		item2.setItemName("테스트 상품2");
		item2.setPrice(10000);
		item2.setItemSellStatus(ItemSellStatus.SELL);
		item2.setStockNumber(100);
		item2.setRegTime(LocalDateTime.now());
		item2.setUpdateTime(LocalDateTime.now());
		item2.setItemDetail("테스트 상품 상세 설명");
		
		itemRepository.save(item1);
		itemRepository.save(item2);
	}
	

}


















