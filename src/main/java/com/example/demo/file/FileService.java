package com.example.demo.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
@lombok.extern.slf4j.Slf4j
public class FileService {
	
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension;
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		

		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		log.info("파일 업로드 완료 fullUrl {}", fileUploadFullUrl);
		return savedFileName;
	}
	
	public void deleteFile(String filePath) throws Exception {
		File deleteFile = new File(filePath);

		if(deleteFile.exists()) {
			deleteFile.delete();
		} else {
			
		}
	}

}
