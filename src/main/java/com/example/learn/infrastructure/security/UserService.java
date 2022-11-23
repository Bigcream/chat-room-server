package com.example.learn.infrastructure.security;


import com.example.learn.domain.api.user.IUserRepository;
import com.example.learn.infrastructure.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepo.findByUsername(username));
        if(!userEntity.isPresent()){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userEntity.get());
    }
}
