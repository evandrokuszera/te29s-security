package br.edu.utfpr.security.controllers;

import br.edu.utfpr.security.model.User;
import br.edu.utfpr.security.repositories.RoleRepository;
import br.edu.utfpr.security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        novoUser.setPassword(createUserDTO.password());
        novoUser.setRoles(Set.of(basicRole));

        userRepository.save(novoUser);
        return ResponseEntity.ok().build();
    }
}
