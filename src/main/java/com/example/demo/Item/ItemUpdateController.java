package com.example.demo.Item;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.ItemSellStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemUpdateController {
	private final ItemService itemService;



	
	@PostMapping(value ="/admin/item/new") 
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, ModelMap model,
			@RequestParam("itemImgFile") java.util.List<MultipartFile> itemImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "item/itemFrom";
		}
		
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.put("errorMessage",	"첫번째 상품 이미지는 필수 입력값");
			return "item/itemForm";
		}
		
		if(itemFormDto.getItemSellStatus() == ItemSellStatus.SOLD_OUT && itemFormDto.getStockNumber() !=0) {
			model.put("errorMessage", "품절 상품은 재고가 0이여야합니다");
			return "item/itemForm";
		}
		
		try {
			itemService.saveItem(itemFormDto , itemImgFileList);
			
			
		} catch (Exception e) {
			model.put("errorMEssage", "상품 등록 중 에러가 발생함");
			return "item/itemForm";
		}
		
		return "redirect:/";
	}
	

	
	@PostMapping(value ="/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, 
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, ModelMap model) {
		
		if(bindingResult.hasErrors()) {
			return "item/itemForm";
		}
		
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.put("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다");
			return "item/itemForm";
		}
		
		if(itemFormDto.getItemSellStatus() == ItemSellStatus.SOLD_OUT && itemFormDto.getStockNumber() !=0) {
			model.put("errorMessage", "품절 상품은 재고가 0이여야합니다");
			return "item/itemForm";
		}
		
		try {
			itemService.updateItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			model.put("errorMessage", "상품 수정 중 에러 발생");
			return "item/itemForm";
		}
		
		return "redirect:/";
			
	}
}
