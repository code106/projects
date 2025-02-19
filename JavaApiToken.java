import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenFetcher {
    public static void main(String[] args) {
        // Replace these with your actual details from the screenshot
        String tokenUrl     = "https://travelers.okta.com/oauth2/aus1nw6a7oh7/v1/token";
        String clientId     = "YOUR_CLIENT_ID";
        String clientSecret = "YOUR_CLIENT_SECRET";

        try {
            // Prepare Basic Authentication header
            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            // Open connection to the token URL
            URL url = new URL(tokenUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // Request body using the client_credentials grant type.
            // Adjust "scope" if required.
            String requestBody = "grant_type=client_credentials&scope=api_aggregator";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check the response code and read the response
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parse the JSON response using Jackson
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(response.toString(), Map.class);
                String accessToken = (String) responseMap.get("access_token");

                // Print the token. In Talend, assign it to context.client_token:
                // context.client_token = accessToken;
                System.out.println("Access Token: " + accessToken);
            } else {
                System.out.println("Error: " + responseCode + " - " + con.getResponseMessage());
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



/***************************************************************************************
 * This code snippet fetches an OAuth2 token from Okta using the client_credentials flow
 * and stores the access token in context.client_token.
 ***************************************************************************************/
try {
    // ----------------------------------------------------------------------------
    // 1) Define your Okta details
    // ----------------------------------------------------------------------------
    // Hardcode here, or pull from context variables:
    String tokenUrl     = "https://irs.okta.com/oauth2/aus1wo6a7oh7/v1/token";
    String clientId     = "YOUR_CLIENT_ID";     // or context.clientId
    String clientSecret = "YOUR_CLIENT_SECRET"; // or context.clientSecret

    // ----------------------------------------------------------------------------
    // 2) Prepare the request body and headers
    // ----------------------------------------------------------------------------
    String requestBody  = "grant_type=client_credentials&scope=api_aggregator";

    // Encode clientId:clientSecret as Base64 for Basic Auth
    // Make sure you have Java 8+ for Base64.
    String authString   = clientId + ":" + clientSecret;
    String encodedAuth  = java.util.Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));

    // ----------------------------------------------------------------------------
    // 3) Open HTTP connection
    // ----------------------------------------------------------------------------
    java.net.URL url = new java.net.URL(tokenUrl);
    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);

    // Set headers
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

    // ----------------------------------------------------------------------------
    // 4) Send the POST request
    // ----------------------------------------------------------------------------
    try (java.io.OutputStream os = conn.getOutputStream()) {
        byte[] input = requestBody.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
    }

    // ----------------------------------------------------------------------------
    // 5) Read the response
    // ----------------------------------------------------------------------------
    int responseCode = conn.getResponseCode();
    System.out.println("Response Code : " + responseCode);

    // If successful, parse the JSON to get the token
    if(responseCode == 200) {
        // Read response
        StringBuilder response = new StringBuilder();
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // Parse JSON using Jackson
        // Make sure you have the Jackson libraries in Talend
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        java.util.Map<String,Object> jsonMap = mapper.readValue(response.toString(), java.util.Map.class);

        // Typically, the token is in "access_token"
        String accessToken = (String) jsonMap.get("access_token");
        System.out.println("Access Token: " + accessToken);

        // ----------------------------------------------------------------------------
        // 6) Assign the token to context variable
        // ----------------------------------------------------------------------------
        context.client_token = accessToken;

    } else {
        // Error handling
        System.err.println("Failed to fetch token. Response Message: " + conn.getResponseMessage());
    }

    conn.disconnect();

} catch (Exception e) {
    e.printStackTrace();
    System.err.println("Exception while fetching token: " + e.getMessage());
}
