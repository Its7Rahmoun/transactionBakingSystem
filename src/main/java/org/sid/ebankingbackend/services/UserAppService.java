package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.Role;
import org.sid.ebankingbackend.entities.UserApp;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.repositories.RoleRepository;
import org.sid.ebankingbackend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAppService {


    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserApp saveuser(String username,String password,String namecustomer,String rolename){
        Customer c = customerRepository.findByName(namecustomer);
        Role role = roleRepository.findByName(rolename);
        UserApp user = new UserApp(
                null,
                username,
                passwordEncoder.encode(password),
                c,
                List.of(role)
        );
        return  userRepository.save(user);
    }


    public UserApp saveuser1(String username,String password,String namecustomer,String rolename){
        Customer cx = Customer.builder().email(namecustomer+"@mail.com")
                .name(username)
                .build();
        Customer c = customerRepository.save(cx);
        Role role = roleRepository.findByName(rolename);
        UserApp user = new UserApp(
                null,
                username,
                passwordEncoder.encode(password),
                c,
                List.of(role)
        );
        return  userRepository.save(user);
    }


}
