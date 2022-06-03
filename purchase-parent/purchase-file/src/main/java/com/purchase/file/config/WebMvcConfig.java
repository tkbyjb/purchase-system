package com.purchase.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Bean
//    public AuthInterceptor addAuthInteceptor() {
//        return new AuthInterceptor();
//    }
//
//    @Override   //拦截器配置
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(addAuthInteceptor())
//                .addPathPatterns("/**") //指定要拦截的请求
//                .excludePathPatterns("/login", "/register", "/verifyCode/get", "/upload/**", "/verifyCode/forgetPassword", "/forgetPassword"); //排除请求
//    }
//
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/file/avatar/**").addResourceLocations("file:D:/code/1_my_projects/1-school-purchase/file/avatar/");
        registry.addResourceHandler("/file/user/**").addResourceLocations("file:D:/code/1_my_projects/1-school-purchase/file/user/");
        registry.addResourceHandler("/file/contract/**").addResourceLocations("file:D:/code/1_my_projects/1-school-purchase/file/contract/");
        //这里的opt是我D盘下面的opt目录，在配置文件配置过，图片存储的位置
        //域名例如：127.0.0.1:8080/opt/1.jpg
//        registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/static/upload/");
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}
