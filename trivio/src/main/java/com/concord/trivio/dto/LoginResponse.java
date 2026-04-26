package com.concord.trivio.dto;

public record LoginResponse(
    String token,
    Long id,
    String email,
    String role
) {}
