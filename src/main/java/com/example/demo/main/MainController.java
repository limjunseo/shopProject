package com.example.demo.main;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Item.ItemService;
import com.example.demo.Item.MainItemDto;
import com.example.demo.itemsearch.ItemSearchDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	private final ItemService itemService;
	
	@GetMapping("/")
	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, ModelMap model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainItemDto> items =itemService.getMainItemPage(itemSearchDto, pageable);
		model.put("items", items);
		model.put("itemSearchDto", itemSearchDto);
		model.put("maxPage", 5);
		return "main";
	}


}
