package tech.miladsadeghi.sslcore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallGatewayController {

    @GetMapping("/call-gateway-from-core")
    public ResponseEntity<String> callGateway() {
        return new ResponseEntity<>("Gateway server has been called.", HttpStatus.OK);
    }
}
