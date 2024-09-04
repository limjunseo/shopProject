package com.example.demo.itemimg;

import com.example.demo.BaseEntity.BaseEntity;
import com.example.demo.Item.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_image")
@Setter @Getter
public class ItemImg extends BaseEntity {
	
	@Id
	@Column(name ="item_img_id")
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repimgYn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="item_id")
	private Item item;
	
	public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
	

}
