package com.project.film.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMveConfig implements WebMvcConfigurer {

	@Value("${file.imageDir}")
	private String imageLocation;
	
//	@Value("${file.imagePath}")
//	private String imagePath; // /images
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/test/**")
			.addResourceLocations("file://" + imageLocation);
		
		
	}
	
}
