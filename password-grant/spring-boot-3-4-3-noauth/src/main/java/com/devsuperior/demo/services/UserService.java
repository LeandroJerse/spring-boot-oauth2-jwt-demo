package com.devsuperior.demo.services;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    //Interessante para casos many to many
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(email);
        if (result.size() == 0 ) {
            throw new UsernameNotFoundException("username not found");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection userDetailsProjection : result) {
            user.addRole(new Role(userDetailsProjection.getRoleId(), userDetailsProjection.getUsername() ));
        }
        return user;
    }
}
