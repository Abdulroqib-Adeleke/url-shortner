package com.jug.url.utils;

import com.jug.url.auth.AuthUserDetails;
import com.jug.url.enums.Roles;
import com.jug.url.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityUtilsService {
    private final UserModelRepository userModelRepository;

    public Optional<UserProxy> getPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        AuthUserDetails authUserDetails = (AuthUserDetails) auth.getPrincipal();
        if (authUserDetails == null) return  Optional.empty();
        String email = authUserDetails.getUsername();

        Set<Roles> userRoles = userModelRepository.getUserRoles(email);
        Optional<UserProxy> optionalUserProxy = userModelRepository.findUserByEmail(email);

        if (optionalUserProxy.isEmpty()){
            return  Optional.empty();
        }
        UserProxy userProxy = optionalUserProxy.get();
        userProxy.setRoles(userRoles);
        return Optional.of(userProxy);
    }
}
