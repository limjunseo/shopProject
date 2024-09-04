package com.example.demo.itemsearch;

import java.time.LocalDateTime;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.demo.Item.Item;
import com.example.demo.Item.MainItemDto;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.entity.Item.QItem;
import com.example.demo.entity.Item.QMainItemDto;
import com.example.demo.itemimg.QItemImg;
import com.example.demo.itemsearch.ItemSearchDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
	

	private JPAQueryFactory queryFactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		List<Item> results = queryFactory
				.selectFrom(QItem.item)
				.where(regDtsAfter(itemSearchDto.getSearchDateType()),
						searchSeelStatusEq(itemSearchDto.getSearchSellStatus()),
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = results.size();
		return new PageImpl<Item>(results, pageable, total);
	}				
	
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		List<MainItemDto> results = queryFactory
				.select(
						new QMainItemDto(item.id, item.itemName, item.itemDetail, itemImg.imgUrl, item.price))
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNameLike(itemSearchDto.getSearchQuery()))
				.orderBy(item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = results.size();
		return new PageImpl<MainItemDto>(results, pageable, total);
	}

	private BooleanExpression itemNameLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
		
	}
	
	private BooleanExpression searchSeelStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();
		
		if(searchDateType == null || searchDateType.equals("all") ) {
			return null;
		} else if(searchDateType.equals("1d")) {
			dateTime = dateTime.minusDays(1);
		} else if(searchDateType.equals("1w")) {
			dateTime = dateTime.minusWeeks(1);
		} else if(searchDateType.equals("1m")) {
			dateTime = dateTime.minusMonths(1);
		} else if(searchDateType.equals("6m")) {
			dateTime = dateTime.minusMonths(6);
		}
		
		return QItem.item.regTime.after(dateTime);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if("itemName".equals(searchQuery)) {
			return QItem.item.itemName.like("%" + searchQuery + "%");
		} else if("createdBy".equals(searchQuery)) {
			return QItem.item.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}



	

}
