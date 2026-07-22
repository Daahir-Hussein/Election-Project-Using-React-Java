package com.ems.electionmanagement.config;

import com.ems.electionmanagement.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageService fileStorageService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = fileStorageService.getUploadRoot().toUri().toString();
        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations(uploadLocation)
            .setCachePeriod(3600);
    }
}
