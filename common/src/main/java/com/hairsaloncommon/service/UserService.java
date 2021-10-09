package com.hairsaloncommon.service;

import com.hairsaloncommon.model.User;
import com.hairsaloncommon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public User save(User user) {
        userRepo.save(user);
        return user;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public Optional<User> findByUsername (String username) {
        return userRepo.findByUsername(username);
    }
}
