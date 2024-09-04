package com.example.demo.Item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.itemimg.ItemImg;
import com.example.demo.itemimg.ItemImgDto;
import com.example.demo.itemimg.ItemImgService;
import com.example.demo.itemsearch.ItemSearchDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	
	@org.springframework.transaction.annotation.Transactional
	public Long saveItem(ItemFormDto itemFormDto, java.util.List<MultipartFile> itemImgFileList) throws Exception {
		Item item = itemFormDto.createItem();
		itemRepository.save(item);
		
		for (int i=0; i<itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			if(i==0) {
				itemImg.setRepimgYn("Y");
			} else {
				itemImg.setRepimgYn("N");
			}
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	
	@org.springframework.transaction.annotation.Transactional
	public Long updateItem(ItemFormDto itemFormDto , List<MultipartFile> itemImgFileList) throws Exception {
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		item.updateItem(itemFormDto);
		
		List<Long> itemImgIds = itemFormDto.getItemImgIds();
		for (int i=0; i<itemImgFileList.size(); i++) {

			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		return item.getId();
		
	}
	
	public ItemFormDto getItemDtl(Long itemId) {
		List<ItemImg> itemImgs = itemImgService.findByItemIdOrderByIdAsc(itemId);
		List<ItemImgDto> itemImgDtos = itemImgs.stream()
												.map(ItemImgDto::of)
												.toList();
												
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtos);
		return itemFormDto;
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
	}
	
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	

}
