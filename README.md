# YouTube Transcript MCP Server

This project is an **MCP (Model Context Protocol) server** that allows users to generate transcriptions for YouTube video links. It provides an MCP tool to fetch the transcript of a given YouTube video using an external transcription service.

## Features

- Accepts YouTube video URLs and returns their transcribed text
- Integrates with an external API for fetching YouTube video transcripts
- Built with Spring Boot for easy configuration and extensibility

## Setup

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd youtube-transcript-mcp
   ```
2. **Build the project:**
   ```sh
   ./mvnw clean install
   ```
3. **MCP json**
```json
{
  "mcpServers": {
    "youtube-transcript": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-jar",
        "<path_to>/youtube-transcript-mcp/target/youtube-transcript-mcp-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}


```

## License

MIT
