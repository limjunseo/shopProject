package com.example.demo.Item;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.example.demo.itemsearch.ItemRepositoryCustom;


public interface ItemRepository extends JpaRepository<Item, Long> , QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
	//named query
	List<Item> findByItemName(String name);
	List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);
	List<Item> findByPriceLessThan(int price);
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	
	//static query
	@Query("select i from Item i "
			+ "where " + "i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
	
	//native query
	@Query(value = "select * from item i "
			+ "where i.itemdetail like %:itemDetail% "
			+ "order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	
	
	
	
	
}
