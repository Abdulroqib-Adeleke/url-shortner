package com.jug.url.utils;

import com.jug.url.auth.AuthUserDetails;
import com.jug.url.dto.proxy.UserProxy;
import com.jug.url.enums.Roles;
import com.jug.url.exceptions.AccessDeniedException;
import com.jug.url.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtilsService {

    private final UserModelRepository userModelRepository;

    public Optional<UserProxy> getPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        AuthUserDetails authUserDetails = (AuthUserDetails) auth.getPrincipal();
        if (authUserDetails == null) return  Optional.empty();
        UUID userId = UUID.fromString(authUserDetails.getUsername());

        Set<Roles> userRoles = userModelRepository.getUserRoles(userId);
        Optional<UserProxy> optionalUserProxy = userModelRepository.findUserById(userId);

        if (optionalUserProxy.isEmpty()){
            return  Optional.empty();
        }
        UserProxy userProxy = optionalUserProxy.get();
        userProxy.setRoles(userRoles);
        return Optional.of(userProxy);
    }

    public UserProxy getSecurityPrincipal(){
        Optional<UserProxy> userProxyOptional = getPrincipal();
        if (userProxyOptional.isEmpty()) throw new AccessDeniedException("Error occurred!");
        return userProxyOptional.get();
    }

    public void validateSystemAdminInRole(){
        UserProxy loggedInUser = getSecurityPrincipal();
        if (!loggedInUser.getRoles().contains(Roles.SYSTEM_ADMIN)) throw new AccessDeniedException("Unauthorized to perform this action!");
    }

    public void validateCustomerInRole() {
        UserProxy loggedInUser = getSecurityPrincipal();
        if(!loggedInUser.getRoles().contains(Roles.USER)) throw new AccessDeniedException("Unauthorized to perform this action!");
    }
}
