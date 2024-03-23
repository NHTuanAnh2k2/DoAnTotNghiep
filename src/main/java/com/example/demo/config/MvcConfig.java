package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("image", registry);
    }
    public void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("D:\\DuAnTotNghiep\\DATN\\src\\main\\resources\\static\\img\\khachhang");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) {
            dirName = dirName.replace("../","");
        }
        registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:/"+uploadPath+"/");
    }
}
