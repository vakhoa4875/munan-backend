package com.rhed.munan.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/public/hello")
    public String publicEndpoint() {
        return "Public endpoint - không cần xác thực";
    }

    @GetMapping("/api/user")
    public String userEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "Hello, " + jwt.getClaimAsString("preferred_username");
    }

    @GetMapping("/api/admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String adminEndpoint() {
        return "Hello Admin!";
    }
}