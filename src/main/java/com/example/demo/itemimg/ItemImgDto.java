package com.example.demo.itemimg;

import org.modelmapper.ModelMapper;

import groovy.transform.ToString;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @lombok.ToString
public class ItemImgDto {
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력")
	private String imgName;
	
	@NotBlank(message = "가격은 필수 입력")
	private String oriImgName;
	
	@NotBlank(message = "이름은 필수 입력")
	private String itemDetail;
	
	
	private String imgUrl;
	
	private String repImgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
	}

}
