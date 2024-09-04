package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.itemimg.ItemImg;
import com.example.demo.itemimg.ItemImgRepository;

import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
public class ItemImgServiceTest {
	
	@Autowired
	ItemImgRepository itemImgRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	@Test
	@DisplayName("application-properties 속성 @value 주입 테스트")
	public void propertiesTest() {
		assertEquals(itemImgLocation, "C:/shop/item");
	}
	
	@Test
	@DisplayName("ItemImg 저장 테스트")
	@Transactional
	public void itemRepositoryTest() {
		byte[] bytes = {1,2,3,4};
		
		//저장할 파일 mock 생성
		MockMultipartFile file = new MockMultipartFile("junseo", bytes); 
		String oriName = file.getName();
		String savedImageName = "/1234" + oriName;
		String imgUrl = "/images/item" + savedImageName;

		ItemImg img = new ItemImg();
		img.setOriImgName(oriName);
		img.setImgUrl(imgUrl);
		img.setImgName(savedImageName);
		
		ItemImg savedItemImg = itemImgRepository.save(img);
		
		em.flush();
		em.clear();
		
		ItemImg findItemImg = itemImgRepository.findById(savedItemImg.getId()).orElseThrow(EntityNotFoundException::new);
		
		assertThat(savedItemImg.getImgName()).isEqualTo(findItemImg.getImgName());
		assertThat(savedItemImg.getImgUrl()).isEqualTo(findItemImg.getImgUrl());
		assertThat(savedItemImg.getOriImgName()).isEqualTo(findItemImg.getOriImgName());
	}
	
	
	@Test
	@DisplayName("ItemRepository delete 테스트")
	@Transactional
	public void itemRepositoryDeleteTest() {
		byte[] bytes = {1,2,3,4};
		
		//저장할 파일 mock 생성
		MockMultipartFile file = new MockMultipartFile("junseo", bytes); 
		String oriName = file.getName();
		String savedImageName = "/1234" + oriName;
		String imgUrl = "/images/item" + savedImageName;

		ItemImg img = new ItemImg();
		img.setOriImgName(oriName);
		img.setImgUrl(imgUrl);
		img.setImgName(savedImageName);
		
		ItemImg savedItemImg = itemImgRepository.save(img);
		
		em.flush();
		em.clear();
		
		itemImgRepository.delete(savedItemImg);
		em.flush();
		em.clear();
		
		Optional<ItemImg> findItemImg = itemImgRepository.findById(savedItemImg.getId());
		assertThat(findItemImg.isEmpty()).isEqualTo(true);
	}
	
	

}

//UUID uuid = UUID.randomUUID();
//String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//String savedFileName = uuid.toString() + extension;
//String fileUploadFullUrl = uploadPath + "/" + savedFileName;
//
//FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
//fos.write(fileData);
//fos.close();
//return savedFileName;




//private Long id;
//
//private String imgName;
//
//private String oriImgName;
//
//private String imgUrl;
//
//private String repimgYn;
//
//@ManyToOne(fetch = FetchType.LAZY)
//@JoinColumn(name ="item_id")
//private Item item;


//spring.servlet.multipart.maxFileSize=20MB
//spring.servlet.multipart.maxRequestSize=100MB
//itemImgLocation=C:/shop/item
//uploadPath=file:///C:/shop/

//public class ItemImgService {
//	
//	@Value("${itemImgLocation}")
//	private String itemImgLocation;
//	
//	private final ItemImgRepository itemImgRepository;
//	
//	private final FileService fileService;
//	
//	public void saveItemImg(ItemImg img, MultipartFile itemImgFile) throws Exception {
//		String oriImgName = itemImgFile.getOriginalFilename();
//		String imgName ="";
//		String imgUrl ="";
//		
//		if(!StringUtils.isEmpty(oriImgName)) {
//			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
//			imgUrl = "/images/item" + imgName;
//		}
//		
//		img.updateItemImg(oriImgName, imgName, imgUrl);
//		itemImgRepository.save(img);
//	}
//	
//	public void deleteImg(Long id) {
//		itemImgRepository.deleteById(id);
//	}
//	
//	@Transactional
//	public void updaetItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
//		if(!itemImgFile.isEmpty()) {
//			ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
//			
//			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
//				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
//			}
//			
//			String oriImgName = itemImgFile.getOriginalFilename();
//			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
//			String imgUrl = "/images/item/" + imgName;
//			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
//		}
//		
//		
//	}
//
//}
