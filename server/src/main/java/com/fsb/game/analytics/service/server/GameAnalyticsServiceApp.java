
package com.fsb.game.analytics.service.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.fsb.game.analytics.service.server.interceptor.HeadersInterceptor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Game Analytics Service", version = "v1.0", description = "FSB Game Anlytics REST APIs", contact = @Contact(name = "FSB", email = "contacts@fsb.com", url = "https://fsb.com/")), servers = {
        @Server(url = "http://localhost:${server.port}/", description = "Local Server")

})
public class GameAnalyticsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(GameAnalyticsServiceApp.class, args);
    }

    @Bean
    public MappedInterceptor myMappedInterceptor() {
        return new MappedInterceptor(new String[] { "/**" }, new HeadersInterceptor());
    }

}
