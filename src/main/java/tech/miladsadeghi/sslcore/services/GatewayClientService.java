package tech.miladsadeghi.sslcore.services;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class GatewayClientService {

    private final RestTemplate restTemplate;

    public String callOtherMicroservice(String endpoint, Object requestBody) {
        String gatewayUrl = "https://localhost:8090";
        try {
            String url = gatewayUrl + endpoint;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to call gateway: " + e.getMessage(), e);
        }
    }
}
