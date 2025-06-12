package com.biit.metaviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.biit.metaviewer"})
@ComponentScan({"com.biit.metaviewer", "com.biit.factmanager.client", "com.biit.server.client"})
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:forms.properties"),
        @PropertySource(value = "file:${EXTERNAL_CONFIG_FILE}", ignoreResourceNotFound = true)
})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
