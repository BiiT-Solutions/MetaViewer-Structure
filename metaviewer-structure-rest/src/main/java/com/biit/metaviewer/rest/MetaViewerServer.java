package com.biit.metaviewer.rest;

import com.biit.metaviewer.cadt.CadtScoreController;
import com.biit.metaviewer.cadt.CadtValueController;
import com.biit.metaviewer.controllers.FormController;
import com.biit.metaviewer.logger.MetaViewerLogger;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

//Avoid Swagger redirecting https to http
@OpenAPIDefinition(servers = {@Server(url = "${server.servlet.context-path}", description = "Default Server URL")})
@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:forms.properties"),
        @PropertySource(value = "file:${EXTERNAL_CONFIG_FILE}", ignoreResourceNotFound = true)
})
@ComponentScan(basePackages = {"com.biit.metaviewer", "com.biit.server.security", "com.biit.server", "com.biit.messagebird.client",
        "com.biit.usermanager.client", "com.biit.factmanager.client", "com.biit.kafka"})
@ConfigurationPropertiesScan({"com.biit.metaviewer.rest", "com.biit.kafka.config"})
public class MetaViewerServer {
    private static final int POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 100;

    @Value("${forms.enabled}")
    private List<String> formsEnabled;

    private final FormController formController;
    private final CadtScoreController cadtScoreController;
    private final CadtValueController cadtValueController;

    public MetaViewerServer(FormController formController, CadtScoreController cadtScoreController, CadtValueController cadtValueController) {
        this.formController = formController;
        this.cadtScoreController = cadtScoreController;
        this.cadtValueController = cadtValueController;
    }

    public static void main(String[] args) {
        SpringApplication.run(MetaViewerServer.class, args);
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }


    @Bean("threadPoolExecutor")
    public TaskExecutor getAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Rest_Async-");
        return executor;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> startupLoggingListener() {
        return event -> MetaViewerLogger.info(MetaViewerServer.class, "### Server started ###");
    }
}
