package com.genai.ollamarestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.genai.ollamarestapi.model.jira.JiraApiProperties;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebClientConfig {

    /*
     * @Bean
     * public WebClient jiraWebClient(JiraApiProperties props) {
     * 
     * String auth = Base64.getEncoder()
     * .encodeToString((props.getUsername() + ":" +
     * props.getApiToken()).getBytes());
     * 
     * return WebClient.builder()
     * .baseUrl(props.getApiUrl())
     * .defaultHeader("Authorization", "Basic " + auth)
     * .defaultHeader("Accept", "application/json")
     * .build();
     * }
     */

    /*
     * @Bean
     * public WebClient jiraWebClient(JiraApiProperties props) {
     * 
     * String auth = Base64.getEncoder()
     * .encodeToString(
     * (props.getUsername() + ":" + props.getApiToken())
     * .getBytes(StandardCharsets.UTF_8));
     * 
     * System.out.println("Authorization Header:");
     * System.out.println("Basic " + auth);
     * 
     * return WebClient.builder()
     * .baseUrl(props.getApiUrl())
     * .defaultHeader("Authorization", "Basic " + auth)
     * .defaultHeader("Accept", "application/json")
     * .build();
     * }
     */

    /*
     * @Bean
     * public WebClient jiraWebClient(JiraApiProperties props) {
     * 
     * return WebClient.builder()
     * .baseUrl(props.getApiUrl())
     * .defaultHeaders(headers -> {
     * headers.setBasicAuth(
     * props.getUsername(),
     * props.getApiToken());
     * headers.set("Accept", "application/json");
     * })
     * .build();
     * }
     */

    /* @Bean
    public WebClient jiraWebClient(JiraApiProperties props) {

        return WebClient.builder()

                .baseUrl(props.getApiUrl())

                .filter((request, next) -> {

                    System.out.println("===== REQUEST =====");

                    request.headers().forEach((k, v) ->

                System.out.println(k + " = " + v));

                    return next.exchange(request);
                })

                .defaultHeaders(headers ->

                headers.setBasicAuth(
                        props.getUsername().trim(),
                        props.getApiToken().trim()))

                .build();
    } */

                @Bean
    public WebClient jiraWebClient(JiraApiProperties props) {

        return WebClient.builder()

                .baseUrl(props.getApiUrl())

                .defaultHeaders(headers -> {
                    headers.setBasicAuth(
                            props.getUsername().trim(),
                            props.getApiToken().trim());

                    headers.set("Accept", "application/json");
                })

                .filter((request, next) -> {

                    System.out.println("==================================");
                    System.out.println(request.method());
                    System.out.println(request.url());

                    request.headers().forEach((k, v) ->
                            System.out.println(k + " = " + v));

                    System.out.println("==================================");

                    return next.exchange(request);
                })

                .build();
    }
}