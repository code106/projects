import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

try {
    // Define API URL
    String apiUrl = "https://vpce-0278.execute-api.us-east-1.vpce.amazonaws.com/prod/api/v1/views/oca-Idalm-data-aggregator";

    // Define JSON Request Body
    String jsonInputString = "[\"T1111111\", \"12406714\"]";

    // Create URL object
    URL url = new URL(apiUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
    // Set request method to POST
    conn.setRequestMethod("POST");
    
    // Set headers
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("x-api-key", "<your_api_key>");
    conn.setRequestProperty("x-apigw-apl-id", "<your_apigw_apl_id>");
    conn.setRequestProperty("Authorization", "Bearer <your_token>");
    
    // Enable output for sending data
    conn.setDoOutput(true);

    // Write JSON data
    try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
    }

    // Get response code
    int responseCode = conn.getResponseCode();
    System.out.println("Response Code: " + responseCode);

    // Read response
    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
    StringBuilder response = new StringBuilder();
    String responseLine;
    while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
    }
    
    // Print response
    System.out.println("Response: " + response.toString());

    // Close connection
    conn.disconnect();

} catch (Exception e) {
    e.printStackTrace();
}
