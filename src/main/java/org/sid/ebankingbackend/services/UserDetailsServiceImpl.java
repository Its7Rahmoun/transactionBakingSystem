package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.entities.Role;
import org.sid.ebankingbackend.entities.UserApp;
import org.sid.ebankingbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("this user not exist"));
        return user;
    }
}
