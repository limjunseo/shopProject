package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.Item.ItemService;
import com.example.demo.Item.ItemUpdateController;


@SpringBootTest
@WebMvcTest(ItemUpdateController.class)
public class ItemUpdateControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ItemService itemService;
	
	


	
	
//	@Test
//	@DisplayName("Item 생성 컨트롤러 테스트")
//	public void ItemSaveTest() {
//		Item item1 = new Item("테스트 아이템", 100, 10, "테스트 아이템 설명");
//		mockMvc.perform(servletContext -> servletContext.)
//	}

}
