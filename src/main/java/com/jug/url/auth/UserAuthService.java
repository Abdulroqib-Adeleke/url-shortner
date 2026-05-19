package com.jug.url.auth;

import com.jug.url.exceptions.ResourceNotFoundException;
import com.jug.url.model.UserModel;
import com.jug.url.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserModelRepository userModelRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserModel> userModelOptional = userModelRepository.findByEmail(email);

        if (userModelOptional.isEmpty()) throw new ResourceNotFoundException("User not found!");
        UserModel user = userModelOptional.get();

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(item -> new SimpleGrantedAuthority(item.name()))
                .collect(Collectors.toSet());

        return new AuthUserDetails(user.getEmail(),user.getPassword(),authorities);
    }
}
