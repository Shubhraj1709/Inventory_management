package com.inventory.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
	    if (authentication == null || permission == null) return false;

	    Object principal = authentication.getPrincipal();
	    if (principal instanceof UserDetails userDetails) {
	        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
	        return authorities.stream()
	                .anyMatch(auth -> ((GrantedAuthority) auth).getAuthority().equals(permission.toString()));
	    }

	    return false;
	}

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
