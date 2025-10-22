package com.esports.tournament.service.impl;

import com.esports.tournament.model.User;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
