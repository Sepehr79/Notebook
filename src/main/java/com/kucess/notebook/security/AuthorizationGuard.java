package com.kucess.notebook.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationGuard {

    public boolean checkUserId(Authentication authentication, String username){
        return authentication.getName().equals(username);
    }

}
