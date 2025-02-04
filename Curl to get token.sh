String curlCommand = "curl -X POST \"https://your-api-endpoint.com/token\" "
                   + "-H \"Content-Type: application/json\" "
                   + "-u \"your_client_id:your_client_secret\" "
                   + "-d '{\"grant_type\": \"client_credentials\"}'";
curl -X POST "https://vpce-02762b.amazonaws.com/prod/api/abc" \
     -H "Authorization: Bearer token" \
     -H "x-api-key: aaaa" \
     -H "x-apigw-api-id: bbbb" \
     -H "Content-Type: application/json" \
     -d '["T1111111", "T2406714"]'
     
String curlCommand = "curl -X POST \"https://vpce-02762b.amazonaws.com/prod/api/abc\" "
                   + "-H \"Authorization: Bearer token\" "
                   + "-H \"x-api-key: aaaa\" "
                   + "-H \"x-apigw-api-id: bbbb\" "
                   + "-H \"Content-Type: application/json\" "
                   + "-d '[\"T1111111\", \"T2406714\"]'";
                   
