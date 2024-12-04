package br.edu.utfpr.security.controllers.dtos;

public record LoginResponse(String token, Long expiresIn) {
}
