# DatoCMS Java Client

A lightweight Java client library for interacting with the [DatoCMS](https://www.datocms.com/) GraphQL API.

## About DatoCMS

DatoCMS is a headless Content Management System designed to help businesses manage and distribute content across multiple digital platforms. It provides a powerful GraphQL API for querying content, multi-language support, and real-time updates.

## Features

- Simple and intuitive API for executing GraphQL queries
- Support for multiple DatoCMS environments
- Type-safe response handling with generics
- Minimal dependencies (uses Java 17+ HttpClient and Gson)
- Easy configuration with API key authentication

## Requirements

- Java 17 or higher
- Gradle (for building)

## Installation

### Using Gradle

Add the following to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.datocms:java-datocms:0.0.1")
}
```

### Building from Source

```bash
git clone <repository-url>
cd java-datocms
./gradlew build
```

## Usage

### Basic Example

```java
import com.datocms.DatoCMSClient;
import com.datocms.dto.GetDataRequestDTO;
import java.util.Map;

// Initialize the client with your API key
DatoCMSClient client = new DatoCMSClient("your-api-key");

// Or specify a custom environment
DatoCMSClient client = new DatoCMSClient("your-api-key", "production");

// Create a GraphQL request
GetDataRequestDTO request = new GetDataRequestDTO(
    "MyQuery",
    """
    query MyQuery($id: String!) {
        article(filter: {id: {eq: $id}}) {
            title
            content
        }
    }
    """,
    Map.of("id", "123456789")
);

// Execute the query with a custom response type
MyArticle result = client.getData(request, MyArticle.class);
```

### Working with Custom Response Types

Define your response classes to match your GraphQL query structure:

```java
public class MyArticle {
    private Article article;

    public Article getArticle() {
        return article;
    }

    public static class Article {
        private String title;
        private String content;

        // Getters and setters
        public String getTitle() { return title; }
        public String getContent() { return content; }
    }
}
```

### Handling Queries Without Variables

```java
GetDataRequestDTO request = new GetDataRequestDTO(
    "AllArticles",
    """
    query AllArticles {
        allArticles {
            title
            publishedAt
        }
    }
    """,
    null  // No variables needed
);

AllArticlesResponse result = client.getData(request, AllArticlesResponse.class);
```

## API Reference

### DatoCMSClient

#### Constructors

- `DatoCMSClient(String apikey)` - Creates a client with the default "main" environment
- `DatoCMSClient(String apikey, String environment)` - Creates a client with a custom environment

#### Methods

- `<A> A getData(GetDataRequestDTO request, Class<A> responseType)` - Executes a GraphQL query and returns the parsed response
  - **Parameters:**
    - `request`: The GraphQL request containing operation name, query, and variables
    - `responseType`: The Java class type for deserializing the response
  - **Returns:** Deserialized response of type `A`
  - **Throws:** `IOException`, `InterruptedException`

### GetDataRequestDTO

#### Constructors

- `GetDataRequestDTO(String operationName, String query, Map<String, String> variables)`

#### Properties

- `operationName` - The name of the GraphQL operation
- `query` - The GraphQL query string
- `variables` - Map of query variables (can be null)

## Configuration

### API Key

Obtain your API key from the DatoCMS dashboard:
1. Log in to your DatoCMS account
2. Go to Settings → API Tokens
3. Create a new read-only or read-write token

### Environments

DatoCMS supports multiple environments (e.g., "main", "production", "staging"). Specify the environment when creating the client:

```java
DatoCMSClient client = new DatoCMSClient("your-api-key", "production");
```

If not specified, the client defaults to the "main" environment.

## Testing

Run the test suite:

```bash
./gradlew test
```

**Note:** Update the test API key in `DatoCMSClientTest.java` before running tests:

```java
private static final String TEST_API_KEY = "your-api-key";
```

## Dependencies

- [Gson](https://github.com/google/gson) 2.10.1 - JSON serialization/deserialization
- Java 17+ HttpClient - HTTP communication

## License

[Add your license information here]

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Resources

- [DatoCMS Official Website](https://www.datocms.com/)
- [DatoCMS GraphQL API Documentation](https://www.datocms.com/docs/content-delivery-api)
- [DatoCMS Community](https://community.datocms.com/)