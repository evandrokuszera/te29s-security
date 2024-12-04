package br.edu.utfpr.security.controllers;

import br.edu.utfpr.security.controllers.dtos.CreateUserDTO;
import br.edu.utfpr.security.model.User;
import br.edu.utfpr.security.repositories.RoleRepository;
import br.edu.utfpr.security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDTO createUserDTO){
        var basicRole = roleRepository.findByName("BASIC");
        var user = userRepository.findByUsername(createUserDTO.username());
        // usuário já existe!
        if (user.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var novoUser = new User();
        novoUser.setUsername(createUserDTO.username());
        novoUser.setPassword(this.bCryptPasswordEncoder.encode(createUserDTO.password()));
        novoUser.setRoles(Set.of(basicRole));

        userRepository.save(novoUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(this.userRepository.findAll());
    }
}
