package com.genai.ollamarestapi.service;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.model.UploadRequest;
import com.genai.ollamarestapi.model.UploadResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    private final JiraUploadService jiraUploadService;

    public UploadResponse upload(

            UploadRequest request) {

        return jiraUploadService.upload(

                request);

    }

}
