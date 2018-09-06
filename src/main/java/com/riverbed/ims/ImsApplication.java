package com.riverbed.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
public class ImsApplication {
	@Bean
    WebMvcConfigurer configurer () {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers (ResourceHandlerRegistry registry) {
				registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            }
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(ImsApplication.class, args);
	}
}
