package br.edu.utfpr.security.controllers;

import br.edu.utfpr.security.controllers.dtos.LoginRequest;
import br.edu.utfpr.security.controllers.dtos.LoginResponse;
import br.edu.utfpr.security.model.Role;
import br.edu.utfpr.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dto){

        var user = userRepository.findByUsername(dto.username());
        if (user.isEmpty() ||
                !bCryptPasswordEncoder.matches(dto.password(), user.get().getPassword())){
            throw new BadCredentialsException("username or password invalid");
        }
        // se passar... usuário existe.

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("br.edu.utfpr.security")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        var loginResponse = new LoginResponse(jwtValue, expiresIn);
        return ResponseEntity.ok(loginResponse);
    }

}
