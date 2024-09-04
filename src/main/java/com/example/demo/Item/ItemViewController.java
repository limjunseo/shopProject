package com.example.demo.Item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.itemsearch.ItemSearchDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ItemViewController {
	private final ItemService itemService;
	
	@GetMapping(value = {"/admin/items", "/admin/items/{page}"})
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, ModelMap model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		model.put("items", items);
		model.put("itemSearchDto", itemSearchDto);
		model.put("maxPage",5);
		return "item/itemMng";
	}
	
	@GetMapping(value="/item/{itemId}")
	public String itemDtl(@PathVariable("itemId") Long itemId, ModelMap model) {
		ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
		model.put("item", itemFormDto);
		log.info("아이템 페이지: {}", itemFormDto);
		return "item/itemDtl";
	}
	
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemView(@PathVariable("itemId") Long itemId, ModelMap model) {
		try {
			ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
			model.put("itemFormDto", itemFormDto);
			
		} catch (EntityNotFoundException e) {
			model.put("errorMessage", "존재하지 않는 상품입니다.");
			return "item/itemForm";
		}
		return "item/itemForm";
	}
	
	@GetMapping(value = "/admin/item/new")
	public String itemFrom(ModelMap model) {
		model.put("itemFormDto", new ItemFormDto());
		return "/item/itemForm";
	}
	

}
