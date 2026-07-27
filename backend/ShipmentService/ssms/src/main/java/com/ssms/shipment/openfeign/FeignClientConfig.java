package com.ssms.shipment.openfeign;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate -> {
            // Grab JWT from context
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String token = request.getHeader("Authorization");
                // DEBUG: Print token
                System.out.println("[FeignClient] Forwarding token: " + token);

                if (token != null) {
                    requestTemplate.header("Authorization", token);
                }
            }
        };
    }
}
