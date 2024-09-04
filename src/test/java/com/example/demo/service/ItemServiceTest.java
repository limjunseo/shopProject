package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Item.Item;
import com.example.demo.Item.ItemRepository;
import com.example.demo.Item.ItemService;
import com.example.demo.constant.ItemSellStatus;
import com.example.demo.itemimg.ItemImg;
import com.example.demo.itemimg.ItemImgRepository;
import com.example.demo.itemimg.ItemImgService;

import groovy.transform.ASTTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceTest {
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	ItemImgService itemImgService;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("상품 등록, 이미지 등록 테스트")
	@WithMockUser(username = "admin", roles = "ADMIN")
	@Transactional
	void saveItem() throws Exception {
	    com.example.demo.Item.ItemFormDto itemFormDto = new com.example.demo.Item.ItemFormDto();
	    itemFormDto.setItemName("상품명");
	    itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
	    itemFormDto.setItemDetail("테스트 상품 입니다.");
	    itemFormDto.setPrice(1000);
	    itemFormDto.setStockNumber(100);
	    
	    java.util.List<MultipartFile> multipartFiles = createMultipartFiles();
	    Long itemId = itemService.saveItem(itemFormDto, multipartFiles);
	    
	    em.flush();
	    em.clear();

	    
	    java.util.List<ItemImg> itemImgList = itemImgService.findByItemIdOrderByIdAsc(itemId);
	    Item item = itemRepository.findById(itemId).get();
	    
	    assertEquals(itemFormDto.getItemName(), item.getItemName());
	    assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
	    assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
	    assertEquals(itemFormDto.getPrice(), item.getPrice());
	    assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
	    assertEquals(multipartFiles.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
	    assertEquals(itemImgList.size(), 5);
	    

	}
	

	
	java.util.List<MultipartFile> createMultipartFiles() throws Exception {
		java.util.List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();
		
		for (int i=0; i<5; i++) {
			String imageName = "image" + i + ".jpg";
			MockMultipartFile multipartFile = new MockMultipartFile(imageName, new byte[] {1,2,3,4});
			multipartFileList.add(multipartFile);
		}
		return multipartFileList;
	}

}
