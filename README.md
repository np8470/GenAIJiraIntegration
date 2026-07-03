# ⚡ TestPilot AI
### AI-Powered Jira Test Case Generator

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-blue)
![Ollama](https://img.shields.io/badge/Ollama-Mistral-purple)
![Jira](https://img.shields.io/badge/Jira-Cloud-blue)
![License](https://img.shields.io/badge/License-MIT-green)

---

## 🚀 Overview

**TestPilot AI** is an AI-powered enterprise application that automatically generates high-quality Jira Test Cases from User Stories using Large Language Models (LLMs).

The application retrieves a Jira Work Item, sends the requirements to a locally running Ollama model (Mistral), generates structured test cases, allows users to review and edit them, and uploads selected test cases back to Jira.

Designed for QA Engineers, SDETs, Test Architects, and Agile Teams.

---

# ✨ Features

## 🤖 AI Test Case Generation

- Generate UI Test Cases
- Generate API Test Cases
- Generate Selenium Test Scripts
- JSON structured AI responses
- Automatic parsing
- AI prompt engineering
- Supports Ollama + Mistral

---

## 🔍 Jira Integration

- Fetch Jira User Story
- Read Summary
- Read Description
- Read Acceptance Criteria
- Upload generated Test Cases
- Link Test Cases with User Story
- Bulk Upload Support

---

## 📋 Test Case Management

- Search Test Cases
- Priority Sorting
- Expand All
- Collapse All
- Select All
- Individual Selection
- Upload Selected Test Cases
- Review before Upload

---

## 🎨 Modern Enterprise UI

- Responsive Bootstrap UI
- Dark Mode
- Light Mode
- Enterprise Branding
- Workflow Visualization
- Progress Indicators
- Upload Summary
- Toast Notifications

---

# 🏗 Architecture


                 +------------------+
                 |     Browser      |
                 +---------+--------+
                           |
                           |
                    Spring Boot REST API
                           |
        +------------------+----------------+
        |                                   |
        |                                   |
    Jira Service                     AI Service
        |                                   |
        |                             Ollama Client
        |                                   |
        |                              Mistral LLM
        |
   Jira Cloud REST API
```

---

# 📁 Project Structure

```
src
├── main
│
├── java
│   └── com.genai.ollamarestapi
│
│       ├── ai
│       │      JsonSchemaFactory.java
│       │      ResponseParser.java
│       │
│       ├── client
│       │      OllamaClient.java
│       │
│       ├── config
│       │      JiraConfig.java
│       │      ObjectMapperConfig.java
│       │
│       ├── controller
│       │      GenerationController.java
│       │
│       ├── exception
│       │      AIException.java
│       │      JiraException.java
│       │
│       ├── model
│       │
│       │      ai
│       │          TestCase.java
│       │          OllamaRequest.java
│       │          OllamaResponse.java
│       │
│       │      jira
│       │          JiraIssueResponse.java
│       │          JiraBulkResponse.java
│       │          JiraLinkRequest.java
│       │
│       ├── service
│       │      AIService.java
│       │      JiraService.java
│       │      TestCaseOrchestratorService.java
│       │
│       └── TestPilotApplication.java
│
├── resources
│
│   ├── static
│   │
│   ├── css
│   │      style.css
│   │
│   ├── js
│   │      app.js
│   │      generator.js
│   │      upload.js
│   │      ui.js
│   │      theme.js
│   │
│   └── templates
│          index.html
```

---

# ⚙ Technology Stack

| Technology | Version |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.x |
| Bootstrap | 5.3 |
| JavaScript | ES6 |
| Thymeleaf | Latest |
| Jackson | Latest |
| Ollama | Latest |
| Mistral | Latest |
| Jira Cloud REST API | v3 |

---

# 📷 Application Workflow

```
🔍 Fetch Work Item

        ↓

🤖 AI Generation

        ↓

✏️ Review & Edit

        ↓

☁️ Upload to Jira
```

---

# 🔄 End-to-End Flow

1. Enter Jira Work Item ID
2. Select Generation Type
3. Click Generate
4. AI generates Test Cases
5. Review generated Test Cases
6. Search / Sort / Edit
7. Select Test Cases
8. Upload to Jira
9. View Upload Summary

---

# 🤖 Supported AI Models

Currently Tested

- Mistral
Configured through Ollama.

---

# 🔧 Configuration

## application.properties

```properties
server.port=8080

ollama.base.url=http://localhost:11434
ollama.model=mistral:latest

jira.base.url=https://your-domain.atlassian.net
jira.email=your-email
jira.api.token=your-api-token
```

---

# ▶ Running the Project

## Clone

```bash
git clone https://github.com/yourusername/GenAIJiraIntegration.git
```

---

## Start Ollama

```bash
ollama run mistral
```

---

## Run Spring Boot

```bash
./gradlew clean build bootRun
```

---

Open

```
http://localhost:8080
```

---

# 🎯 Future Roadmap

- Spring Security
- OAuth Login
- Role Based Access
- Audit Logging
- Dashboard Analytics
- AI Prompt Library
- Test Case Versioning
- Export to Excel
- Export to PDF
- Azure DevOps Integration
- GitHub Integration
- Jenkins Integration
- Docker Support
- Kubernetes Deployment

---

# 📊 Enterprise Features

✔ AI Usage Dashboard

✔ Upload History

✔ Generation History

✔ Recent Stories

✔ Charts

✔ Export Reports

✔ Team Management

✔ Dark Mode

✔ Responsive UI

---

# 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch

```
git checkout -b feature/my-feature
```

3. Commit changes

```
git commit -m "Added new feature"
```

4. Push

```
git push origin feature/my-feature
```

5. Open a Pull Request

---

# 📜 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Niraj Patel**

Senior SDET | Automation Engineer | AI Enthusiast

---

# ⭐ Support

If you like this project, consider giving it a ⭐ on GitHub!

It helps others discover the project and motivates future improvements.

---

## © 2026 TestPilot AI | Enterprise Edition v1.0
