package com.example.demo.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value ="/thymeleaf")
public class ThymeleafExController {
	
	@GetMapping(value="/ex02")
	public String thymeleafExample02(ModelMap model) {
		ItemDto dto = new ItemDto();
		dto.setItemDetail("상품 상세 설명");
		dto.setItemName("상품 이름");
		dto.setPrice(1000);
		dto.setRegTime(LocalDateTime.now());
		
		model.put("item", dto);
		return "thymeleafEx02";
	}
	
	@GetMapping(value ="/ex03")
	public String thymeleafExample03(ModelMap model) {
		List<ItemDto> itemDtoList =new ArrayList<ItemDto>();
		
		for (int i=1; i<=10; i++) {
			ItemDto dto = new ItemDto();
			dto.setItemDetail("상품 상세 설명" + i);
			dto.setItemName("테스트 상품" + i);
			dto.setPrice(1000*i);
			dto.setRegTime(LocalDateTime.now());
			
			itemDtoList.add(dto);
		}
		
		model.put("itemDtoList", itemDtoList);
		return "thymeleafEx03";
	}
	
	@GetMapping(value="/ex04")
	public String thymeleafExample04(ModelMap model) {
		List<ItemDto> itemDtoList = new ArrayList<ItemDto>();
		
		for (int i=1; i<=10; i++) {
			ItemDto itemDto = new ItemDto();
			itemDto.setItemDetail("상품 상세 설명" + i);
			itemDto.setItemName("테스트 상품" + i);
			itemDto.setPrice(1000*i);
			itemDto.setRegTime(LocalDateTime.now());
			
			itemDtoList.add(itemDto);		
		}
		
		model.put("itemDtolist", itemDtoList);
		return "thymeleafEx04";
	}

}
