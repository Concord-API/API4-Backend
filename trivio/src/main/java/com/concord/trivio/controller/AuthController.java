package com.concord.trivio.controller;

import com.concord.trivio.dto.LoginRequest;
import com.concord.trivio.dto.LoginResponse;
import com.concord.trivio.entity.Employee;
import com.concord.trivio.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            Employee employee = (Employee) authentication.getPrincipal();
            String token = jwtUtil.generateToken(employee);
            String role = employee.isAdmin() ? "MANAGER" : "TECHNICIAN";

            return ResponseEntity.ok(new LoginResponse(token, employee.getEmployeeId(), employee.getEmail(), role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Credenciais inválidas"));
        }
    }
}
