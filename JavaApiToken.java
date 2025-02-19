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
