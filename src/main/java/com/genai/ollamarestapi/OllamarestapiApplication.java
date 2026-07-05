package com.genai.ollamarestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import com.genai.ollamarestapi.model.jira.JiraApiProperties;


@EnableRetry
@SpringBootApplication
@ComponentScan(basePackages = "com.genai.ollamarestapi")
@EnableConfigurationProperties(JiraApiProperties.class)
public class OllamarestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OllamarestapiApplication.class, args);
	}

}
