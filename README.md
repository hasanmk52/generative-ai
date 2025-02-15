# Generative AI

## Introduction

Welcome to the **Generative AI** project! This is a Spring Boot-based application designed to interact with various AI-powered models for generating text, images, and handling vector-based semantic searches. The application leverages OpenAI, Ollama, and other AI models to provide advanced conversational and analytical capabilities.

This project is structured around multiple services, including:
- AI-powered **Chat** services
- A **Weather** information retriever
- **Vector search** using embeddings
- **Semantic similarity matching**

## Project Requirements

### Minimum Requirements
- **Java 21** or later
- **Maven** (for dependency management and building the project)
- **Spring Boot 3**
- **Docker** (optional, for deployment)
- **API keys** for OpenAI and Weather API (configured in `application.properties`)

### Dependencies
This project relies on several key libraries:
- **Spring Boot Starter Web** - RESTful API support
- **Spring AI** - AI integration with OpenAI and Ollama
- **Spring Boot Configuration Processor** - Property management
- **Lombok** - Boilerplate code reduction
- **RestClient** - Handling external API calls
- **Vector Search** - Efficient similarity searches

## Getting Started

### Configuration
Before running the application, update the `application.properties` file with your API keys:
```properties
spring.ai.openai.api-key=your_openai_key
spring.weather.api.key=your_weatherapi_key
```

### How to Run the Application
#### Running Locally
1. Ensure Java 21+ and Maven are installed.
2. Navigate to the project root and run:
   ```sh
   mvn spring-boot:run
   ```
3. The application will be available at `http://localhost:8080/generative-ai`.

#### Running with Docker
1. Build the Docker image:
   ```sh
   docker build -t generative-ai .
   ```
2. Run the container:
   ```sh
   docker run -p 8080:8080 generative-ai
   ```

## API Endpoints

### 1. Chat API
- **Generate Chat Response**
  ```http
  GET /chat/prompt?category=fiction&year=2024
  ```
  **Response:** JSON containing book recommendations

- **Generate Image Response**
  ```http
  GET /chat/generate-image?query=landscape
  ```

### 2. Advisor API
- **Travel Assistant Advice**
  ```http
  GET /advisor/response?query=Best flight from NYC to London?
  ```

### 3. Weather API
- **Fetch Weather Information**
  ```http
  GET /weather/query?query=What's the temperature in Paris?
  ```

### 4. Semantic Search API
- **Match Semantic Data**
  ```http
  POST /semantic/match
  ```
  **Payload:**
  ```json
  {
    "sourceList": ["What's your name?", "Where do you live?"],
    "destinationList": ["Your name please?", "Your city?"]
  }
  ```
  **Response:** Matches with similarity scores.

## Code Example
Here’s an example of how `ChatService` generates AI-powered responses:
```java
public ChatResponse gentChatResponse(String category, String year) {
    PromptTemplate promptTemplate = new PromptTemplate(
        """
        Please provide me best book for the given {category} and the {year}.
        Provide a summary of the book in JSON format: {category, book, year, author, summary}.
        """
    );
    promptTemplate.add("category", category);
    promptTemplate.add("year", year);
    Prompt prompt = promptTemplate.create();
    return chatModel.call(prompt);
}
```

## Conclusion
This project provides a robust foundation for AI-driven applications. Whether you need chatbots, semantic searches, or AI-powered recommendations, this project has the essential building blocks.