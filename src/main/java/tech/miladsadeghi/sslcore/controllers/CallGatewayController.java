package tech.miladsadeghi.sslcore.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.miladsadeghi.sslcore.services.GatewayClientService;

import java.util.Map;

@RestController
@AllArgsConstructor
public class CallGatewayController {

    private final GatewayClientService gatewayClient;

    @PostMapping("/call-gateway-from-core")
    public ResponseEntity<String> testGatewayCall(@RequestBody Map<String, Integer> requestBody) {
        String response = gatewayClient.callOtherMicroservice("/gateway-health-check", requestBody);
        return ResponseEntity.ok(response);
    }
}
