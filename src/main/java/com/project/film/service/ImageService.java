package com.project.film.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.film.domain.Image;
import com.project.film.domain.Review;
import com.project.film.domain.Users;
import com.project.film.repository.ImageRepository;
import com.project.film.repository.ReviewRepository;
import com.project.film.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
	
	@Value("${file.imageDir}")
	private String fileImageDir;
	
	private final ImageRepository imageRepository;
	private final ReviewRepository reviewRepository;
	private final UsersRepository usersRepository;

	public Long saveFile(Integer reviewId, MultipartFile files) throws IOException {
		if (files.isEmpty()) {
			return null;
		}
		
		Review review = reviewRepository.findById(reviewId).get();
		
		// Original File Name
		String originName = files.getOriginalFilename();
		
		// 사용할 File Name
		String uuid = UUID.randomUUID().toString();
		
		// 확장자 추출
		String extension = originName.substring(originName.lastIndexOf("."));
		
		// uuid + extension
		String savedName = uuid + extension;
		
		// 파일 불러올 때 사용할 경로
		String savedPath = fileImageDir + savedName;
		
		Image image = Image.builder()
				.originName(originName)
				.fileName(savedName)
				.filePath(savedPath)
				.review(review)
				.build();
		
		// 로컬에 저장
		files.transferTo(new File(savedPath));
		
		Image savedFile = imageRepository.save(image);
		
		return savedFile.getId();
	}
	
	// 대표 사진 하나씩만 저장
	public List<Image> readImg(String idName) {
		List<Review> reviewList = reviewRepository.findByAuthorOrderByIdDesc(idName);
		List<Image> imageList = new ArrayList<>();
		
		if (reviewList != null) {
			for (Review r : reviewList) {
				List<Image> image = imageRepository.findByReviewId(r.getId());
				log.info("reviewId={}", r.getId());
				log.info("image size?? = {}", image.size());
				if (image != null) {
					imageList.add(image.get(0));
				}
			}			
		} else {
			imageList = null;
		}
		
		return imageList;
	}
	
}
