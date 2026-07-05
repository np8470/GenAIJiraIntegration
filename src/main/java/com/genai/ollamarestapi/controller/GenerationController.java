    package com.genai.ollamarestapi.controller;

    import com.genai.ollamarestapi.audit.Audit;
    import com.genai.ollamarestapi.audit.AuditAction;
    import com.genai.ollamarestapi.model.GenerateResponse;
    import com.genai.ollamarestapi.model.GenerationType;
    import com.genai.ollamarestapi.model.UploadResponse;
    import com.genai.ollamarestapi.service.TestCaseOrchestratorService;

    import jakarta.servlet.http.HttpSession;
    import lombok.RequiredArgsConstructor;

    import java.util.List;

    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/v1")
    @RequiredArgsConstructor
    public class GenerationController {

        private final TestCaseOrchestratorService service;

        @GetMapping("/generate")
        @Audit(action = AuditAction.GENERATE_TEST_CASE, message = "Generated AI Test Cases for Work Item {0} using type {1}")
        public GenerateResponse generate(
                @RequestParam String storyKey,
                @RequestParam String type,
                HttpSession session) {
                    System.out.println("Story Key = " + storyKey);
                    System.out.println("Type = " + type);
            return service.generateOnly(
                    storyKey,
                    GenerationType.valueOf(type),
                    session);
        }

        @PostMapping("/upload")
        @Audit(action = AuditAction.UPLOAD_TO_JIRA, message = "Uploaded {0} Test Cases to Jira")
        public UploadResponse uploadToJira(@RequestBody List<Integer> selectedIndexes, HttpSession session) {

            return service.uploadSelectedToJira(
                selectedIndexes,
                session);
        }

    }