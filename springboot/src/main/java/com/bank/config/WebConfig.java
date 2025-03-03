package com.bank.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/auth/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true); 
                

                registry.addMapping("/atm/process/**") 
                        .allowedOrigins("http://localhost:4200") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE") 
                        .allowedHeaders("*") 
                        .allowCredentials(true); 
                
                registry.addMapping("/netbanking/process/**") 
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("*") 
                .allowCredentials(true); 
                
                registry.addMapping("/api/loan/**") 
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("*")
                .allowCredentials(true); 
                
                registry.addMapping("/api/loandue/**") 
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("*") 
                .allowCredentials(true);
                
                registry.addMapping("/api/cards/**")
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("*") 
                .allowCredentials(true); 
                
                
                registry.addMapping("/admin/card-block/**") 
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*") 
                .allowCredentials(true);                
            }
            
        };
       
    }
}
