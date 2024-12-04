package br.edu.utfpr.security.controllers;

import br.edu.utfpr.security.controllers.dtos.TweetDTO;
import br.edu.utfpr.security.model.Tweet;
import br.edu.utfpr.security.repositories.TweetRepository;
import br.edu.utfpr.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> createTweet(@RequestBody TweetDTO tweetDTO, JwtAuthenticationToken jwt) {

        // Recuperar o usuário por meio do UUID presente no token
        var user = userRepository.findById(UUID.fromString(jwt.getName()));

        Tweet novoTweet = new Tweet();
        novoTweet.setContent(tweetDTO.content());
        novoTweet.setUser(user.get());

        tweetRepository.save(novoTweet);
        return ResponseEntity.ok().build();
    }
}
