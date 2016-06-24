package main.java.appdirect.interview.appdirectchallenge.service;

import java.util.Optional;

import main.java.appdirect.interview.appdirectchallenge.domain.UserAccountInfo;
import main.java.appdirect.interview.appdirectchallenge.repository.UserAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class UserAccountService {

    private final UserAccountRepo userAccountRepo;

    @Autowired
    public UserAccountService(UserAccountRepo userAccountRepository) {
        this.userAccountRepo = userAccountRepository;
    }

    @RequestMapping("/user/current")
    public ResponseEntity<UserAccountInfo> currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final Optional<UserAccountInfo> user = userAccountRepo.readUsingOpenId(userDetails.getUsername());
            return user.map(u -> new ResponseEntity<>(user.get(), HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}