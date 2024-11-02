package com.sena.tecmiecommercebackend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("Payment was successful!");
    }

    @GetMapping("/failed")
    public ResponseEntity<String> paymentFailed() {
        return ResponseEntity.ok("Payment failed, please try again.");
    }
}
