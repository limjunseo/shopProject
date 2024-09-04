package com.example.demo.itemsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.Item.Item;
import com.example.demo.Item.MainItemDto;
import com.example.demo.itemsearch.ItemSearchDto;

public interface ItemRepositoryCustom {
	org.springframework.data.domain.Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
	Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
