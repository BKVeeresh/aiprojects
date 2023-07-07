package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/engines/davinci-codex/completions";

  public static void main(String[] args) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the code with a syntax error:");
    String codeWithError = reader.readLine();

    String fixedCode = fixSyntaxError(codeWithError);
    System.out.println("Fixed code:");
    System.out.println(fixedCode);
  }

  private static String fixSyntaxError(String codeWithError) throws Exception {
    String prompt = "```java\n" + codeWithError + "\n```";
    String requestPayload = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 100}";

    URL url = new URL(API_ENDPOINT);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer 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");  // Replace with your OpenAI API key
    connection.setDoOutput(true);

    connection.getOutputStream().write(requestPayload.getBytes("UTF-8"));

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      String completion = response.toString();
      int startIndex = completion.indexOf("\"text\": \"") + 9;
      int endIndex = completion.indexOf("\"", startIndex);
      String fixedCode = completion.substring(startIndex, endIndex);
      return fixedCode;
    } else {
      throw new Exception("Error: " + responseCode);
    }
  }
}
