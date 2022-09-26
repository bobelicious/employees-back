package com.augusto.employees.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.augusto.employees.model.Role;
import com.augusto.employees.repository.EmployeesRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeesRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
      var user =  userRepository.findByCpfOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(()-> 
                new UsernameNotFoundException("User not found cpf or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getCpf(), user.getPassword(),mapRoleAuthorities(user.getRoles()));
    }

    private Collection <? extends GrantedAuthority> mapRoleAuthorities(Set<Role> roles){
       return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
