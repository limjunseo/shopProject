package com.example.demo.itemimg;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.file.FileService;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemImgService {

	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	public void saveItemImg(ItemImg img, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName ="";
		String imgUrl ="";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName;
		}

		img.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(img);
	}
	
	public void deleteImg(Long id) {
		itemImgRepository.deleteById(id);
	}
	
	@Transactional
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
		
		if(!itemImgFile.isEmpty()) {
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
			
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
			}
			
			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
	
	public List<ItemImg> findByItemIdOrderByIdAsc(Long itemImgId) {
		List<ItemImg> itemImgLists  = itemImgRepository.findByItemIdOrderByIdAsc(itemImgId);
		return itemImgLists;
	}
	

}
